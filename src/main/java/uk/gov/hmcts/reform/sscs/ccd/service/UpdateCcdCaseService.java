package uk.gov.hmcts.reform.sscs.ccd.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.exception.ExitRetryableException;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Slf4j
@Service
public class UpdateCcdCaseService {

    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;
    private final ReadCcdCaseService readCcdCaseService;

    @Autowired
    public UpdateCcdCaseService(IdamService idamService,
                                SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient, ReadCcdCaseService readCcdCaseService) {
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
        this.readCcdCaseService = readCcdCaseService;
    }

    @Retryable
    public SscsCaseDetails updateCaseV2(Long caseId, String eventType, String summary, String description, IdamTokens idamTokens, Consumer<SscsCaseDetails> mutator) {
        return updateCaseV2(caseId, eventType, idamTokens, caseDetails -> {
            mutator.accept(caseDetails);
            return new UpdateResult(summary, description);
        });
    }

    @Retryable
    public SscsCaseDetails updateCaseV2WithUnaryFunction(Long caseId, String eventType, String summary, String description, IdamTokens idamTokens, UnaryOperator<SscsCaseDetails> mutator) {
        return updateCaseV2(caseId, eventType, idamTokens, caseDetails -> {
            SscsCaseDetails sscsCaseDetails = mutator.apply(caseDetails);
            return new UpdateResult(sscsCaseDetails, summary, description);
        });
    }

    @Recover
    public SscsCaseDetails recoverUpdateCaseV2WithUnaryFunction(RuntimeException exception, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens, UnaryOperator<SscsCaseDetails> mutator) {
        log.error("In recover method(updateCaseV2WithUnaryFunction) for caseId {} and eventType {}",
                caseId,
                eventType,
                exception);
        throw exception;
    }

    public SscsCaseDetails updateCaseV2WithoutRetry(Long caseId, String eventType, String summary, String description, IdamTokens idamTokens, Consumer<SscsCaseDetails> mutator) {
        return updateCaseV2(caseId, eventType, idamTokens, caseDetails -> {
            mutator.accept(caseDetails);
            return new UpdateResult(summary, description);
        });
    }

    @Retryable
    public SscsCaseDetails triggerCaseEventV2(Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        return updateCaseV2(caseId, eventType, idamTokens, caseDetails -> new UpdateResult(summary, description));
    }

    public record UpdateResult(SscsCaseDetails sscsCaseDetails, String summary, String description) {
        public UpdateResult(String summary, String description) {
            this(null, summary, description);
        }
    }

    /**
     * Update a case while making correct use of CCD's optimistic locking.
     * Changes can be made to case data by the provided consumer which will always be provided
     * the current version of case data from CCD's start event.
     */
    @Retryable
    public SscsCaseDetails updateCaseV2(Long caseId, String eventType, IdamTokens idamTokens, Function<SscsCaseDetails, UpdateResult> mutator) {
        log.info("UpdateCaseV2 for caseId {} and eventType {}", caseId, eventType);
        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        SscsCaseDetails caseDetails = sscsCcdConvertService.getCaseDetails(startEventResponse);
        SscsCaseData data = caseDetails.getData();

        /**
         * @see uk.gov.hmcts.reform.sscs.ccd.deserialisation.SscsCaseCallbackDeserializer#deserialize(String)
         * setCcdCaseId & sortCollections are called above, so this functionality has been replicated here preserving existing logic
         */
        data.setCcdCaseId(caseId.toString());
        data.sortCollections();

        var result = mutator.apply(caseDetails);
        SscsCaseData sscsCaseData = caseDetails.getData();
        if (result.sscsCaseDetails != null) {
            log.info("Result contains sscsCaseDetails for caseId {}", caseId);
            sscsCaseData = result.sscsCaseDetails.getData();
        }

        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(sscsCaseData, startEventResponse, result.summary, result.description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    public record ConditionalUpdateResult(String summary, String description, Boolean willCommit) { }

    /**
     * Conditionally update a case, by passing a boolean parameter as part of the mutator
     * If true it will update data while making correct use of CCD's optimistic locking.
     * Changes can be made to case data by the provided consumer which will always be provided
     * the current version of case data from CCD's start event.
     */
    @Retryable
    public Optional<SscsCaseDetails> updateCaseV2Conditional(Long caseId, String eventType, IdamTokens idamTokens, Function<SscsCaseDetails, ConditionalUpdateResult> mutator) {
        log.info("UpdateCaseV2 for caseId {} and eventType {}", caseId, eventType);
        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        SscsCaseDetails caseDetails = sscsCcdConvertService.getCaseDetails(startEventResponse);
        SscsCaseData data = caseDetails.getData();

        /**
         * @see uk.gov.hmcts.reform.sscs.ccd.deserialisation.SscsCaseCallbackDeserializer#deserialize(String)
         * setCcdCaseId & sortCollections are called above, so this functionality has been replicated here preserving existing logic
         */
        data.setCcdCaseId(caseId.toString());
        data.sortCollections();

        var result = mutator.apply(caseDetails);
        if (result.willCommit()) {
            CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseDetails.getData(), startEventResponse, result.summary, result.description);
            return Optional.of(sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)));
        } else {
            return Optional.empty();
        }
    }

    public record DynamicEventUpdateResult(String summary, String description, Boolean willCommit, String eventType) { }

    // prob of a concurrency event happening * prob of that event or data changes the postponement field in case data (potentially resulting in different event)

    /**
     * Update a case while making correct use of CCD's optimistic locking,
     * when event is dyanamic can be made to case data by the provided mutator which will always be provided
     * the current version of case data from CCD's start event.
     */
    @Retryable(exclude = ExitRetryableException.class)
    public Optional<SscsCaseDetails> updateCaseV2DynamicEvent(Long caseId, IdamTokens idamTokens, Function<SscsCaseDetails, DynamicEventUpdateResult> mutator) {
        LocalDateTime initialLastModified;
        LocalDateTime latestLastModified;

        SscsCaseDetails initialCaseDetails = readCcdCaseService.getByCaseId(caseId, idamTokens);
        SscsCaseData caseData = initialCaseDetails.getData();

        /**
          * @see uk.gov.hmcts.reform.sscs.ccd.deserialisation.SscsCaseCallbackDeserializer#deserialize(String)
          * setCcdCaseId & sortCollections are called above, so this functionality has been replicated here preserving existing logic
        */
        caseData.setCcdCaseId(caseId.toString());
        caseData.sortCollections();

        initialLastModified = initialCaseDetails.getLastModified();

        DynamicEventUpdateResult dynamicEventUpdateResult = mutator.apply(initialCaseDetails);

        if (dynamicEventUpdateResult.willCommit()) {

            String eventType = dynamicEventUpdateResult.eventType;

            log.info("UpdateCaseV2 for caseId {} and eventType {}", caseId, eventType);
            StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
            SscsCaseDetails latestCaseDetails = sscsCcdConvertService.getCaseDetails(startEventResponse);

            latestLastModified = latestCaseDetails.getLastModified();

            if (!initialLastModified.isEqual(latestLastModified)){
                throw new RuntimeException();
            }

            CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, dynamicEventUpdateResult.summary, dynamicEventUpdateResult.description);
            return Optional.of(sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent)));
        } else {
            return Optional.empty();
        }
    }

    @Retryable
    @Deprecated(since = "since 18/10/2024, use updateCaseV2 instead", forRemoval = true)
    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        log.info("UpdateCase for caseId {} and eventType {}", caseId, eventType);

        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, summary, description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    @Deprecated(since = "since 18/10/2024, use updateCaseV2 instead", forRemoval = true)
    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventId, String eventToken, String eventType, String summary,
                                      String description, IdamTokens idamTokens) {
        log.info("UpdateCase for caseId {} eventToken {} and eventType {}", caseId, eventToken, eventType);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(eventToken, eventId, caseData, summary, description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    @Deprecated(since = "since 18/10/2024, use updateCaseV2WithoutRetry instead", forRemoval = true)
    public SscsCaseDetails updateCaseWithoutRetry(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        log.info("UpdateCase for caseId {} and eventType {}", caseId, eventType);

        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, summary, description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    @Recover
    protected SscsCaseDetails recover(RuntimeException exception, SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {

        log.info("Requesting IDAM tokens to update caseId {} with eventType {}", caseId, eventType);

        idamTokens = idamService.getIdamTokens();

        log.info("Received IDAM tokens for updating caseId {} with eventType {}", caseId, eventType);

        return updateCase(caseData, caseId, eventType, summary, description, idamTokens);
    }

    public void setSupplementaryData(IdamTokens idamTokens, Long caseId, Map<String, Map<String, Map<String, Object>>> supplementaryData) {
        log.info("Setting supplementary data for caseId {} ", caseId);

        ccdClient.setSupplementaryData(idamTokens, caseId, supplementaryData);
    }

    /**
     * Need to provide this so that recoverable/non-recoverable exception doesn't get wrapped in an IllegalArgumentException
     */
    @Recover
    public SscsCaseDetails recoverUpdateCaseV2(RuntimeException exception, Long caseId, String eventType) {
        log.error("In recover method(recoverUpdateCaseV2) for caseId {} and eventType {}", caseId, eventType);
       throw exception;
    }

}

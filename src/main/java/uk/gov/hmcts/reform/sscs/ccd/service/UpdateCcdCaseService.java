package uk.gov.hmcts.reform.sscs.ccd.service;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Slf4j
@Service
public class UpdateCcdCaseService {

    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;

    @Autowired
    public UpdateCcdCaseService(IdamService idamService,
                                SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient) {
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
    }

    @Retryable(maxAttempts = 2)
    public SscsCaseDetails updateCaseV2(Long caseId, String eventType, String summary, String description, IdamTokens idamTokens, Consumer<SscsCaseData> mutator) {
        return updateCaseV2(caseId, eventType, idamTokens, data -> {
            mutator.accept(data);
            return new UpdateResult(summary, description);
        });
    }

    @Retryable(maxAttempts = 2)
    public SscsCaseDetails triggerCaseEventV2(Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        return updateCaseV2(caseId, eventType, idamTokens, data -> new UpdateResult(summary, description));
    }

    public record UpdateResult(String summary, String description) { }

    /**
     * Update a case while making correct use of CCD's optimistic locking.
     * Changes can be made to case data by the provided consumer which will always be provided
     * the current version of case data from CCD's start event.
     */
    @Retryable(maxAttempts = 2)
    public SscsCaseDetails updateCaseV2(Long caseId, String eventType, IdamTokens idamTokens, Function<SscsCaseData, UpdateResult> mutator) {
        log.info("UpdateCaseV2 for caseId {} and eventType {}", caseId, eventType);
        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        var data = sscsCcdConvertService.getCaseData(startEventResponse.getCaseDetails().getData());

        /**
         * @see uk.gov.hmcts.reform.sscs.ccd.deserialisation.SscsCaseCallbackDeserializer#deserialize(String)
         * setCcdCaseId & sortCollections are called above, so this functionality has been replicated here preserving existing logic
         */
        data.setCcdCaseId(caseId.toString());
        data.sortCollections();

        var result = mutator.apply(data);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(data, startEventResponse, result.summary, result.description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    @Retryable
    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        log.info("UpdateCase for caseId {} and eventType {}", caseId, eventType);

        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, summary, description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventId, String eventToken, String eventType, String summary,
                                        String description, IdamTokens idamTokens) {
        log.info("UpdateCase for caseId {} eventToken {} and eventType {}", caseId, eventToken, eventType);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(eventToken, eventId, caseData, summary, description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    public SscsCaseDetails updateCaseWithoutRetry(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        log.info("UpdateCase for caseId {} and eventType {}", caseId, eventType);

        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, summary, description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }

    @Recover
    protected SscsCaseDetails recover(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {

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
    public SscsCaseDetails recoverUpdateCaseV2(Long caseId, String eventType, IdamTokens idamTokens, Function<SscsCaseData, UpdateResult> mutator) {
        log.error("In recover method(recoverUpdateCaseV2) for caseId {} and eventType {}", caseId, eventType);

        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        var data = sscsCcdConvertService.getCaseData(startEventResponse.getCaseDetails().getData());

        /**
         * @see uk.gov.hmcts.reform.sscs.ccd.deserialisation.SscsCaseCallbackDeserializer#deserialize(String)
         * setCcdCaseId & sortCollections are called above, so this functionality has been replicated here preserving existing logic
         */
        data.setCcdCaseId(caseId.toString());
        data.sortCollections();

        var result = mutator.apply(data);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(data, startEventResponse, result.summary, result.description);

        return sscsCcdConvertService.getCaseDetails(ccdClient.submitEventForCaseworker(idamTokens, caseId, caseDataContent));
    }
}

package uk.gov.hmcts.reform.sscs.ccd.service;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SUBSCRIPTION_UPDATED;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.ccd.exception.CcdException;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.utility.AppealNumberGenerator;

@Service
@Slf4j
public class CcdService {
    public static final String ERROR_WHILE_GETTING_CASE_FROM_CCD = "Error while getting case from ccd";
    private final CreateCcdCaseService createCcdCaseService;
    private final SearchCcdCaseService searchCcdCaseService;
    private final UpdateCcdCaseService updateCcdCaseService;
    private final ReadCcdCaseService readCcdCaseService;
    private final CcdClient ccdClient;
    private final SscsCcdConvertService sscsCcdConvertService;
    private static final String YES = "yes";
    private static final String NO = "no";
    private final boolean sscs2Enabled;

    @Autowired
    public CcdService(CreateCcdCaseService createCcdCaseService,
                      SearchCcdCaseService searchCcdCaseService,
                      UpdateCcdCaseService updateCcdCaseService,
                      ReadCcdCaseService readCcdCaseService,
                      CcdClient ccdClient,
                      SscsCcdConvertService sscsCcdConvertService,
                      @Value("${feature.sscs2.enabled:false}") boolean sscs2Enabled) {
        this.createCcdCaseService = createCcdCaseService;
        this.searchCcdCaseService = searchCcdCaseService;
        this.updateCcdCaseService = updateCcdCaseService;
        this.readCcdCaseService = readCcdCaseService;
        this.ccdClient = ccdClient;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.sscs2Enabled = sscs2Enabled;
    }

    public List<SscsCaseDetails> findCaseBy(String field, String value, IdamTokens idamTokens) {
        SearchSourceBuilder searchBuilder = findCaseBySingleField(field, value);

        return findCaseByQuery(searchBuilder, idamTokens);
    }

    public List<SscsCaseDetails> findCaseByQuery(SearchSourceBuilder searchBuilder, IdamTokens idamTokens) {
        try {
            return searchCcdCaseService.findCaseBySearchCriteria(searchBuilder.toString(), idamTokens);
        } catch (Exception ex) {
            throw logCcdException(ERROR_WHILE_GETTING_CASE_FROM_CCD, ex);
        }
    }

    public SscsCaseDetails findCaseByAppealNumber(String appealNumber, IdamTokens idamTokens) {
        try {
            return getCaseByAppealNumber(appealNumber, idamTokens);
        } catch (Exception ex) {
            throw logCcdException(ERROR_WHILE_GETTING_CASE_FROM_CCD, ex);
        }
    }

    public SscsCaseDetails getCaseForModification(Long caseId, IdamTokens idamTokens, String eventType) {
        StartEventResponse startEventResponse = ccdClient.startEvent(idamTokens, caseId, eventType);
        return sscsCcdConvertService.getCaseDetails(startEventResponse);
    }

    public SscsCaseDetails getByCaseId(Long caseId, IdamTokens idamTokens) {
        return readCcdCaseService.getByCaseId(caseId, idamTokens);
    }

    public SscsCaseDetails createCase(SscsCaseData caseData, String eventType, String summary, String description, IdamTokens idamTokens) {
        return createCcdCaseService.createCase(caseData, eventType, summary, description, idamTokens);
    }

    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        return updateCcdCaseService.updateCase(caseData, caseId, eventType, summary, description, idamTokens);
    }

    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventId, String eventToken, String eventType,
                                        String summary, String description, IdamTokens idamTokens) {
        return updateCcdCaseService.updateCase(caseData, caseId, eventId, eventToken, eventType, summary, description, idamTokens);
    }

    public SscsCaseDetails updateCaseWithoutRetry(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        return updateCcdCaseService.updateCaseWithoutRetry(caseData, caseId, eventType, summary, description, idamTokens);
    }

    public SscsCaseDetails findCcdCaseByNinoAndBenefitTypeAndMrnDate(String nino, String benefitCode, String mrnDate, IdamTokens idamTokens) {
        if (mrnDate != null) {
            try {
                SearchSourceBuilder searchBuilder = findCcdCaseByNinoAndBenefitTypeAndMrnDateQuery(nino, benefitCode, mrnDate);

                List<SscsCaseDetails> caseDetails = searchCcdCaseService.findCaseBySearchCriteria(searchBuilder.toString(), idamTokens);

                return !caseDetails.isEmpty() ? caseDetails.get(0) : null;
            } catch (Exception ex) {
                throw logCcdException(ERROR_WHILE_GETTING_CASE_FROM_CCD, ex);
            }
        }
        return null;
    }

    public SscsCaseDetails updateSubscription(String appealNumber, String email, IdamTokens idamTokens) {
        try {
            SscsCaseDetails caseDetails = getCaseByAppealNumber(appealNumber, idamTokens);

            if (caseDetails != null) {
                SscsCaseData caseData = caseDetails.getData();
                Subscriptions caseSubscriptions = caseData.getSubscriptions();

                caseSubscriptions = updateAppellantSubscription(appealNumber, email, caseSubscriptions);
                caseSubscriptions = updateAppointeeSubscription(appealNumber, email, caseSubscriptions);
                caseSubscriptions = updateRepresentativeSubscription(appealNumber, email, caseSubscriptions);

                caseData.setSubscriptions(caseSubscriptions);

                return updateCase(caseData, caseDetails.getId(), SUBSCRIPTION_UPDATED.getCcdType(),
                        "SSCS - appeal updated event", "Update SSCS subscription", idamTokens);
            }
        } catch (Exception ex) {
            throw logCcdException("Error while updating details in ccd", ex);
        }
        return null;
    }

    private Optional<Subscription> updateSubscription(final Subscription subscription, final String tya, String email) {
        if (subscription != null && tya.equals(subscription.getTya())) {
            String subscribeEmail = null != email ? YES : NO;
            return Optional.of(subscription.toBuilder().email(email).subscribeEmail(subscribeEmail).build());
        }
        return Optional.empty();
    }

    private Subscriptions updateAppellantSubscription(String appealNumber, String email, Subscriptions caseSubscriptions) {
        Subscription appellantSubscription = caseSubscriptions.getAppellantSubscription();

        return updateSubscription(appellantSubscription, appealNumber, email)
            .map(updatedSubscription ->
                    caseSubscriptions.toBuilder().appellantSubscription(updatedSubscription).build()
            ).orElse(caseSubscriptions);
    }

    private Subscriptions updateAppointeeSubscription(String appealNumber, String email, Subscriptions caseSubscriptions) {
        Subscription appointeeSubscription = caseSubscriptions.getAppointeeSubscription();

        return updateSubscription(appointeeSubscription, appealNumber, email)
            .map(updatedSubscription ->
                    caseSubscriptions.toBuilder().appointeeSubscription(updatedSubscription).build()
            ).orElse(caseSubscriptions);
    }

    private Subscriptions updateRepresentativeSubscription(String appealNumber, String email, Subscriptions caseSubscriptions) {
        Subscription representativeSubscription = caseSubscriptions.getRepresentativeSubscription();

        return updateSubscription(representativeSubscription, appealNumber, email)
            .map(updatedSubscription ->
                    caseSubscriptions.toBuilder().representativeSubscription(updatedSubscription).build()
            ).orElse(caseSubscriptions);
    }

    private SscsCaseDetails getCaseByAppealNumber(String appealNumber, IdamTokens idamTokens) {
        log.info("Finding case by appeal number {}", appealNumber);

        SearchSourceBuilder searchBuilder;
        if (sscs2Enabled) {
            searchBuilder = findCaseByTyaNumberQueryWithOtherParty(appealNumber);
        } else {
            searchBuilder = findCaseByTyaNumberQuery(appealNumber);
        }


        List<SscsCaseDetails> caseDetailsList = searchCcdCaseService.findCaseBySearchCriteria(searchBuilder.toString(), idamTokens);

        caseDetailsList = caseDetailsList.stream()
                .filter(AppealNumberGenerator::filterCaseNotDraftOrArchivedDraft)
                .collect(Collectors.toList());

        return !caseDetailsList.isEmpty() ? caseDetailsList.get(0) : null;
    }

    private CcdException logCcdException(String message, Exception ex) {
        CcdException ccdException = new CcdException(message, ex);
        log.error(message, ccdException);
        return ccdException;
    }

    public void setSupplementaryData(IdamTokens idamTokens, Long caseId, Map<String, Map<String, Map<String, Object>>> supplementaryData) {
        updateCcdCaseService.setSupplementaryData(idamTokens, caseId, supplementaryData);
    }
}

package uk.gov.hmcts.reform.sscs.ccd.service;

import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SUBSCRIPTION_UPDATED;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.Subscription;
import uk.gov.hmcts.reform.sscs.ccd.domain.Subscriptions;
import uk.gov.hmcts.reform.sscs.ccd.exception.AppealNotFoundException;
import uk.gov.hmcts.reform.sscs.ccd.exception.CcdException;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.utility.SurnameUtil;

@Service
@Slf4j
public class CcdService {
    public static final String ERROR_WHILE_GETTING_CASE_FROM_CCD = "Error while getting case from ccd";
    private final CreateCcdCaseService createCcdCaseService;
    private final SearchCcdCaseService searchCcdCaseService;
    private final UpdateCcdCaseService updateCcdCaseService;
    private final ReadCcdCaseService readCcdCaseService;
    private static final String YES = "yes";
    private static final String NO = "no";

    @Autowired
    public CcdService(CreateCcdCaseService createCcdCaseService,
                      SearchCcdCaseService searchCcdCaseService,
                      UpdateCcdCaseService updateCcdCaseService,
                      ReadCcdCaseService readCcdCaseService) {
        this.createCcdCaseService = createCcdCaseService;
        this.searchCcdCaseService = searchCcdCaseService;
        this.updateCcdCaseService = updateCcdCaseService;
        this.readCcdCaseService = readCcdCaseService;
    }

    public List<SscsCaseDetails> findCaseBy(Map<String, String> searchCriteria, IdamTokens idamTokens) {
        try {
            return searchCcdCaseService.findCaseBySearchCriteria(searchCriteria, idamTokens);
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

    public SscsCaseDetails getByCaseId(Long caseId, IdamTokens idamTokens) {
        return readCcdCaseService.getByCaseId(caseId, idamTokens);
    }

    public SscsCaseDetails createCase(SscsCaseData caseData, String eventType, String summary, String description, IdamTokens idamTokens) {
        return createCcdCaseService.createCase(caseData, eventType, summary, description, idamTokens);
    }

    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
        return updateCcdCaseService.updateCase(caseData, caseId, eventType, summary, description, idamTokens);
    }

    public SscsCaseDetails findCcdCaseByNinoAndBenefitTypeAndMrnDate(SscsCaseData caseData, IdamTokens idamTokens) {
        if (caseData.getAppeal().getMrnDetails().getMrnDate() != null) {
            try {
                List<SscsCaseDetails> caseDetails = searchCcdCaseService.findCaseBySearchCriteria(ImmutableMap.of(
                        "case.generatedNino", caseData.getGeneratedNino(),
                        "case.appeal.benefitType.code", caseData.getAppeal().getBenefitType().getCode(),
                        "case.appeal.mrnDetails.mrnDate", caseData.getAppeal().getMrnDetails().getMrnDate()),
                        idamTokens);

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

    public SscsCaseData findCcdCaseByAppealNumberAndSurname(String appealNumber, String surname, IdamTokens idamTokens) {
        SscsCaseDetails details = findCaseByAppealNumber(appealNumber, idamTokens);
        if (details == null) {
            log.info("Appeal does not exist for appeal number: {}", appealNumber);
            throw new AppealNotFoundException(appealNumber);
        }
        SscsCaseData caseData = (SscsCaseData) details.getData();
        caseData.setCcdCaseId(String.valueOf(details.getId()));
        return (doesMatchAppellantAppealNumberAndLastname(surname, caseData, appealNumber)
                || doesMatchRepresentativeAppealNumberAndLastname(surname, caseData, appealNumber)
                || doesMatchAppointeeAppealNumberAndLastname(surname, caseData, appealNumber)) ? caseData : null;
    }

    private boolean doesMatchAppellantAppealNumberAndLastname(String surname, SscsCaseData caseData, String appealNumber) {
        return caseData.getAppeal() != null && caseData.getAppeal().getAppellant() != null
                && caseData.getAppeal().getAppellant().getName() != null
                && caseData.getAppeal().getAppellant().getName().getLastName() != null
                && appealNumber.equals(caseData.getSubscriptions().getAppellantSubscription().getTya())
                && SurnameUtil.compare(surname, caseData.getAppeal().getAppellant().getName().getLastName());
    }

    private boolean doesMatchRepresentativeAppealNumberAndLastname(String surname, SscsCaseData caseData, String appealNumber) {
        return caseData.getAppeal() != null && caseData.getAppeal().getRep() != null
                && caseData.getAppeal().getRep().getName() != null
                && caseData.getAppeal().getRep().getName().getLastName() != null
                && appealNumber.equals(caseData.getSubscriptions().getRepresentativeSubscription().getTya())
                && SurnameUtil.compare(surname, caseData.getAppeal().getRep().getName().getLastName());
    }

    private boolean doesMatchAppointeeAppealNumberAndLastname(String surname, SscsCaseData caseData, String appealNumber) {
        return caseData.getAppeal() != null && caseData.getAppeal().getAppointee() != null
                && caseData.getAppeal().getAppointee().getName() != null
                && caseData.getAppeal().getAppointee().getName().getLastName() != null
                && appealNumber.equals(caseData.getSubscriptions().getAppointeeSubscription().getTya())
                && SurnameUtil.compare(surname, caseData.getAppeal().getAppointee().getName().getLastName());
    }

    private SscsCaseDetails getCaseByAppealNumber(String appealNumber, IdamTokens idamTokens) {
        log.info("Finding case by appeal number {}", appealNumber);

        List<SscsCaseDetails> caseDetailsList = searchCcdCaseService.findCaseBySearchCriteria(ImmutableMap.of(
            "case.subscriptions.appellantSubscription.tya", appealNumber), idamTokens);

        if (caseDetailsList.isEmpty()) {
            caseDetailsList = searchCcdCaseService.findCaseBySearchCriteria(ImmutableMap.of(
                "case.subscriptions.appointeeSubscription.tya", appealNumber), idamTokens);
        }

        if (caseDetailsList.isEmpty()) {
            caseDetailsList = searchCcdCaseService.findCaseBySearchCriteria(ImmutableMap.of(
                "case.subscriptions.representativeSubscription.tya", appealNumber), idamTokens);
        }

        return !caseDetailsList.isEmpty() ? caseDetailsList.get(0) : null;
    }

    private CcdException logCcdException(String message, Exception ex) {
        CcdException ccdException = new CcdException(message, ex);
        log.error(message, ccdException);
        return ccdException;
    }
}

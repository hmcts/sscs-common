package uk.gov.hmcts.reform.sscs.ccd.service;

import static gcardone.junidecode.Junidecode.unidecode;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SUBSCRIPTION_UPDATED;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
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

    public SscsCaseDetails createCase(SscsCaseData caseData, IdamTokens idamTokens) {
        return createCcdCaseService.createCase(caseData, idamTokens);
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
                String subscribeEmail = null != email ? YES : NO;
                if (caseData.getSubscriptions().getAppellantSubscription() != null && appealNumber.equals(caseData.getSubscriptions().getAppellantSubscription().getTya())) {
                    updateAppellantSubscription(email, caseData, subscribeEmail);
                } else if (caseData.getSubscriptions().getRepresentativeSubscription() != null && appealNumber.equals(caseData.getSubscriptions().getRepresentativeSubscription().getTya())) {
                    updateRepresentativeSubscription(email, caseData, subscribeEmail);
                }
                return updateCase(caseData, caseDetails.getId(), SUBSCRIPTION_UPDATED.getCcdType(),
                        "SSCS - appeal updated event", "Update SSCS subscription", idamTokens);
            }
        } catch (Exception ex) {
            throw logCcdException("Error while updating details in ccd", ex);
        }
        return null;
    }

    private void updateAppellantSubscription(String email, SscsCaseData caseData, String subscribeEmail) {
        Subscription appellantSubscription = caseData.getSubscriptions()
                .getAppellantSubscription().toBuilder().email(email).subscribeEmail(subscribeEmail).build();

        Subscriptions subscriptions = caseData.getSubscriptions().toBuilder().appellantSubscription(appellantSubscription).build();

        caseData.setSubscriptions(subscriptions);
    }

    private void updateRepresentativeSubscription(String email, SscsCaseData caseData, String subscribeEmail) {
        Subscription representativeSubscription = caseData.getSubscriptions()
                .getRepresentativeSubscription().toBuilder().email(email).subscribeEmail(subscribeEmail).build();

        Subscriptions subscriptions = caseData.getSubscriptions().toBuilder().representativeSubscription(representativeSubscription).build();

        caseData.setSubscriptions(subscriptions);
    }

    public SscsCaseData findCcdCaseByAppealNumberAndSurname(String appealNumber, String surname, IdamTokens idamTokens) {
        SscsCaseDetails details = findCaseByAppealNumber(appealNumber, idamTokens);
        if (details == null) {
            log.info("Appeal does not exist for appeal number: {}", appealNumber);
            throw new AppealNotFoundException(appealNumber);
        }
        SscsCaseData caseData = details.getData();
        caseData.setCcdCaseId(String.valueOf(details.getId()));
        return (doesMatchAppellantAppealNumberAndLastname(surname, caseData, appealNumber)
                || doesMatchRepresentativeAppealNumberAndLastname(surname, caseData, appealNumber)) ? caseData : null;
    }

    private boolean doesMatchAppellantAppealNumberAndLastname(String surname, SscsCaseData caseData, String appealNumber) {
        return caseData.getAppeal() != null && caseData.getAppeal().getAppellant() != null
                && caseData.getAppeal().getAppellant().getName() != null
                && caseData.getAppeal().getAppellant().getName().getLastName() != null
                && caseData.getSubscriptions().getAppellantSubscription().getTya().equals(appealNumber)
                && compareSurnames(surname, caseData.getAppeal().getAppellant().getName().getLastName());
    }

    private boolean doesMatchRepresentativeAppealNumberAndLastname(String surname, SscsCaseData caseData, String appealNumber) {
        return caseData.getAppeal() != null && caseData.getAppeal().getRep() != null
                && caseData.getAppeal().getRep().getName() != null
                && caseData.getAppeal().getRep().getName().getLastName() != null
                && caseData.getSubscriptions().getRepresentativeSubscription().getTya().equals(appealNumber)
                && compareSurnames(surname, caseData.getAppeal().getRep().getName().getLastName());
    }

    private SscsCaseDetails getCaseByAppealNumber(String appealNumber, IdamTokens idamTokens) {
        log.info("Finding case by appeal number {}", appealNumber);

        List<SscsCaseDetails> caseDetailsList = getSscsCaseDetailsByAppellantAppealNumber(appealNumber, idamTokens);

        if (caseDetailsList.isEmpty()) {
            caseDetailsList = getSscsCaseDetailsByRepresentativeAppealNumber(appealNumber, idamTokens);
        }

        return !caseDetailsList.isEmpty() ? caseDetailsList.get(0) : null;
    }

    private List<SscsCaseDetails> getSscsCaseDetailsByAppellantAppealNumber(String appealNumber, IdamTokens idamTokens) {
        return searchCcdCaseService.findCaseBySearchCriteria(ImmutableMap.of(
                    "case.subscriptions.appellantSubscription.tya", appealNumber), idamTokens);
    }

    private List<SscsCaseDetails> getSscsCaseDetailsByRepresentativeAppealNumber(String appealNumber, IdamTokens idamTokens) {
        return searchCcdCaseService.findCaseBySearchCriteria(ImmutableMap.of(
                "case.subscriptions.representativeSubscription.tya", appealNumber), idamTokens);
    }

    private boolean compareSurnames(String surname, String caseDataLastName) {
        String caseDataSurname = unidecode(caseDataLastName)
                .replaceAll("[^a-zA-Z]", "");
        String unidecodeSurname = unidecode(surname).replaceAll("[^a-zA-Z]", "");
        return caseDataSurname.equalsIgnoreCase(unidecodeSurname);
    }

    private CcdException logCcdException(String message, Exception ex) {
        CcdException ccdException = new CcdException(message, ex);
        log.error(message, ccdException);
        return ccdException;
    }
}

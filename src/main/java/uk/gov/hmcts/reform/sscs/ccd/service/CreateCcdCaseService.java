package uk.gov.hmcts.reform.sscs.ccd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.exception.CreateCcdCaseException;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;


@Slf4j
@Service
public class CreateCcdCaseService {
    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;

    @Autowired
    public CreateCcdCaseService(SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient) {
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
    }

    public SscsCaseDetails createCase(SscsCaseData caseData, String eventType, String summary, String description, IdamTokens idamTokens) {
        log.info("Starting create case process with SC number {} and ccdID {} and eventType {} ...",
                caseData.getCaseReference(), caseData.getCcdCaseId(), eventType);
        String nino = caseData.getAppeal() != null && caseData.getAppeal().getAppellant() != null && caseData.getAppeal().getAppellant().getIdentity() != null
                ? caseData.getAppeal().getAppellant().getIdentity().getNino() : null;
        try {
            return createCaseInCcd(caseData, eventType, summary, description, idamTokens, nino);
        } catch (Exception e) {

            throw new CreateCcdCaseException(String.format(
                    "Error found in the case creation or callback process for the ccd case "
                            + "with SC (%s) and ccdID (%s) and Nino (%s) and Benefit Type (%s) and exception: (%s) ",
                    caseData.getCaseReference(), caseData.getCcdCaseId(), nino,
                    caseData.getAppeal().getBenefitType().getCode(), e.getMessage()), e);
        }
    }

    private SscsCaseDetails createCaseInCcd(SscsCaseData caseData, String eventType, String summary, String description, IdamTokens idamTokens, String nino) {
        BenefitType benefitType = caseData.getAppeal() != null ? caseData.getAppeal().getBenefitType() : null;

        log.info("Creating CCD case for Nino {} and benefit type {} with event {}", nino, benefitType, eventType);

        StartEventResponse startEventResponse = ccdClient.startCaseForCaseworker(idamTokens, eventType);

        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, summary, description);
        CaseDetails caseDetails = ccdClient.submitForCaseworker(idamTokens, caseDataContent);

        return sscsCcdConvertService.getCaseDetails(caseDetails);
    }
}

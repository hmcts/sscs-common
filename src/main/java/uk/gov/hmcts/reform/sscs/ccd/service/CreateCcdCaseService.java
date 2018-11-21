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
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;


@Slf4j
@Service
public class CreateCcdCaseService {
    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;

    @Autowired
    public CreateCcdCaseService(IdamService idamService,
                                SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient) {
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
    }

    public SscsCaseDetails createCase(SscsCaseData caseData, IdamTokens idamTokens) {
        log.info("Starting create case process with SC number {} and ccdID {} ...",
                caseData.getCaseReference(), caseData.getCcdCaseId());
        try {
            requestNewIdamTokens(idamTokens);
            return createCaseInCcd(caseData, idamTokens);
        } catch (Exception e) {
            throw new CreateCcdCaseException(String.format(
                    "Error found in the case creation or callback process for the ccd case "
                            + "with SC (%s) and ccdID (%s) and Nino (%s) and Benefit Type (%s) but carrying on",
                    caseData.getCaseReference(), caseData.getCcdCaseId(), caseData.getGeneratedNino(),
                    caseData.getAppeal().getBenefitType().getCode()), e);
        }
    }

    private void requestNewIdamTokens(IdamTokens idamTokens) {
        idamTokens.setIdamOauth2Token(idamService.getIdamOauth2Token());
        idamTokens.setServiceAuthorization(idamService.generateServiceAuthorization());
    }

    private SscsCaseDetails createCaseInCcd(SscsCaseData caseData, IdamTokens idamTokens) {
        BenefitType benefitType = caseData.getAppeal() != null ? caseData.getAppeal().getBenefitType() : null;
        log.info("Creating CCD case for Nino {} and benefit type {}", caseData.getGeneratedNino(), benefitType);

        StartEventResponse startEventResponse = ccdClient.startCaseForCaseworker(idamTokens, "appealCreated");

        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "SSCS - appeal created event", "Created SSCS");
        CaseDetails caseDetails = ccdClient.submitForCaseworker(idamTokens, caseDataContent);

        return sscsCcdConvertService.getCaseDetails(caseDetails);
    }
}

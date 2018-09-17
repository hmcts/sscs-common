package uk.gov.hmcts.reform.sscs.ccd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
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

    @Retryable
    protected SscsCaseDetails createCase(SscsCaseData caseData, IdamTokens idamTokens) {
        BenefitType benefitType = caseData.getAppeal() != null ? caseData.getAppeal().getBenefitType() : null;
        log.info("Creating CCD case for Nino {} and benefit type {}", caseData.getGeneratedNino(), benefitType);

        StartEventResponse startEventResponse = ccdClient.startCaseForCaseworker(idamTokens, "appealCreated");

        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "SSCS - appeal created event", "Created SSCS");
        CaseDetails caseDetails = ccdClient.submitForCaseworker(idamTokens, caseDataContent);

        return sscsCcdConvertService.getCaseDetails(caseDetails);
    }

    @Recover
    protected SscsCaseDetails recover(SscsCaseData caseData, IdamTokens idamTokens) {
        idamTokens = idamService.getIdamTokens();

        return createCase(caseData, idamTokens);
    }
}

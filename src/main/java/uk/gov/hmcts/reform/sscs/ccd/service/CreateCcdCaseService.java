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
    public CreateCcdCaseService(IdamService idamService, SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient) {
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
    }

    @Retryable
    public SscsCaseDetails createCase(SscsCaseData caseData, String eventType, String summary, String description, IdamTokens idamTokens) {
        String nino = getAppellantNino(caseData);
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

        log.info("Case created with case id {} for nino {}", caseDetails.getId(), nino);

        return sscsCcdConvertService.getCaseDetails(caseDetails);
    }

    @Recover
    protected SscsCaseDetails recover(SscsCaseData caseData, String eventType, String summary, String description, IdamTokens idamTokens) {
        String nino = getAppellantNino(caseData);
        log.info("Requesting IDAM tokens to create case for nino {} with eventType {}", nino, eventType);

        idamTokens = idamService.getIdamTokens();

        log.info("Received IDAM tokens for creating case for nino {} with eventType {}", nino, eventType);

        return createCase(caseData, eventType, summary, description, idamTokens);
    }

    private String getAppellantNino(SscsCaseData caseData) {
        return caseData.getAppeal() != null && caseData.getAppeal().getAppellant() != null && caseData.getAppeal().getAppellant().getIdentity() != null
                ? caseData.getAppeal().getAppellant().getIdentity().getNino() : null;
    }

}

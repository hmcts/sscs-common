package uk.gov.hmcts.reform.sscs.ccd.service;

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
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
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

    @Retryable
    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventType, String summary, String description, IdamTokens idamTokens) {
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
}

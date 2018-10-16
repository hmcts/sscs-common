package uk.gov.hmcts.reform.sscs.ccd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Slf4j
@Service
public class ReadCcdCaseService {

    private final IdamService idamService;
    private final CcdClient ccdClient;
    private final SscsCcdConvertService sscsCcdConvertService;

    @Autowired
    public ReadCcdCaseService(IdamService idamService,
                                CcdClient ccdClient,
                              SscsCcdConvertService sscsCcdConvertService) {
        this.idamService = idamService;
        this.ccdClient = ccdClient;
        this.sscsCcdConvertService = sscsCcdConvertService;
    }

    @Retryable
    protected SscsCaseDetails getByCaseId(Long caseId, IdamTokens idamTokens) {
        log.info("Get getByCaseId " + caseId);

        CaseDetails caseDetails = ccdClient.readForCaseworker(idamTokens, caseId);

        if (null != caseDetails) {
            return sscsCcdConvertService.getCaseDetails(caseDetails);
        }
        return null;
    }

    @Recover
    protected SscsCaseDetails recover(Long caseId) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        return getByCaseId(caseId, idamTokens);
    }
}

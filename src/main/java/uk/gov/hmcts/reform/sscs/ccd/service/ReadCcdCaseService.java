package uk.gov.hmcts.reform.sscs.ccd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Slf4j
@Service
public class ReadCcdCaseService {

    private final IdamService idamService;
    private final CcdClient ccdClient;

    @Autowired
    public ReadCcdCaseService(IdamService idamService,
                                CcdClient ccdClient) {
        this.idamService = idamService;
        this.ccdClient = ccdClient;
    }

    @Retryable
    protected CaseDetails getByCaseId(Long caseId, IdamTokens idamTokens) {
        log.info("Get getByCaseId " + caseId);

        return ccdClient.readForCaseworker(idamTokens, caseId);
    }

    @Recover
    protected CaseDetails recover(Long caseId, IdamTokens idamTokens) {
        idamTokens = idamService.getIdamTokens();

        return ccdClient.readForCaseworker(idamTokens, caseId);
    }
}

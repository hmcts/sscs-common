package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
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
public class SearchCcdCaseService {

    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;

    @Autowired
    public SearchCcdCaseService(IdamService idamService,
                                SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient) {
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
    }

    @Retryable
    protected List<SscsCaseDetails> findCaseBySearchCriteria(Map<String, String> searchCriteria, IdamTokens idamTokens) {
        List<CaseDetails> caseDetailsList = ccdClient.searchForCaseworker(idamTokens, searchCriteria);

        return caseDetailsList.stream().map(sscsCcdConvertService::getCaseDetails).collect(toList());
    }

    @Recover
    protected List<SscsCaseDetails> recover(Map<String, String> searchCriteria, IdamTokens idamTokens) {
        idamTokens = idamService.getIdamTokens();

        return findCaseBySearchCriteria(searchCriteria, idamTokens);
    }
}

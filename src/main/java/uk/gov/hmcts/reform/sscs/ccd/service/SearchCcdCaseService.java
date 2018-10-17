package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Slf4j
@Service
public class SearchCcdCaseService {

    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;
    private final ReadCcdCaseService readCcdCaseService;

    @Autowired
    public SearchCcdCaseService(IdamService idamService,
                                SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient,
                                ReadCcdCaseService readCcdCaseService) {
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
        this.readCcdCaseService = readCcdCaseService;
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


    public SscsCaseDetails findCaseByCaseRef(String caseRef, IdamTokens idamTokens) {
        log.info("Finding case by appeal reference number {}", caseRef);

        List<SscsCaseDetails> caseDetailsList = this.findCaseBySearchCriteria(ImmutableMap.of(
                "case.caseReference", caseRef), idamTokens);

        return !caseDetailsList.isEmpty() ? caseDetailsList.get(0) : null;
    }

    public SscsCaseDetails findCaseByCaseRefOrCaseId(SscsCaseData caseData, IdamTokens idamTokens) {

        SscsCaseDetails sscsCaseDetails = null;

        if (StringUtils.isNotBlank(caseData.getCaseReference())) {
            log.info("*** case-loader *** searching cases by SC number {}", caseData.getCaseReference());
            sscsCaseDetails = this.findCaseByCaseRef(caseData.getCaseReference(), idamTokens);
        }

        if (null == sscsCaseDetails && StringUtils.isNotBlank(caseData.getCcdCaseId())) {
            log.info("*** case-loader *** searching cases by ccdID {}", caseData.getCcdCaseId());
            sscsCaseDetails = readCcdCaseService.getByCaseId(Long.parseLong(caseData.getCcdCaseId()), idamTokens);
        }
        return sscsCaseDetails;
    }

}

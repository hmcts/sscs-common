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

    SscsCaseDetails findCaseByCaseRef(String caseRef, IdamTokens idamTokens) {
        log.info("*** case-loader *** searching cases by SC number {}", caseRef);
        ImmutableMap<String, String> searchCriteria = ImmutableMap.of("case.caseReference", caseRef);
        List<SscsCaseDetails> sscsCaseDetailsList = findCaseBySearchCriteria(searchCriteria, idamTokens);
        return !sscsCaseDetailsList.isEmpty() ? sscsCaseDetailsList.get(0) : null;
    }

    public List<SscsCaseDetails> findCaseBySearchCriteria(Map<String, String> searchCriteria, IdamTokens idamTokens) {
        List<CaseDetails> caseDetailsList = ccdClient.searchForCaseworker(idamTokens, searchCriteria);
        return caseDetailsList.stream()
                .map(sscsCcdConvertService::getCaseDetails)
                .collect(toList());
    }

    @Retryable
    public SscsCaseDetails findCaseByCaseRefOrCaseId(SscsCaseData caseData, IdamTokens idamTokens) {
        return retryLogic(caseData, idamTokens);
    }

    private SscsCaseDetails retryLogic(SscsCaseData caseData, IdamTokens idamTokens) {
        SscsCaseDetails sscsCaseDetails = null;
        if (StringUtils.isNotBlank(caseData.getCaseReference())) {
            sscsCaseDetails = this.findCaseByCaseRef(caseData.getCaseReference(), idamTokens);
        }
        if (null == sscsCaseDetails && StringUtils.isNotBlank(caseData.getCcdCaseId())) {
            sscsCaseDetails = readCcdCaseService.getByCaseId(Long.parseLong(caseData.getCcdCaseId()), idamTokens);
        }
        return sscsCaseDetails;
    }

    @Recover
    public SscsCaseDetails recoverLogic(SscsCaseData caseData, IdamTokens idamTokens) {
        idamTokens = idamService.getIdamTokens();
        return retryLogic(caseData, idamTokens);
    }


}

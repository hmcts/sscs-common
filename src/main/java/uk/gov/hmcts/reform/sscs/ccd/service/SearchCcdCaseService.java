package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.findCaseBySingleField;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.utility.AppealNumberGenerator;

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

    public SscsCaseDetails findCaseByCaseRef(String caseRef, IdamTokens idamTokens) {
        log.info("searching cases by SC number {}", caseRef);

        SearchSourceBuilder searchBuilder = findCaseBySingleField("data.caseReference", caseRef);
        List<SscsCaseDetails> sscsCaseDetailsList = findCaseBySearchCriteria(searchBuilder.toString(), idamTokens);
        return null != sscsCaseDetailsList && !sscsCaseDetailsList.isEmpty() ? sscsCaseDetailsList.get(0) : null;
    }

    public List<SscsCaseDetails> findListOfCasesByCaseRef(String caseRef, IdamTokens idamTokens) {
        log.info("searching list of cases by SC number {}", caseRef);

        SearchSourceBuilder searchBuilder = findCaseBySingleField("data.caseReference", caseRef);
        return findCaseBySearchCriteria(searchBuilder.toString(), idamTokens);
    }

    @Retryable
    public List<SscsCaseDetails>  findCaseBySearchCriteria(String query, IdamTokens idamTokens) {
        return findCaseBySearchCriteriaRetryLogic(query, idamTokens);
    }

    private List<SscsCaseDetails> findCaseBySearchCriteriaRetryLogic(String query, IdamTokens idamTokens) {
        SearchResult caseDetailsList = ccdClient.searchCases(idamTokens, query);

        if (null != caseDetailsList && null != caseDetailsList.getCases()) {
            return caseDetailsList.getCases().stream()
                    .map(sscsCcdConvertService::getCaseDetails)
                    .filter(AppealNumberGenerator::filterCaseNotDraftOrArchivedDraft)
                    .collect(toList());
        }
        return null;
    }

    @Recover
    public List<SscsCaseDetails> findCaseBySearchCriteriaRecoverLogic(String query,
                                                                      IdamTokens idamTokens) {

        log.info("Requesting IDAM tokens for search");
        idamTokens = idamService.getIdamTokens();
        log.info("Received IDAM tokens for search");

        return findCaseBySearchCriteriaRetryLogic(query, idamTokens);
    }

    @Retryable
    public SscsCaseDetails findCaseByCaseRefOrCaseId(SscsCaseData caseData, IdamTokens idamTokens) {
        return findCaseByCaseRefOrCaseIdRetryLogic(caseData, idamTokens);
    }

    @Retryable
    public List<SscsCaseDetails> findListOfCasesByCaseRefOrCaseId(SscsCaseData caseData, IdamTokens idamTokens) {
        return findListOfCasesByCaseRefOrCaseIdRetryLogic(caseData, idamTokens);
    }

    private SscsCaseDetails findCaseByCaseRefOrCaseIdRetryLogic(SscsCaseData caseData, IdamTokens idamTokens) {
        SscsCaseDetails sscsCaseDetails = null;
        if (StringUtils.isNotBlank(caseData.getCaseReference())) {
            sscsCaseDetails = this.findCaseByCaseRef(caseData.getCaseReference(), idamTokens);
        }
        if (null == sscsCaseDetails && StringUtils.isNotBlank(caseData.getCcdCaseId())) {
            sscsCaseDetails = readCcdCaseService.getByCaseId(Long.parseLong(caseData.getCcdCaseId()), idamTokens);
        }
        return sscsCaseDetails;
    }

    private List<SscsCaseDetails> findListOfCasesByCaseRefOrCaseIdRetryLogic(SscsCaseData caseData, IdamTokens idamTokens) {
        List<SscsCaseDetails> sscsCaseDetailsList = new ArrayList<>();
        if (StringUtils.isNotBlank(caseData.getCaseReference())) {
            sscsCaseDetailsList = this.findListOfCasesByCaseRef(caseData.getCaseReference(), idamTokens);
        }
        if (CollectionUtils.isEmpty(sscsCaseDetailsList) && StringUtils.isNotBlank(caseData.getCcdCaseId())) {
            sscsCaseDetailsList.add(readCcdCaseService.getByCaseId(Long.parseLong(caseData.getCcdCaseId()), idamTokens));
        }
        return sscsCaseDetailsList;
    }

    @Recover
    public SscsCaseDetails findCaseByCaseRefOrCaseIdRecoverLogic(SscsCaseData caseData, IdamTokens idamTokens) {

        final String caseId = caseData.getCcdCaseId();

        log.info("Requesting IDAM tokens to get caseId {}", caseId);

        idamTokens = idamService.getIdamTokens();

        log.info("Received IDAM tokens for getting caseId {}", caseId);

        return findCaseByCaseRefOrCaseIdRetryLogic(caseData, idamTokens);
    }


}

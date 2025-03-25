package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.findCaseBySingleField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.utility.AppealNumberGenerator;

@Slf4j
@Service
public class SearchCcdCaseService {

    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;
    private final ReadCcdCaseService readCcdCaseService;

    @Autowired
    public SearchCcdCaseService(SscsCcdConvertService sscsCcdConvertService,
                                CcdClient ccdClient,
                                ReadCcdCaseService readCcdCaseService) {
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
        this.readCcdCaseService = readCcdCaseService;
    }

    @Retryable
    public SscsCaseDetails findCaseByCaseRef(String caseRef, IdamTokens idamTokens) {
        log.info("searching cases by SC number {}", caseRef);

        SearchSourceBuilder searchBuilder = findCaseBySingleField("data.caseReference", caseRef);
        List<SscsCaseDetails> sscsCaseDetailsList = findSubmittedCasesBySearchCriteria(searchBuilder.toString(), idamTokens);
        return null != sscsCaseDetailsList && !sscsCaseDetailsList.isEmpty() ? sscsCaseDetailsList.get(0) : null;
    }

    @Retryable
    public List<SscsCaseDetails> findListOfCasesByCaseRef(String caseRef, IdamTokens idamTokens) {
        log.info("searching list of cases by SC number {}", caseRef);

        SearchSourceBuilder searchBuilder = findCaseBySingleField("data.caseReference", caseRef);
        return findSubmittedCasesBySearchCriteria(searchBuilder.toString(), idamTokens);
    }

    @Retryable
    public List<SscsCaseDetails> findSubmittedCasesBySearchCriteria(String query, IdamTokens idamTokens) {
        log.info("findCaseBySearchCriteria {}", query);
        return findCaseBySearchCriteriaRetryLogic(
                query,
                idamTokens,
                AppealNumberGenerator::filterCaseNotDraftOrArchivedDraft
        );
    }

    @Retryable
    public List<SscsCaseDetails> findAllCasesBySearchCriteria(String query, IdamTokens idamTokens) {
        log.info("findCaseBySearchCriteria {}", query);
        return findCaseBySearchCriteriaRetryLogic(
                query,
                idamTokens,
                caseDetails -> true
        );
    }

    private List<SscsCaseDetails> findCaseBySearchCriteriaRetryLogic(String query, IdamTokens idamTokens,
                                                                     Predicate<SscsCaseDetails> caseFilter) {
        SearchResult caseDetailsList = ccdClient.searchCases(idamTokens, query);

        if (nonNull(caseDetailsList) && nonNull(caseDetailsList.getCases())) {
            return caseDetailsList.getCases().stream()
                    .map(sscsCcdConvertService::getCaseDetails)
                    .filter(caseFilter)
                    .collect(toList());
        }
        return null;
    }

    @Retryable
    public SscsCaseDetails findCaseByCaseRefOrCaseId(SscsCaseData caseData, IdamTokens idamTokens) {
        log.info("findCaseByCaseRefOrCaseId {}", caseData.getCcdCaseId());
        return findCaseByCaseRefOrCaseIdRetryLogic(caseData, idamTokens);
    }

    @Retryable
    public List<SscsCaseDetails> findListOfCasesByCaseRefOrCaseId(SscsCaseData caseData, IdamTokens idamTokens) {
        log.info("findListOfCasesByCaseRefOrCaseId {}", caseData.getCcdCaseId());
        return findListOfCasesByCaseRefOrCaseIdRetryLogic(caseData, idamTokens);
    }

    private SscsCaseDetails findCaseByCaseRefOrCaseIdRetryLogic(SscsCaseData caseData, IdamTokens idamTokens) {
        SscsCaseDetails sscsCaseDetails = null;
        if (StringUtils.isNotBlank(caseData.getCaseReference())) {
            sscsCaseDetails = this.findCaseByCaseRef(caseData.getCaseReference(), idamTokens);
            if (sscsCaseDetails != null){
                sscsCaseDetails = readCcdCaseService.getByCaseId(sscsCaseDetails.getId(), idamTokens);
            }
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
        // if found multiple cases against SC number then search for ccd case id
        if (!CollectionUtils.isEmpty(sscsCaseDetailsList) && sscsCaseDetailsList.size() > 1 && StringUtils.isNotBlank(caseData.getCcdCaseId())) {
            sscsCaseDetailsList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(sscsCaseDetailsList) && StringUtils.isNotBlank(caseData.getCcdCaseId())) {
            sscsCaseDetailsList.add(readCcdCaseService.getByCaseId(Long.parseLong(caseData.getCcdCaseId()), idamTokens));
        }
        return sscsCaseDetailsList;
    }

}

package uk.gov.hmcts.reform.sscs.ccd;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Service
public class CcdClient {
    private final CoreCaseDataApi coreCaseDataApi;
    private final IdamService idamService;

    public CcdClient(@Autowired CoreCaseDataApi coreCaseDataApi, @Autowired IdamService idamService) {
        this.coreCaseDataApi = coreCaseDataApi;
        this.idamService = idamService;
    }

    public List<SscsCaseDetails> findCaseBy(CcdRequestDetails ccdRequestDetails, Map<String, String> searchCriteria) {
        IdamTokens idamTokens = idamService.getIdamTokens();
        List<CaseDetails> caseDetailsList = coreCaseDataApi.searchForCaseworker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                new ImmutableMap.Builder<String, String>()
                        .putAll(searchCriteria)
                        .build()
        );

        return caseDetailsList.stream()
                .map(CcdUtil::getCaseDetails)
                .collect(toList());
    }
}

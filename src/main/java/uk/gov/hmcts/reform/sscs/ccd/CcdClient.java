package uk.gov.hmcts.reform.sscs.ccd;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Service
@Slf4j
public class CcdClient {
    private final CoreCaseDataApi coreCaseDataApi;
    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;

    public CcdClient(@Autowired CoreCaseDataApi coreCaseDataApi,
                     @Autowired IdamService idamService,
                     @Autowired SscsCcdConvertService sscsCcdConvertService) {
        this.coreCaseDataApi = coreCaseDataApi;
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
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
                .map(sscsCcdConvertService::getCaseDetails)
                .collect(toList());
    }

    @Retryable
    public SscsCaseDetails createCase(CcdRequestDetails ccdRequestDetails, SscsCaseData caseData) {
        BenefitType benefitType = caseData.getAppeal() != null ? caseData.getAppeal().getBenefitType() : null;
        log.info("Creating CCD case for Nino {} and benefit type {}", caseData.getGeneratedNino(), benefitType);
        IdamTokens idamTokens = idamService.getIdamTokens();

        StartEventResponse startEventResponse = start(ccdRequestDetails, idamTokens, "appealCreated");

        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "SSCS - appeal created event", "Created SSCS");
        CaseDetails caseDetails = coreCaseDataApi.submitForCaseworker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                true,
                caseDataContent
        );
        return sscsCcdConvertService.getCaseDetails(caseDetails);
    }

    private StartEventResponse start(CcdRequestDetails ccdRequestDetails, IdamTokens idamTokens, String eventId) {
        log.info("Starting CCD case for caseworker");
        return coreCaseDataApi.startForCaseworker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                eventId);
    }

}

package uk.gov.hmcts.reform.sscs.ccd.client;

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
import uk.gov.hmcts.reform.sscs.ccd.config.CcdRequestDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Service
@Slf4j
public class CcdClient {
    private final CcdRequestDetails ccdRequestDetails;
    private final CoreCaseDataApi coreCaseDataApi;
    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;

    @Autowired
    public CcdClient(CcdRequestDetails ccdRequestDetails,
                     CoreCaseDataApi coreCaseDataApi,
                     IdamService idamService,
                     SscsCcdConvertService sscsCcdConvertService) {
        this.ccdRequestDetails = ccdRequestDetails;
        this.coreCaseDataApi = coreCaseDataApi;
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
    }

    @Retryable
    public List<SscsCaseDetails> findCaseBy(Map<String, String> searchCriteria) {
        IdamTokens idamTokens = idamService.getIdamTokens();
        List<CaseDetails> caseDetailsList = searchForCaseworker(idamTokens, searchCriteria);

        return caseDetailsList.stream()
                .map(sscsCcdConvertService::getCaseDetails)
                .collect(toList());
    }

    @Retryable
    public CaseDetails getByCaseId(String caseId) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        log.info("Get getByCaseId " + caseId);
        return coreCaseDataApi.readForCaseWorker(
            idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(),
            idamTokens.getUserId(),
            ccdRequestDetails.getJurisdictionId(),
            ccdRequestDetails.getCaseTypeId(),
            caseId
        );
    }

    @Retryable
    public SscsCaseDetails createCase(SscsCaseData caseData, String description) {
        BenefitType benefitType = caseData.getAppeal() != null ? caseData.getAppeal().getBenefitType() : null;
        log.info("Creating CCD case for Nino {} and benefit type {}", caseData.getGeneratedNino(), benefitType);
        IdamTokens idamTokens = idamService.getIdamTokens();

        StartEventResponse startEventResponse = startCaseForCaseworker(idamTokens, "appealCreated");

        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "SSCS - appeal created event", description);
        CaseDetails caseDetails = submitForCaseworker(idamTokens, caseDataContent);

        return sscsCcdConvertService.getCaseDetails(caseDetails);
    }

    @Retryable
    public SscsCaseDetails updateCase(SscsCaseData caseData, Long caseId, String eventType, String summary, String description) {
        log.info("Updating CCD case id {} for caseworker", caseId);
        IdamTokens idamTokens = idamService.getIdamTokens();

        StartEventResponse startEventResponse = startEvent(idamTokens, caseId.toString(), eventType);
        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, summary, description);

        return sscsCcdConvertService.getCaseDetails(submitEventForCaseworker(idamTokens, caseId.toString(), caseDataContent));
    }

    private StartEventResponse startCaseForCaseworker(IdamTokens idamTokens, String eventId) {
        log.info("Starting CCD case for caseworker");
        return coreCaseDataApi.startForCaseworker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                eventId);
    }

    private CaseDetails submitForCaseworker(IdamTokens idamTokens, CaseDataContent caseDataContent) {
        return coreCaseDataApi.submitForCaseworker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                true,
                caseDataContent
        );
    }

    private StartEventResponse startEvent(IdamTokens idamTokens, String caseId, String eventType) {
        log.info("Starting CCD event for caseworker");

        return coreCaseDataApi.startEventForCaseWorker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                caseId,
                eventType);
    }

    private CaseDetails submitEventForCaseworker(IdamTokens idamTokens, String caseId, CaseDataContent caseDataContent) {

        return coreCaseDataApi.submitEventForCaseWorker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                caseId,
                true,
                caseDataContent);
    }

    private List<CaseDetails> searchForCaseworker(IdamTokens idamTokens, Map<String, String> searchCriteria) {
        return coreCaseDataApi.searchForCaseworker(
                idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(),
                idamTokens.getUserId(),
                ccdRequestDetails.getJurisdictionId(),
                ccdRequestDetails.getCaseTypeId(),
                new ImmutableMap.Builder<String, String>()
                        .putAll(searchCriteria)
                        .build());
    }
}

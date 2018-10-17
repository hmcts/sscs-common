package uk.gov.hmcts.reform.sscs.ccd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.exception.CreateCcdCaseException;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;


@Slf4j
@Service
public class CreateCcdCaseService {

    private final IdamService idamService;
    private final SscsCcdConvertService sscsCcdConvertService;
    private final CcdClient ccdClient;
    private final RetryTemplate retryTemplate;
    private final SearchCcdCaseService searchCcdCaseService;

    @Autowired
    public CreateCcdCaseService(IdamService idamService,
                      SscsCcdConvertService sscsCcdConvertService,
                      CcdClient ccdClient, RetryTemplate retryTemplate,
                                SearchCcdCaseService searchCcdCaseService) {
        this.idamService = idamService;
        this.sscsCcdConvertService = sscsCcdConvertService;
        this.ccdClient = ccdClient;
        this.retryTemplate = retryTemplate;
        this.searchCcdCaseService = searchCcdCaseService;
    }

    public SscsCaseDetails createCase(SscsCaseData caseData, IdamTokens idamTokens) {
        log.info("*** case-loader *** Starting create case process with SC number {} and ccdID {} ...",
                caseData.getCaseReference(), caseData.getCcdCaseId());
        try {
            return retryTemplate.execute(getRetryCallback(caseData, idamTokens),
                    getRecoveryCallback(caseData, idamTokens));
        } catch (Throwable throwable) {
            throw new CreateCcdCaseException(String.format(
                    "Recovery mechanism failed when creating case with SC %s and ccdID %s...",
                    caseData.getCaseReference(), caseData.getCcdCaseId()), throwable);
        }
    }

    private RecoveryCallback<SscsCaseDetails> getRecoveryCallback(SscsCaseData caseData, IdamTokens idamTokens) {
        return context -> {
            log.info("*** case-loader *** Recovery method called when creating case with SC number {} and ccdID {}...",
                    caseData.getCaseReference(), caseData.getCcdCaseId());
            requestNewIdamTokens(idamTokens);
            return createCaseIfDoesNotExist(caseData, idamTokens, context);
        };
    }

    private void requestNewIdamTokens(IdamTokens idamTokens) {
        idamTokens.setIdamOauth2Token(idamService.getIdamOauth2Token());
        idamTokens.setServiceAuthorization(idamService.generateServiceAuthorization());
    }

    private RetryCallback<SscsCaseDetails, ? extends Throwable> getRetryCallback(SscsCaseData caseData,
                                                                             IdamTokens idamTokens) {
        return (RetryCallback<SscsCaseDetails, Throwable>) context -> {
            log.info("*** case-loader *** create case with SC number {} and ccdID {} and retry number {}",
                    caseData.getCaseReference(), caseData.getCcdCaseId(), context.getRetryCount());
            return createCaseIfDoesNotExist(caseData, idamTokens, context);
        };
    }

    private SscsCaseDetails createCaseIfDoesNotExist(SscsCaseData caseData, IdamTokens idamTokens, RetryContext context) {
        SscsCaseDetails sscsCaseDetails = null;
        if (context.getRetryCount() > 0) {
            sscsCaseDetails = searchCcdCaseService.findCaseByCaseRefOrCaseId(caseData, idamTokens);
        }
        return (null == sscsCaseDetails ? createCaseInCcd(caseData, idamTokens) : sscsCaseDetails);
    }

    private SscsCaseDetails createCaseInCcd(SscsCaseData caseData, IdamTokens idamTokens) {
        BenefitType benefitType = caseData.getAppeal() != null ? caseData.getAppeal().getBenefitType() : null;
        log.info("Creating CCD case for Nino {} and benefit type {}", caseData.getGeneratedNino(), benefitType);

        StartEventResponse startEventResponse = ccdClient.startCaseForCaseworker(idamTokens, "appealCreated");

        CaseDataContent caseDataContent = sscsCcdConvertService.getCaseDataContent(caseData, startEventResponse, "SSCS - appeal created event", "Created SSCS");
        CaseDetails caseDetails = ccdClient.submitForCaseworker(idamTokens, caseDataContent);

        return sscsCcdConvertService.getCaseDetails(caseDetails);

    }
}

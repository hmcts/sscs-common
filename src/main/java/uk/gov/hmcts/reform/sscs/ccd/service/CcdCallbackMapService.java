package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Objects.nonNull;

import jakarta.annotation.Nonnull;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdCallbackMap;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;


@Slf4j
@Service
@RequiredArgsConstructor
public class CcdCallbackMapService {
    private final UpdateCcdCaseService updateCcdCaseService;
    private final IdamService idamService;


    public SscsCaseData handleCcdCallbackMapV2(@Nonnull CcdCallbackMap callbackMap, long caseId) {
        return handleCcdCallbackMapV2(callbackMap, caseId, sscsCaseData -> { });
    }

    public SscsCaseData handleCcdCallbackMapV2(@Nonnull CcdCallbackMap callbackMap, long caseId, @Nonnull Consumer<SscsCaseData> handlerMutator) {
        log.info("Triggering update case v2 for event type {} and case {}", callbackMap.getCallbackEvent().getCcdType(), caseId);
        SscsCaseDetails updatedCaseDetails = updateCcdCaseService.updateCaseV2(
                caseId,
                callbackMap.getCallbackEvent().getCcdType(),
                callbackMap.getCallbackSummary(),
                callbackMap.getCallbackDescription(),
                idamService.getIdamTokens(),
                sscsCaseDetails -> {
                    if (nonNull(callbackMap.getPostCallbackDwpState())) {
                        setDwpState(callbackMap, caseId, sscsCaseDetails.getData());
                    }

                    if (nonNull(callbackMap.getPostCallbackInterlocState())) {
                        setInterlocReviewState(callbackMap, sscsCaseDetails.getData(), caseId);

                    }

                    if (nonNull(callbackMap.getPostCallbackInterlocReason())) {
                        setInterlocReferralReason(callbackMap, sscsCaseDetails.getData(), caseId);
                    }
                    handlerMutator.accept(sscsCaseDetails.getData());
                }
        );
        return updatedCaseDetails.getData();
    }

    private static void setDwpState(CcdCallbackMap callbackMap, long caseId, SscsCaseData sscsCaseData) {
        log.info("Setting DwpState to {} for case {}", callbackMap.getPostCallbackDwpState(), caseId);
        sscsCaseData.setDwpState(callbackMap.getPostCallbackDwpState());
    }

    private static void setInterlocReferralReason(CcdCallbackMap callbackMap, SscsCaseData caseData, Long caseId) {
        log.info("Setting InterlocReferralReason to {} for case {}", callbackMap.getPostCallbackInterlocReason(), caseId);
        caseData.setInterlocReferralReason(callbackMap.getPostCallbackInterlocReason());
    }

    private static void setInterlocReviewState(CcdCallbackMap callbackMap, SscsCaseData caseData, Long caseId) {
        log.info("Setting InterlocReviewState to {} for case {}", callbackMap.getPostCallbackInterlocState(), caseId);
        caseData.setInterlocReviewState(callbackMap.getPostCallbackInterlocState());
    }

}

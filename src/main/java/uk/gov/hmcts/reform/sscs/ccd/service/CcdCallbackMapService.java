package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import javax.annotation.Nullable;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdCallbackMap;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CcdCallbackMapService {
    private final CcdService ccdService;
    private final UpdateCcdCaseService updateCcdCaseService;
    private final IdamService idamService;

    /**
     * Updates CCD with stale case data hence use handleCcdCallbackMapV2 which retrieves latest data from DB
     *
     * @deprecated use {@link #handleCcdCallbackMapV2(CcdCallbackMap, long)} instead.
     */
    @Deprecated
    public SscsCaseData handleCcdCallbackMap(@Nullable CcdCallbackMap callbackMap, @Valid SscsCaseData caseData) {
        if (isNull(callbackMap)) {
            return caseData;
        }

        Long caseId = Long.valueOf(caseData.getCcdCaseId());

        if (nonNull(callbackMap.getPostCallbackDwpState())) {
            setDwpState(callbackMap, caseId, caseData);
        }

        if (nonNull(callbackMap.getPostCallbackInterlocState())) {
            setInterlocReviewState(callbackMap, caseData, caseId);
        }

        if (nonNull(callbackMap.getPostCallbackInterlocReason())) {
            setInterlocReferralReason(callbackMap, caseData, caseId);
        }

        if (nonNull(callbackMap.getCallbackEvent())) {
            SscsCaseDetails updatedCaseDetails = ccdService.updateCase(caseData, caseId,
                callbackMap.getCallbackEvent().getCcdType(), callbackMap.getCallbackSummary(),
                callbackMap.getCallbackDescription(), idamService.getIdamTokens());
            return updatedCaseDetails.getData();
        }
        return caseData;
    }

    public Optional<SscsCaseData> handleCcdCallbackMapV2(@Nullable CcdCallbackMap callbackMap, long caseId) {

        if (isNull(callbackMap)) {
            return Optional.empty();
        }

        if (nonNull(callbackMap.getCallbackEvent())) {
            log.info("Triggering update case v2 for event type {} and case {}", callbackMap.getCallbackEvent().getCcdType(), caseId);
            SscsCaseDetails updatedCaseDetails = updateCcdCaseService.updateCaseV2(
                    caseId,
                    callbackMap.getCallbackEvent().getCcdType(),
                    callbackMap.getCallbackSummary(),
                    callbackMap.getCallbackDescription(),
                    idamService.getIdamTokens(),
                    sscsCaseData -> {
                        if (nonNull(callbackMap.getPostCallbackDwpState())) {
                            setDwpState(callbackMap, caseId, sscsCaseData);
                        }

                        if (nonNull(callbackMap.getPostCallbackInterlocState())) {
                            setInterlocReviewState(callbackMap, sscsCaseData, caseId);
                        }

                        if (nonNull(callbackMap.getPostCallbackInterlocReason())) {
                            setInterlocReferralReason(callbackMap, sscsCaseData, caseId);
                        }
                    }
            );

            return Optional.of(updatedCaseDetails.getData());
        }
        return Optional.empty();
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

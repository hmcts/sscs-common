package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import javax.annotation.Nullable;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdCallbackMap;
import uk.gov.hmcts.reform.sscs.ccd.domain.EventType;
import uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;


@Slf4j
@Service
@RequiredArgsConstructor
public class CcdCallbackMapService {
    private final CcdService ccdService;
    private final IdamService idamService;

    public SscsCaseData handleCcdCallbackMap(@Nullable CcdCallbackMap callbackMap, @Valid SscsCaseData caseData) {
        if (isNull(callbackMap)) {
            return caseData;
        }

        Long caseId = Long.valueOf(caseData.getCcdCaseId());

        if (nonNull(callbackMap.getPostCallbackDwpState())) {
            log.info("Setting DwpState to {} for case {}", callbackMap.getPostCallbackDwpState(), caseId);
            caseData.setDwpState(callbackMap.getPostCallbackDwpState());
        }

        if (nonNull(callbackMap.getPostCallbackInterlocState())) {
            log.info("Setting InterlocReviewState to {} for case {}", callbackMap.getPostCallbackInterlocState(), caseId);
            caseData.setInterlocReviewState(callbackMap.getPostCallbackInterlocState());
        }

        if (nonNull(callbackMap.getPostCallbackInterlocReason())) {
            log.info("Setting InterlocReferralReason to {} for case {}", callbackMap.getPostCallbackInterlocReason(), caseId);
            caseData.setInterlocReferralReason(callbackMap.getPostCallbackInterlocReason());
        }

        if (nonNull(callbackMap.getCallbackEvent())) {
            if (isRefuseSor(callbackMap.getPostCallbackInterlocState())) {
                caseData = ccdService.updateCase(caseData, caseId,
                    EventType.SOR_REQUEST.getCcdType(), "Send to hearing Judge for statement of reasons", "",
                    idamService.getIdamTokens()).getData();
            }

            SscsCaseDetails updatedCaseDetails = ccdService.updateCase(caseData, caseId,
                callbackMap.getCallbackEvent().getCcdType(), callbackMap.getCallbackSummary(),
                callbackMap.getCallbackDescription(), idamService.getIdamTokens());
            return updatedCaseDetails.getData();
        }
        return caseData;
    }

    private static boolean isRefuseSor(InterlocReviewState postCallbackInterlocState) {
        return nonNull(postCallbackInterlocState)
            && InterlocReviewState.REVIEW_BY_JUDGE.equals(postCallbackInterlocState);
    }

}

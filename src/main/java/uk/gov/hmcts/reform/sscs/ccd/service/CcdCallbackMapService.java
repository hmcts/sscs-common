package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdCallbackMap;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentGeneration;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentStaging;
import uk.gov.hmcts.reform.sscs.ccd.domain.PostHearing;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;

import java.util.Optional;
import java.util.function.Consumer;


@Slf4j
@Service
@RequiredArgsConstructor
public class CcdCallbackMapService {
    private final CcdService ccdService;
    private final UpdateCcdCaseService updateCcdCaseService;
    private final IdamService idamService;

    public SscsCaseData handleCcdCallbackMap(@Nullable CcdCallbackMap callbackMap, @Valid SscsCaseData caseData) {
        if (isNull(callbackMap)) {
            return caseData;
        }

        String caseId = caseData.getCcdCaseId();

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
            SscsCaseDetails updatedCaseDetails = ccdService.updateCase(caseData, Long.valueOf(caseId),
                callbackMap.getCallbackEvent().getCcdType(), callbackMap.getCallbackSummary(),
                callbackMap.getCallbackDescription(), idamService.getIdamTokens());
            return updatedCaseDetails.getData();
        }
        return caseData;
    }

    public Optional<SscsCaseData> handleCcdCallbackMapV2(@Nullable CcdCallbackMap callbackMap, @Nonnull Consumer<SscsCaseData> mutator, long caseId) {

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
                    mutator
            );

            return Optional.of(updatedCaseDetails.getData());
        }
        return Optional.empty();
    }

    public Consumer<SscsCaseData> getCcdCallbackMutator(CcdCallbackMap callbackMap, String caseId, boolean isPostHearingsEnabled) {
        return caseData -> {
            if (nonNull(callbackMap.getPostCallbackDwpState())) {
                setDwpState(callbackMap, caseId, caseData);
            }

            if (nonNull(callbackMap.getPostCallbackInterlocState())) {
                setInterlocReviewState(callbackMap, caseData, caseId);
            }

            if (nonNull(callbackMap.getPostCallbackInterlocReason())) {
                setInterlocReferralReason(callbackMap, caseData, caseId);
            }

            if ((callbackMap.getClearPostHearingFields())) {
                doClearPostHearingFields(caseData, caseId, isPostHearingsEnabled);
            }
        };
    }

    void setDwpState(CcdCallbackMap callbackMap, String caseId, SscsCaseData sscsCaseData) {
        log.info("Setting DwpState to {} for case {}", callbackMap.getPostCallbackDwpState(), caseId);
        sscsCaseData.setDwpState(callbackMap.getPostCallbackDwpState());
    }

    void setInterlocReferralReason(CcdCallbackMap callbackMap, SscsCaseData caseData, String caseId) {
        log.info("Setting InterlocReferralReason to {} for case {}", callbackMap.getPostCallbackInterlocReason(), caseId);
        caseData.setInterlocReferralReason(callbackMap.getPostCallbackInterlocReason());
    }

    void setInterlocReviewState(CcdCallbackMap callbackMap, SscsCaseData caseData, String caseId) {
        log.info("Setting InterlocReviewState to {} for case {}", callbackMap.getPostCallbackInterlocState(), caseId);
        caseData.setInterlocReviewState(callbackMap.getPostCallbackInterlocState());
    }

    void doClearPostHearingFields(SscsCaseData caseData, String caseId, boolean isPostHearingsEnabled) {
        log.info("Setting doClearPostHearingFields for case {}", caseId);
        if (isPostHearingsEnabled) {
            caseData.setPostHearing(PostHearing.builder().build());
        }
        caseData.setDocumentGeneration(DocumentGeneration.builder().build());
        caseData.setDocumentStaging(DocumentStaging.builder().build());
    }

}

package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.DIRECTION_ACTION_REQUIRED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.READY_TO_LIST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.ADVICE_ON_HOW_TO_PROCEED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.AWAITING_ADMIN_ACTION;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdCallbackMap;
import uk.gov.hmcts.reform.sscs.ccd.domain.Correction;
import uk.gov.hmcts.reform.sscs.ccd.domain.CorrectionActions;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentGeneration;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentStaging;
import uk.gov.hmcts.reform.sscs.ccd.domain.DwpState;
import uk.gov.hmcts.reform.sscs.ccd.domain.PostHearing;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
class CcdCallbackMapServiceTest {
    private static final long CASE_ID = 1234;
    @Mock
    private CcdService ccdService;
    @Mock
    private UpdateCcdCaseService updateCcdCaseService;
    @Mock
    private IdamService idamService;
    @Captor
    private ArgumentCaptor<Consumer<SscsCaseData>>  sscsCaseDataArgumentCaptor;

    @InjectMocks
    private CcdCallbackMapService ccdCallbackMapService;

    @Mock
    private CcdCallbackMap callbackMap;
    private SscsCaseData caseData;
    private IdamTokens idamTokens;

    @BeforeEach
    public void setUp() {
        caseData = SscsCaseData.builder()
            .ccdCaseId(String.valueOf(CASE_ID))
            .build();
        idamTokens = IdamTokens.builder().build();
    }

    @DisplayName("When callbackMap is null handleCcdCallbackMap returns the case data unmodified and doesn't call "
        + "ccdService")
    @Test
    void handleCcdCallbackNullCallbackMap() {

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMap(null, caseData);

        assertThat(result).isEqualTo(caseData);
        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When callbackMap is null handleCcdCallbackMapV2 returns empty optional and doesn't call "
            + "ccdService")
    @Test
    void handleCcdCallbackNullCallbackMapV2() {

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(null, null, CASE_ID);

        assertThat(result).isEmpty();
        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When PostCallbackDwpState is null handleCcdCallbackMap leaves the Dwp State unmodified")
    @Test
    void handleCcdCallbackNullPostCallbackDwpState() {
        given(callbackMap.getPostCallbackDwpState()).willReturn(null);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMap(callbackMap, caseData);

        assertThat(result).isEqualTo(caseData);
    }

    @DisplayName("When PostCallbackDwpState is not null handleCcdCallbackMap correctly sets the Dwp State")
    @Test
    void handleCcdCallbackPostCallbackDwpState() {
        DwpState expected = DIRECTION_ACTION_REQUIRED;
        given(callbackMap.getPostCallbackDwpState()).willReturn(expected);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMap(callbackMap, caseData);

        assertThat(result.getDwpState()).isEqualTo(expected);
    }

    @DisplayName("When PostCallbackDwpState is not null handleCcdCallbackMapV2 correctly sets the Dwp State")
    @Test
    void handleCcdCallbackV2PostCallbackDwpState() {
        given(callbackMap.getPostCallbackDwpState()).willReturn(DIRECTION_ACTION_REQUIRED);
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotEmpty();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDataArgumentCaptor.capture());

        sscsCaseDataArgumentCaptor.getValue().accept(caseData);

        assertThat(result.get().getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
    }

    @DisplayName("When CallbackEvent is null handleCcdCallbackMap doesn't call ccdService")
    @Test
    void handleCcdCallbackNullCallbackEvent() {
        given(callbackMap.getCallbackEvent()).willReturn(null);

        ccdCallbackMapService.handleCcdCallbackMap(callbackMap, caseData);

        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When CallbackEvent is null handleCcdCallbackMapV2 doesn't call ccdService")
    @Test
    void handleCcdCallbackV2NullCallbackEvent() {
        given(callbackMap.getCallbackEvent()).willReturn(null);

        ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, null, CASE_ID);

        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When CallbackEvent is not null handleCcdCallbackMap will call ccdService")
    @Test
    void handleCcdCallbackCallbackEvent() {
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        SscsCaseData expected = SscsCaseData.builder()
            .caseReference("changed")
            .build();
        given(ccdService
            .updateCase(caseData, CASE_ID, READY_TO_LIST.getCcdType(), "summary", "description", idamTokens))
            .willReturn(SscsCaseDetails.builder().data(expected).build());

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMap(callbackMap, caseData);

        verify(ccdService, times(1))
            .updateCase(caseData, CASE_ID, READY_TO_LIST.getCcdType(), "summary", "description", idamTokens);
        verify(idamService, times(1)).getIdamTokens();

        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("When handleCcdCallbackMapV2 gets called, caseUPdateV2 called with the correct params")
    @Test
    void handleCcdCallbackV2WithNoCaseMutation() {
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), isNull()))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, null, CASE_ID);
        assertThat(result).isNotEmpty();

        verify(updateCcdCaseService, times(1))
    }

    @DisplayName("When PostHearing enabled get the standard CcdCallbackMutator mutator")
    @Test
    void handleCcdCallbackV2WithCaseMutationForPostHearingEnabled() {
        PostHearing postHearing = PostHearing.builder().correction(Correction.builder().action(CorrectionActions.GRANT).build()).build();
        caseData.setPostHearing(postHearing);
        caseData.setDocumentGeneration(DocumentGeneration.builder().bodyContent("AAAA").build());
        caseData.setDocumentStaging(DocumentStaging.builder().dateAdded(LocalDate.now()).build());

        given(callbackMap.getPostCallbackDwpState()).willReturn(DIRECTION_ACTION_REQUIRED);
        given(callbackMap.getClearPostHearingFields()).willReturn(true);
        given(callbackMap.getPostCallbackInterlocState()).willReturn(AWAITING_ADMIN_ACTION);
        given(callbackMap.getPostCallbackInterlocReason()).willReturn(ADVICE_ON_HOW_TO_PROCEED);


        Consumer<SscsCaseData> mutator  = ccdCallbackMapService.getCcdCallbackMutator(callbackMap, String.valueOf(CASE_ID), true);
        mutator.accept(caseData);

        assertThat(caseData.getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
        assertThat(caseData.getInterlocReviewState()).isEqualTo(AWAITING_ADMIN_ACTION);
        assertThat(caseData.getInterlocReferralReason()).isEqualTo(ADVICE_ON_HOW_TO_PROCEED);
        assertThat(caseData.getPostHearing().getCorrection().getAction()).isNotEqualTo(postHearing.getCorrection().getAction());
        assertThat(caseData.getDocumentGeneration().getBodyContent()).isNull();
        assertThat(caseData.getDocumentStaging().getDateAdded()).isNull();
    }

    @DisplayName("When PostHearing disabled get the standard CcdCallbackMutator mutator")
    @Test
    void handleCcdCallbackV2WithCaseMutationForPostHearingDisabled() {
        PostHearing postHearing = PostHearing.builder().correction(Correction.builder().action(CorrectionActions.GRANT).build()).build();
        caseData.setPostHearing(postHearing);
        caseData.setDocumentGeneration(DocumentGeneration.builder().bodyContent("AAAA").build());
        caseData.setDocumentStaging(DocumentStaging.builder().dateAdded(LocalDate.now()).build());

        given(callbackMap.getPostCallbackDwpState()).willReturn(DIRECTION_ACTION_REQUIRED);
        given(callbackMap.getClearPostHearingFields()).willReturn(true);
        given(callbackMap.getPostCallbackInterlocState()).willReturn(AWAITING_ADMIN_ACTION);
        given(callbackMap.getPostCallbackInterlocReason()).willReturn(ADVICE_ON_HOW_TO_PROCEED);


        Consumer<SscsCaseData> mutator  = ccdCallbackMapService.getCcdCallbackMutator(callbackMap, String.valueOf(CASE_ID), false);
        mutator.accept(caseData);

        assertThat(caseData.getDocumentGeneration().getBodyContent()).isNull();
        assertThat(caseData.getDocumentStaging().getDateAdded()).isNull();
        assertThat(caseData.getPostHearing().getCorrection().getAction()).isEqualTo(postHearing.getCorrection().getAction());
        assertThat(caseData.getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
        assertThat(caseData.getInterlocReviewState()).isEqualTo(AWAITING_ADMIN_ACTION);
        assertThat(caseData.getInterlocReferralReason()).isEqualTo(ADVICE_ON_HOW_TO_PROCEED);
    }

}

package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.DIRECTION_ACTION_REQUIRED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.RESPONSE_SUBMITTED_DWP;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.READY_TO_LIST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReferralReason.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.REVIEW_BY_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.InterlocReviewState.REVIEW_BY_TCW;

import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;


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
    private ArgumentCaptor<Consumer<SscsCaseDetails>>  sscsCaseDetailsArgumentCaptor;

    @InjectMocks
    private CcdCallbackMapService ccdCallbackMapService;

    @Mock
    private CcdCallbackMap callbackMap;
    private SscsCaseData caseData;
    private IdamTokens idamTokens;

    @Captor
    private ArgumentCaptor<CaseDataContent> caseDataContentArgumentCaptor;
    @Mock
    private SscsCcdConvertService sscsCcdConvertService;
    @Mock
    private CcdClient ccdClient;
    @Mock
    private ReadCcdCaseService readCcdCaseService;

    @BeforeEach
    public void setUp() {
        caseData = SscsCaseData.builder()
            .ccdCaseId(String.valueOf(CASE_ID))
            .build();
        idamTokens = IdamTokens.builder().build();
    }

    @DisplayName("When callbackMap is null, a null pointer exception is thrown")
    @Test
    void handleCcdCallbackNullCallbackMapV2() {
        assertThatNullPointerException().isThrownBy(() -> ccdCallbackMapService.handleCcdCallbackMapV2(null, CASE_ID));
        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When callbackMap is null, a null pointer exception is thrown")
    @Test
    void handleCcdCallbackNullCallbackMapV2WithHandlerConsumer() {
        assertThatNullPointerException().isThrownBy(() -> ccdCallbackMapService.handleCcdCallbackMapV2(null, CASE_ID, caseData -> {
        }));
        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When PostCallbackDwpState is null handleCcdCallbackMapV2 leaves the Dwp State unmodified")
    @Test
    void handleCcdCallbackNullPostCallbackDwpState() {
        caseData.setDwpState(DIRECTION_ACTION_REQUIRED);
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(callbackMap.getPostCallbackDwpState()).willReturn(null);
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(sscsCaseDetails);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);

        assertThat(result.getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
    }

    @DisplayName("When PostCallbackInterlocState is null handleCcdCallbackMapV2 leaves the InterlocReviewState unmodified")
    @Test
    void handleCcdCallbackNullPostCallbackInterlocState() {
        caseData.setInterlocReviewState(REVIEW_BY_JUDGE);
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(callbackMap.getPostCallbackInterlocState()).willReturn(null);
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(sscsCaseDetails);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);

        assertThat(result.getInterlocReviewState()).isEqualTo(REVIEW_BY_JUDGE);
    }

    @DisplayName("When PostCallbackInterlocState is not null handleCcdCallbackMapV2 correctly sets InterlocReviewState")
    @Test
    void handleCcdCallbackNotNullPostCallbackInterlocState() {
        caseData.setInterlocReviewState(REVIEW_BY_JUDGE);
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(callbackMap.getPostCallbackInterlocState()).willReturn(REVIEW_BY_TCW);
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(sscsCaseDetails);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);

        assertThat(result.getInterlocReviewState()).isEqualTo(REVIEW_BY_TCW);
    }

    @DisplayName("When PostCallbackInterlocReason is not null handleCcdCallbackMapV2 correctly sets InterlocReferralReason")
    @Test
    void handleCcdCallbackNotNullPostCallbackInterlocReason() {
        caseData.setInterlocReferralReason(ADVICE_ON_HOW_TO_PROCEED);
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(callbackMap.getPostCallbackInterlocReason()).willReturn(TIME_EXTENSION);
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(sscsCaseDetails);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);

        assertThat(result.getInterlocReferralReason()).isEqualTo(TIME_EXTENSION);
    }

    @DisplayName("When PostCallbackDwpState is not null handleCcdCallbackMapV2 correctly sets the Dwp State")
    @Test
    void handleCcdCallbackV2PostCallbackDwpState() {
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(callbackMap.getPostCallbackDwpState()).willReturn(DIRECTION_ACTION_REQUIRED);
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(sscsCaseDetails);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);

        assertThat(result.getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
    }

    @DisplayName("When PostCallbackDwpState is not null handleCcdCallbackMapV2 correctly sets the Dwp State")
    @Test
    void handleCcdCallbackV2PostCallbackDwpStateWithHandlerConsumer() {
        given(callbackMap.getPostCallbackDwpState()).willReturn(DIRECTION_ACTION_REQUIRED);
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(sscsCaseDetails);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID, caseData -> {
        });

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);

        assertThat(result.getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
    }

    @DisplayName("When CallbackEvent is null, a null pointer exception is thrown")
    @Test
    void handleCcdCallbackV2NullCallbackEvent() {
        given(callbackMap.getCallbackEvent()).willReturn(null);

        assertThatNullPointerException().isThrownBy(() -> ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID));

        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When CallbackEvent is null, a null pointer exception is thrown")
    @Test
    void handleCcdCallbackV2NullCallbackEventWithHandlerConsumer() {
        given(callbackMap.getCallbackEvent()).willReturn(null);

        assertThatNullPointerException().isThrownBy(() -> ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID, caseData -> {
        }));

        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When CallbackEvent is not null handleCcdCallbackMapV2 will call ccdService")
    @Test
    void handleCcdCallbackEventV2() {
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        SscsCaseData expected = SscsCaseData.builder().ccdCaseId(String.valueOf(CASE_ID)).build();

        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());
        verify(idamService, times(1)).getIdamTokens();

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);
        assertThat(caseData).usingRecursiveComparison().ignoringFields("jointParty.id").isEqualTo(expected);
    }

    @DisplayName("When CallbackEvent is not null handleCcdCallbackMapV2 will call ccdService")
    @Test
    void handleCcdCallbackEventV2WithHandlerConsumer() {
        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        SscsCaseData expected = SscsCaseData.builder().ccdCaseId(String.valueOf(CASE_ID)).build();

        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(sscsCaseDetails);

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID, caseData -> {
        });

        assertThat(result).isNotNull();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDetailsArgumentCaptor.capture());
        verify(idamService, times(1)).getIdamTokens();

        sscsCaseDetailsArgumentCaptor.getValue().accept(sscsCaseDetails);
        assertThat(caseData).usingRecursiveComparison().ignoringFields("jointParty.id").isEqualTo(expected);
    }

    @DisplayName("When CallbackEvent is not null handleCcdCallbackMapV2 will call ccdService" +
            " updating case data with handler mutator")
    @Test
    void handleCcdCallbackEventV2WithHandlerConsumerUpdates() {
        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails =
                mock(uk.gov.hmcts.reform.ccd.client.model.CaseDetails.class);

        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        StartEventResponse startEventResponse = StartEventResponse.builder().caseDetails(caseDetails).build();

        given(ccdClient.startEvent(any(), anyLong(), anyString()))
                .willReturn(startEventResponse);

        given(sscsCcdConvertService.getCaseDataContent(
                eq(sscsCaseDetails.getData()),
                any(),
                anyString(),
                anyString()))
                .willReturn(CaseDataContent.builder()
                        .data(sscsCaseDetails.getData())
                        .build());

        UpdateCcdCaseService updateCcdCaseService = new UpdateCcdCaseService(
                idamService,
                sscsCcdConvertService,
                ccdClient,
                readCcdCaseService
        );

        CcdCallbackMapService ccdCallbackMapService = new CcdCallbackMapService(updateCcdCaseService, idamService);

        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        given(ccdClient.submitEventForCaseworker(any(), anyLong(), any()))
                .willReturn(CaseDetails.builder().build());

        SscsCaseData expected = SscsCaseData.builder()
                .ccdCaseId(String.valueOf(CASE_ID))
                .state(State.READY_TO_LIST)
                .dwpState(RESPONSE_SUBMITTED_DWP)
                .build();


        given(sscsCcdConvertService.getCaseDetails(CaseDetails.builder().build()))
                .willReturn(sscsCaseDetails);

        given(sscsCcdConvertService.getCaseDetails(any(StartEventResponse.class)))
                .willReturn(sscsCaseDetails);


        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMapV2(
                callbackMap,
                CASE_ID,
                caseData -> {
                    caseData.setState(State.READY_TO_LIST);
                    caseData.setDwpState(RESPONSE_SUBMITTED_DWP);
                });

        assertThat(result).isNotNull();
        verify(sscsCcdConvertService).getCaseDataContent(
                eq(sscsCaseDetails.getData()),
                any(),
                anyString(),
                anyString());

        verify(ccdClient).submitEventForCaseworker(any(), anyLong(), caseDataContentArgumentCaptor.capture());
        SscsCaseData updatedSscsCaseData = (SscsCaseData) caseDataContentArgumentCaptor.getValue().getData();
        assertThat(updatedSscsCaseData.getDwpState()).isEqualTo(RESPONSE_SUBMITTED_DWP);
        assertThat(updatedSscsCaseData.getState()).isEqualTo(State.READY_TO_LIST);

        verify(idamService).getIdamTokens();
        assertThat(caseData).usingRecursiveComparison().ignoringFields("jointParty.id").isEqualTo(expected);
    }

    @DisplayName("When case handler mutator is null, a null pointer exception is thrown")
    @Test
    void handleCcdCallbackEventV2WithHandlerConsumerUpdatesIsNull() {
        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails =
                mock(uk.gov.hmcts.reform.ccd.client.model.CaseDetails.class);

        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder().id(CASE_ID).data(caseData).build();
        StartEventResponse startEventResponse = StartEventResponse.builder().caseDetails(caseDetails).build();

        given(ccdClient.startEvent(any(), anyLong(), anyString()))
                .willReturn(startEventResponse);

        UpdateCcdCaseService updateCcdCaseService = new UpdateCcdCaseService(
                idamService,
                sscsCcdConvertService,
                ccdClient,
                readCcdCaseService
        );

        CcdCallbackMapService ccdCallbackMapService = new CcdCallbackMapService(updateCcdCaseService, idamService);

        given(callbackMap.getCallbackEvent()).willReturn(READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        assertThatNullPointerException().isThrownBy(() -> ccdCallbackMapService.handleCcdCallbackMapV2(
                callbackMap,
                CASE_ID,
               null));


        verify(sscsCcdConvertService, never()).getCaseDataContent(
                eq(sscsCaseDetails.getData()),
                any(),
                anyString(),
                anyString());

        verify(ccdClient, never()).submitEventForCaseworker(any(), anyLong(), caseDataContentArgumentCaptor.capture());
        verify(idamService).getIdamTokens();
    }
}

package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.DIRECTION_ACTION_REQUIRED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.RESPONSE_SUBMITTED_DWP;

import java.util.Optional;
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
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdCallbackMap;
import uk.gov.hmcts.reform.sscs.ccd.domain.DwpState;
import uk.gov.hmcts.reform.sscs.ccd.domain.EventType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;
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
    private ArgumentCaptor<Consumer<SscsCaseData>> sscsCaseDataArgumentCaptor;

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

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(null, CASE_ID);

        assertThat(result).isEmpty();
        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When callbackMap is null handleCcdCallbackMapV2 returns empty optional and doesn't call "
            + "ccdService")
    @Test
    void handleCcdCallbackNullCallbackMapV2WithHandlerConsumer() {

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(null, CASE_ID, sscsCaseData -> {
        });

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
        given(callbackMap.getCallbackEvent()).willReturn(EventType.READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotEmpty();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDataArgumentCaptor.capture());

        sscsCaseDataArgumentCaptor.getValue().accept(caseData);

        assertThat(result.get().getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
    }

    @DisplayName("When PostCallbackDwpState is not null handleCcdCallbackMapV2 correctly sets the Dwp State")
    @Test
    void handleCcdCallbackV2PostCallbackDwpStateWithHandlerConsumer() {
        given(callbackMap.getPostCallbackDwpState()).willReturn(DIRECTION_ACTION_REQUIRED);
        given(callbackMap.getCallbackEvent()).willReturn(EventType.READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID, sscsCaseData -> {
        });

        assertThat(result).isNotEmpty();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDataArgumentCaptor.capture());

        sscsCaseDataArgumentCaptor.getValue().accept(caseData);

        assertThat(result.get().getDwpState()).isEqualTo(DIRECTION_ACTION_REQUIRED);
    }

    @DisplayName("When PostCallbackDwpState is not null handleCcdCallbackMapV2 correctly sets the Dwp State")
    @Test
    void handleCcdCallbackV2PostCallbackDwpStateWithNullHandlerConsumer() {
        given(callbackMap.getPostCallbackDwpState()).willReturn(DIRECTION_ACTION_REQUIRED);
        given(callbackMap.getCallbackEvent()).willReturn(EventType.READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");
        given(idamService.getIdamTokens()).willReturn(idamTokens);
        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID, null);

        assertThat(result).isNotEmpty();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDataArgumentCaptor.capture());

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

        ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When CallbackEvent is null handleCcdCallbackMapV2 doesn't call ccdService")
    @Test
    void handleCcdCallbackV2NullCallbackEventWithHandlerConsumer() {
        given(callbackMap.getCallbackEvent()).willReturn(null);

        ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID, sscsCaseData -> {
        });

        verifyNoInteractions(ccdService, idamService);
    }

    @DisplayName("When CallbackEvent is not null handleCcdCallbackMap will call ccdService")
    @Test
    void handleCcdCallbackCallbackEvent() {
        given(callbackMap.getCallbackEvent()).willReturn(EventType.READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        SscsCaseData expected = SscsCaseData.builder()
                .caseReference("changed")
                .build();
        given(ccdService
                .updateCase(caseData, CASE_ID, EventType.READY_TO_LIST.getCcdType(), "summary", "description", idamTokens))
                .willReturn(SscsCaseDetails.builder().data(expected).build());

        SscsCaseData result = ccdCallbackMapService.handleCcdCallbackMap(callbackMap, caseData);

        verify(ccdService, times(1))
                .updateCase(caseData, CASE_ID, EventType.READY_TO_LIST.getCcdType(), "summary", "description", idamTokens);
        verify(idamService, times(1)).getIdamTokens();

        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("When CallbackEvent is not null handleCcdCallbackMapV2 will call ccdService")
    @Test
    void handleCcdCallbackEventV2() {
        given(callbackMap.getCallbackEvent()).willReturn(EventType.READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        SscsCaseData expected = SscsCaseData.builder().ccdCaseId(String.valueOf(CASE_ID)).build();

        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID);

        assertThat(result).isNotEmpty();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDataArgumentCaptor.capture());
        verify(idamService, times(1)).getIdamTokens();

        sscsCaseDataArgumentCaptor.getValue().accept(caseData);
        assertThat(caseData).usingRecursiveComparison().ignoringFields("jointParty.id").isEqualTo(expected);
    }

    @DisplayName("When CallbackEvent is not null handleCcdCallbackMapV2 will call ccdService")
    @Test
    void handleCcdCallbackEventV2WithHandlerConsumer() {
        given(callbackMap.getCallbackEvent()).willReturn(EventType.READY_TO_LIST);
        given(callbackMap.getCallbackSummary()).willReturn("summary");
        given(callbackMap.getCallbackDescription()).willReturn("description");

        given(idamService.getIdamTokens()).willReturn(idamTokens);

        SscsCaseData expected = SscsCaseData.builder().ccdCaseId(String.valueOf(CASE_ID)).build();

        given(updateCcdCaseService
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), any(Consumer.class)))
                .willReturn(SscsCaseDetails.builder().id(CASE_ID).data(caseData).build());

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(callbackMap, CASE_ID, sscsCaseData -> {
        });

        assertThat(result).isNotEmpty();

        verify(updateCcdCaseService, times(1))
                .updateCaseV2(eq(CASE_ID), eq(EventType.READY_TO_LIST.getCcdType()), eq("summary"), eq("description"), eq(idamTokens), sscsCaseDataArgumentCaptor.capture());
        verify(idamService, times(1)).getIdamTokens();

        sscsCaseDataArgumentCaptor.getValue().accept(caseData);
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

        given(sscsCcdConvertService.getCaseData(anyMap()))
                .willReturn(sscsCaseDetails.getData());

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
                ccdClient
        );

        CcdCallbackMapService ccdCallbackMapService = new CcdCallbackMapService(ccdService, updateCcdCaseService, idamService);

        given(callbackMap.getCallbackEvent()).willReturn(EventType.READY_TO_LIST);
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

        Optional<SscsCaseData> result = ccdCallbackMapService.handleCcdCallbackMapV2(
                callbackMap,
                CASE_ID,
                sscsCaseData -> {
                    sscsCaseData.setState(State.READY_TO_LIST);
                    sscsCaseData.setDwpState(RESPONSE_SUBMITTED_DWP);
                });

        assertThat(result).isNotEmpty();
        verify(sscsCcdConvertService).getCaseDataContent(
                eq(sscsCaseDetails.getData()),
                any(),
                anyString(),
                anyString());

        verify(ccdClient).submitEventForCaseworker(any(), anyLong(), caseDataContentArgumentCaptor.capture());
        SscsCaseData sscsCaseData = (SscsCaseData) caseDataContentArgumentCaptor.getValue().getData();
        assertThat(sscsCaseData.getDwpState()).isEqualTo(RESPONSE_SUBMITTED_DWP);
        assertThat(sscsCaseData.getState()).isEqualTo(State.READY_TO_LIST);

        verify(idamService).getIdamTokens();
        assertThat(caseData).usingRecursiveComparison().ignoringFields("jointParty.id").isEqualTo(expected);
    }
}

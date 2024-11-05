package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.POST_HEARING_REQUEST;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.client.CcdClient;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@ExtendWith(MockitoExtension.class)
public class UpdateCcdCaseServiceTest {

    @Mock
    private IdamService idamService;
    @Mock
    private SscsCcdConvertService sscsCcdConvertService;
    @Mock
    private CcdClient ccdClient;

    @Captor
    private ArgumentCaptor<SscsCaseData>  sscsCaseDataArgumentCaptor;

    @InjectMocks
    private UpdateCcdCaseService updateCcdCaseService;

    @Test
    void handleUpdateCaseV2() {
        long caseId = 1234L;
        String benefitCode = "200DD";
        UnaryOperator<SscsCaseDetails> unaryOperator = sscsCaseDetails -> sscsCaseDetails.toBuilder()
                .data(SscsCaseData.builder()
                        .addDocuments(YES)
                        .benefitCode(benefitCode)
                        .build())
                .build();

        when(ccdClient.startEvent(isA(IdamTokens.class), anyLong(), eq(POST_HEARING_REQUEST.getType())))
                .thenReturn(StartEventResponse.builder().build());
        when(sscsCcdConvertService.getCaseDetails(isA(StartEventResponse.class)))
                        .thenReturn(SscsCaseDetails.builder()
                                .data(SscsCaseData.builder()
                                        .addDocuments(NO)
                                        .build())
                                .build());


        String postHearingSummary = "Post hearing Summary";
        String postHearingDescription = "Post hearing Description";

        updateCcdCaseService.updateCaseV2WithUnaryFunction(
                caseId,
                POST_HEARING_REQUEST.getType(),
                postHearingSummary,
                postHearingDescription,
                IdamTokens.builder().build(),
                unaryOperator);

        verify(sscsCcdConvertService).getCaseDataContent(
                sscsCaseDataArgumentCaptor.capture(),
                any(),
                eq(postHearingSummary),
                eq(postHearingDescription));

        SscsCaseData sscsCaseData = sscsCaseDataArgumentCaptor.getValue();
        assertThat(sscsCaseData.getAddDocuments())
                .isEqualTo(YES);
        assertThat(sscsCaseData.getBenefitCode())
                .isEqualTo(benefitCode);
    }
}

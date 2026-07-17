package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FtaCommunicationFields {

    @CCD(
            label = "Requests from Tribunal",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "ftaCommunicationRequest",
            access = {SscsCudAccess.class}
    )
    private List<CommunicationRequest> ftaCommunications;
    @CCD(label = "Communication type", access = {SscsCrudAccess.class})
    private FtaRequestType ftaRequestType;
    @CCD(label = "FTA Communications", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList ftaRequestsDl;
    @CCD(label = "FTA Request Query", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String ftaRequestNoResponseQuery;

    @CCD(
            label = "Requests from FTA",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "ftaCommunicationRequest",
            access = {SscsCudAccess.class}
    )
    private List<CommunicationRequest> tribunalCommunications;
    @CCD(label = "Communication type", access = {SscsCrudAccess.class})
    private TribunalRequestType tribunalRequestType;
    @CCD(label = "Tribunal Communications", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList tribunalRequestsDl;
    @CCD(label = "Tribunal Request Query", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String tribunalRequestNoResponseQuery;

    @CCD(
            label = "Request Topic",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_commRequestTopic",
            access = {SscsCrudAccess.class}
    )
    private CommunicationRequestTopic commRequestTopic;
    @CCD(label = "Request", max = 500, typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String commRequestQuestion;
    @CCD(label = "Reply", max = 500, typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String commRequestResponseTextArea;
    @CCD(
            label = " ",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_replyNoActionRequired",
            access = {SscsCrudAccess.class}
    )
    private List<String> commRequestResponseNoAction;

    @CCD(label = "Communication Requests", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList deleteCommRequestRadioDl;
    @CCD(label = "Communication Request", access = {SscsCrudAccess.class})
    private CommunicationRequestDetails deleteCommRequestReadOnly;
    @CCD(label = "Reason for deleting", max = 500, typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String deleteCommRequestTextArea;
    @CCD(label = " ", access = {SscsCrudAccess.class})
    private CommunicationRequestDetails deleteCommRequestReadOnlyStored;
    @CCD(label = " ", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String deleteCommRequestTextAreaStored;

    @CCD(
            label = "Tribunal Communications",
            typeOverride = FieldType.DynamicMultiSelectList,
            access = {SscsCrudAccess.class}
    )
    private DynamicMixedChoiceList tribunalRequestsToReviewDl;
    @CCD(label = "FTA Communications", typeOverride = FieldType.DynamicMultiSelectList, access = {SscsCrudAccess.class})
    private DynamicMixedChoiceList ftaRequestsToReviewDl;

    @CCD(label = "Tribunal need to reply by", access = {SscsCrudAccess.class})
    private LocalDate tribunalResponseDueDate;
    @CCD(label = "FTA need to reply by", access = {SscsCrudAccess.class})
    private LocalDate ftaResponseDueDate;
    @CCD(label = "Date the Tribunal responded", access = {SscsCrudAccess.class})
    private LocalDate tribunalResponseProvidedDate;
    @CCD(label = "Date the FTA responded", access = {SscsCrudAccess.class})
    private LocalDate ftaResponseProvidedDate;

    @CCD(label = "Awaiting Info From FTA", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo awaitingInfoFromFta;
    @CCD(label = "Info Provided By FTA", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo infoProvidedByFta;
    @CCD(label = "Info Request From FTA", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo infoRequestFromFta;
    @CCD(label = "Info Request From Tribunal", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo infoRequestFromTribunal;
    @CCD(label = "Awaiting Info From Tribunal", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo awaitingInfoFromTribunal;
    @CCD(label = "Info Provided By Tribunal", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo infoProvidedByTribunal;
}


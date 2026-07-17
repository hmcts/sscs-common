package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.sscs.ccd.validation.documentlink.DocumentLinkMustBePdf;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;
import uk.gov.hmcts.reform.sscs.ccd.validation.localdate.LocalDateMustNotBeInFuture;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsFinalDecisionCaseData {

    @CCD(
            label = "Is this award about daily living and/or mobility?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionIsDescriptorFlow;
    @CCD(label = "Generate notice?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo writeFinalDecisionGenerateNotice;
    @CCD(
            label = "Is the appeal allowed or refused?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_allowedOrRefused",
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionAllowedOrRefused;
    @CCD(
            label = "What type of hearing was held?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipTypeOfHearing",
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionTypeOfHearing;
    @CCD(
            label = "Did a Presenting Officer attend on behalf of the Respondent?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionPresentingOfficerAttendedQuestion;
    @CCD(
            label = "Did the appellant attend the hearing?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionAppellantAttendedQuestion;
    @CCD(
            label = "Did the appointee attend the hearing?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionAppointeeAttendedQuestion;
    @CCD(
            label = "Other party",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "otherPartyAttendedQuestion",
            access = {SscsCrudAccess.class}
    )
    private List<OtherPartyAttendedQuestion> otherPartyAttendedQuestions;
    @CCD(label = "Start date", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    private String writeFinalDecisionStartDate;
    @CCD(
            label = "Does this award have an end date?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_endDateType",
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionEndDateType;
    @CCD(label = "End date", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    private String writeFinalDecisionEndDate;
    @CCD(label = "Name of Tribunal Member 1", access = {SscsCrudAccess.class})
    private String writeFinalDecisionDisabilityQualifiedPanelMemberName;
    @CCD(label = "Name of Tribunal Member 2", access = {SscsCrudAccess.class})
    private String writeFinalDecisionMedicallyQualifiedPanelMemberName;
    @CCD(label = "Name of Tribunal Member 2", access = {SscsCrudAccess.class})
    private String writeFinalDecisionFinanciallyQualifiedPanelMemberName;
    @CCD(label = "Name of Tribunal Member 1", access = {SscsCrudAccess.class})
    private String writeFinalDecisionOtherPanelMemberName;
    @CCD(label = "Date of FTA's decision", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    @LocalDateMustNotBeInFuture(message = "Decision notice date of decision must not be in the future", groups = UniversalCreditValidationGroup.class)
    private String writeFinalDecisionDateOfDecision;
    @CCD(
            label = "Is date of decision after severe conditions criteria apply date?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo writeFinalDecisionDateOfDecisionIsAfterSvDate;
    @CCD(label = "Summary of outcome decision", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String writeFinalDecisionDetailsOfDecision;
    @CCD(
            label = "Reasons for decision",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "TextArea",
            access = {SscsCrudAccess.class}
    )
    private List<CollectionItem<String>> writeFinalDecisionReasons;
    @CCD(
            label = "What is the last page in the tribunal bundle?",
            hint = "For example: B7",
            access = {SscsCrudAccess.class}
    )
    private String writeFinalDecisionPageSectionReference;
    @CCD(label = "Anything else?", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String writeFinalDecisionAnythingElse;
    @CCD(
            label = "Preview Decision Notice",
            hint = "All documents must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document,
            access = {SscsCrudAccess.class}
    )
    @DocumentLinkMustBePdf(message = "You need to upload PDF documents only", groups = UniversalCreditValidationGroup.class)
    private DocumentLink writeFinalDecisionPreviewDocument;
    @CCD(label = "Final decision generated date", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    private String writeFinalDecisionGeneratedDate;
    @CCD(label = "final decision judge", access = {SscsCrudAccess.class})
    private String finalDecisionJudge;
    @CCD(label = "final decision held at", access = {SscsCrudAccess.class})
    private String finalDecisionHeldAt;
    @CCD(label = "name", access = {SscsCrudAccess.class})
    private String finalDecisionIdamSurname;
    @CCD(label = "final decision issued date", access = {SscsCrudAccess.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate finalDecisionIssuedDate;
    @CCD(label = "final decision generated date", access = {SscsCrudAccess.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate finalDecisionGeneratedDate;
    @CCD(label = "Was original decision uploaded", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo finalDecisionWasOriginalDecisionUploaded;

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isDailyLivingAndOrMobilityDecision() {
        return StringUtils.equalsIgnoreCase("yes", writeFinalDecisionIsDescriptorFlow);
    }
}

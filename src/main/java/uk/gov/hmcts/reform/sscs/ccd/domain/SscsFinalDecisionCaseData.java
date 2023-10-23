package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsFinalDecisionCaseData {

    private String writeFinalDecisionIsDescriptorFlow;
    private String writeFinalDecisionGenerateNotice;
    private String writeFinalDecisionAllowedOrRefused;
    private String writeFinalDecisionTypeOfHearing;
    private String writeFinalDecisionPresentingOfficerAttendedQuestion;
    private String writeFinalDecisionAppellantAttendedQuestion;
    private String writeFinalDecisionAppointeeAttendedQuestion;
    private List<OtherPartyAttendedQuestion> otherPartyAttendedQuestions;
    private String writeFinalDecisionStartDate;
    private String writeFinalDecisionEndDateType;
    private String writeFinalDecisionEndDate;
    private String writeFinalDecisionDisabilityQualifiedPanelMemberName;
    private String writeFinalDecisionMedicallyQualifiedPanelMemberName;
    private String writeFinalDecisionFinanciallyQualifiedPanelMemberName;
    private String writeFinalDecisionOtherPanelMemberName;
    @LocalDateMustNotBeInFuture(message = "Decision notice date of decision must not be in the future", groups = UniversalCreditValidationGroup.class)
    private String writeFinalDecisionDateOfDecision;
    private String writeFinalDecisionDetailsOfDecision;
    private List<CollectionItem<String>> writeFinalDecisionReasons;
    private String writeFinalDecisionPageSectionReference;
    private String writeFinalDecisionAnythingElse;
    @DocumentLinkMustBePdf(message = "You need to upload PDF documents only", groups = UniversalCreditValidationGroup.class)
    private DocumentLink writeFinalDecisionPreviewDocument;
    private String writeFinalDecisionGeneratedDate;
    private String finalDecisionJudge;
    private String finalDecisionHeldAt;
    private String finalDecisionIdamSurname;
    private LocalDate finalDecisionIssuedDate;
    private LocalDate finalDecisionGeneratedDate;
    private YesNo finalDecisionWasOriginalDecisionUploaded;

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isDailyLivingAndOrMobilityDecision() {
        return StringUtils.equalsIgnoreCase("yes", writeFinalDecisionIsDescriptorFlow);
    }
}

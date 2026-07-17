package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.ccd.validation.documentlink.DocumentLinkMustBePdf;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterHmrcresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.RegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkRSuperuserCrudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudPanelmemberRCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CallagentCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCudSuperuserCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterRuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.HmrcresponsewriterRuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarTeamleaderCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.IbcaresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkJudgeRegistrarSuperuserRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.GSProfileRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CitizenRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.domain.WorkType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Panel;
import uk.gov.hmcts.reform.sscs.ccd.domain.TimeExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.ReasonsForChallenge;
import uk.gov.hmcts.reform.sscs.ccd.domain.RevisedReason;
import uk.gov.hmcts.reform.sscs.ccd.domain.Lapse;
import uk.gov.hmcts.reform.sscs.ccd.domain.Withdrawal;
import uk.gov.hmcts.reform.sscs.ccd.domain.DecisionCorrection;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtjReferral;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseCorrectionInfo;
import uk.gov.hmcts.reform.sscs.ccd.domain.DecisionInformation;
import uk.gov.hmcts.reform.sscs.ccd.domain.InterlocDecisions;
import uk.gov.hmcts.reform.sscs.ccd.domain.InterlocDirection2;
import uk.gov.hmcts.reform.sscs.ccd.domain.StrikeOutDocument;
import uk.gov.hmcts.reform.sscs.ccd.domain.JudgeNotePad;
import uk.gov.hmcts.reform.sscs.ccd.domain.DwpWorkFlow;
import uk.gov.hmcts.reform.sscs.ccd.domain.DwpNoActionDocumentCT;
import uk.gov.hmcts.reform.sscs.ccd.domain.InterlocWaiver;
import uk.gov.hmcts.reform.sscs.ccd.domain.BulkScanEnvelope;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingRoute2;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Adjournment {
    @CCD(label = "Generate notice?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseGenerateNotice")
    private YesNo generateNotice;
    @CCD(label = "What type of hearing was held?", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseTypeOfHearing")
    private AdjournCaseTypeOfHearing typeOfHearing;
    @CCD(label = "Can case be listed right away?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseCanCaseBeListedRightAway")
    private YesNo canCaseBeListedRightAway;
    @CCD(
            label = "Are you making directions to the parties?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCaseAreDirectionsBeingMadeToParties")
    private YesNo areDirectionsBeingMadeToParties;
    @CCD(label = "Directions due date", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseDirectionsDueDateDaysOffset")
    private AdjournCaseDaysOffset directionsDueDateDaysOffset;

    @CCD(label = "Specified directions due date", access = {SscsCrudAccess.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("adjournCaseDirectionsDueDate")
    private LocalDate directionsDueDate;
    @CCD(label = "Confirm the format of the next hearing", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseTypeOfNextHearing")
    private AdjournCaseTypeOfHearing typeOfNextHearing;
    @CCD(label = "Where should the next hearing be listed?", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseNextHearingVenue")
    private AdjournCaseNextHearingVenue nextHearingVenue;
    @CCD(label = "Specify the venue", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseNextHearingVenueSelected")
    private DynamicList nextHearingVenueSelected;
    @CCD(label = "Are panel members excluded?", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCasePanelMembersExcluded")
    private AdjournCasePanelMembersExcluded panelMembersExcluded;
    @CCD(label = "Name of the Disability Qualified Panel Member (DQPM)", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseDisabilityQualifiedPanelMemberName")
    private String disabilityQualifiedPanelMemberName;
    @CCD(label = "Name of the Medically Qualified Panel Member (MQPM)", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseMedicallyQualifiedPanelMemberName")
    private String medicallyQualifiedPanelMemberName;
    @CCD(label = "Other", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseOtherPanelMemberName")
    private String otherPanelMemberName;
    @CCD(
            label = "Name of the Disability Qualified Panel Member (DQPM)",
            typeOverride = FieldType.JudicialUser,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCasePanelMember1")
    private JudicialUserBase panelMember1;
    @CCD(
            label = "Name of the Medically Qualified Panel Member (MQPM)",
            typeOverride = FieldType.JudicialUser,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCasePanelMember2")
    private JudicialUserBase panelMember2;
    @CCD(label = "Other", typeOverride = FieldType.JudicialUser, access = {SscsCrudAccess.class})
    @JsonProperty("adjournCasePanelMember3")
    private JudicialUserBase panelMember3;
    @CCD(label = "Adjourned signed in user", typeOverride = FieldType.JudicialUser, access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseSignedInUser")
    private JudicialUserBase signedInUser;
    @CCD(label = "How long should the next hearing be listed for?", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseNextHearingListingDurationType")
    private AdjournCaseNextHearingDurationType nextHearingListingDurationType;
    @CCD(label = "Duration length", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseNextHearingListingDuration")
    private Integer nextHearingListingDuration;
    @CCD(
            label = "Minutes or sessions",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_adjournCaseNextHearingDurationUnits",
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCaseNextHearingListingDurationUnits")
    private AdjournCaseNextHearingDurationUnits nextHearingListingDurationUnits;
    @CCD(
            label = "Is an interpreter required for the hearing?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCaseInterpreterRequired")
    private YesNo interpreterRequired;
    @CCD(
            label = "What language do they need to speak?",
            typeOverride = FieldType.DynamicList,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCaseInterpreterLanguageList")
    private DynamicList interpreterLanguage;
    @CCD(label = "When should the next hearing be?", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseNextHearingDateType")
    private AdjournCaseNextHearingDateType nextHearingDateType;
    @CCD(label = "Provide date or period?", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseNextHearingDateOrPeriod")
    private AdjournCaseNextHearingDateOrPeriod nextHearingDateOrPeriod;
    @CCD(ignore = true)
    @JsonProperty("adjournCaseNextHearingDateOrTime")
    private String nextHearingDateOrTime;

    @CCD(label = "Provide date", access = {SscsCrudAccess.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("adjournCaseNextHearingFirstAvailableDateAfterDate")
    private LocalDate nextHearingFirstAvailableDateAfterDate;
    @CCD(label = "Provide period", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseNextHearingFirstAvailableDateAfterPeriod")
    private AdjournCaseNextHearingPeriod nextHearingFirstAvailableDateAfterPeriod;
    @CCD(label = " ", access = {SscsCrudAccess.class})
    @JsonProperty("adjournCaseTime")
    private AdjournCaseTime time;
    @CCD(
            label = "Reasons for adjournment",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "TextArea",
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCaseReasons")
    private List<CollectionItem<String>> reasons;
    @CCD(
            label = "Additional directions",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "TextArea",
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("adjournCaseAdditionalDirections")
    private List<CollectionItem<String>> additionalDirections;

    @CCD(
            label = "Preview Adjournment",
            hint = "All documents must be PDF formatted",
            typeOverride = FieldType.Document,
            access = {SscsCrudAccess.class}
    )
    @DocumentLinkMustBePdf(message = "You need to upload PDF documents only")
    @JsonProperty("adjournCasePreviewDocument")
    private DocumentLink previewDocument;

    @CCD(label = "Adjourn case generated date", access = {SscsCrudAccess.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("adjournCaseGeneratedDate")
    private LocalDate generatedDate;
    @CCD(
            label = "Adjournment in progress?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsSuperuserCrudAccess.class, JudgeSystemupdateCrudAccess.class}
    )
    @JsonProperty("adjournmentInProgress")
    private YesNo adjournmentInProgress;

    @SuppressWarnings("unused")
    @JsonIgnore
    public List<JudicialUserBase> getPanelMembers() {
        List<JudicialUserBase> panelMembers = Arrays.asList(this.panelMember1,
            this.panelMember2,
            this.panelMember3);

        return panelMembers.stream().filter(Objects::nonNull)
            .filter(panelMember -> nonNull(panelMember.getIdamId()) || nonNull(panelMember.getPersonalCode()))
            .collect(Collectors.toList());
    }

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(
          label = "Work Type",
          hint = "Select a work type e.g. Pre Hearing Work",
          typeOverride = FieldType.FixedList,
          typeParameterOverride = "FL_workType",
          access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, JudgeCruAccess.class, RegistrarCrudAccess.class}
  )
  private WorkType workType;
  @CCD(
          label = "Panel",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCudPanelmemberRCitizenCrudAccess.class, JudgeCrudAccess.class}
  )
  private Panel onlinePanel;
  @CCD(
          label = "Time extension reason",
          typeOverride = FieldType.FixedList,
          typeParameterOverride = "FL_timeExtension",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private TimeExtension timeExtension;
  @CCD(
          label = "Reasons for challenge",
          typeOverride = FieldType.FixedList,
          typeParameterOverride = "FL_reasonsForChallenge",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private ReasonsForChallenge reasonsForChallenge;
  @CCD(
          label = "Revised reason",
          typeOverride = FieldType.FixedList,
          typeParameterOverride = "FL_revisedReason",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private RevisedReason revisedReason;
  @CCD(
          label = "Lapsed case information",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private Lapse lapseInfo;
  @CCD(
          label = "Withdrawn case information",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private Withdrawal withdrawalInfo;
  @CCD(
          label = "Decision correction information",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private DecisionCorrection decisionCorrectionInfo;
  @CCD(
          label = "Referral information",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private OtjReferral referralInfo;
  @CCD(
          label = "Correction information",
          access = {DefaultAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private CaseCorrectionInfo caseCorrection;
  @CCD(
          label = "Decision information",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, CallagentCrudAccess.class}
  )
  private DecisionInformation decisionInfo;
  @CCD(
          label = "Date information requested",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, CallagentCrudAccess.class}
  )
  private java.time.LocalDate dateInformationRequested;
  @CCD(
          label = "Judges Direction",
          hint = "Copy the Judge's direction information here",
          typeOverride = FieldType.TextArea,
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, CallagentCrudAccess.class}
  )
  private String directionText;
  @CCD(
          label = "Request info from FTA",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudAccess.class}
  )
  private uk.gov.hmcts.ccd.sdk.type.YesOrNo informationFromDWP;
  @CCD(
          label = "Request info from an other party",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudAccess.class}
  )
  private uk.gov.hmcts.ccd.sdk.type.YesOrNo informationFromOther;
  @CCD(label = "Interloc Decisions", access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRAccess.class})
  private InterlocDecisions interlocDecisionInfo;
  @CCD(label = "Interloc Directions", access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRAccess.class})
  private InterlocDirection2 interlocDirectionInfo;
  @CCD(label = " ", access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRAccess.class, SscsRAccess.class})
  private StrikeOutDocument interlocStrikeOutInfo;
  @CCD(label = "Judges notepad", access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRAccess.class})
  private JudgeNotePad judgeAppealNotePad;
  @CCD(label = "In progress (TCW)", access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class})
  private uk.gov.hmcts.ccd.sdk.type.YesOrNo tcwInProgress;
  @CCD(
          label = "Case history",
          typeOverride = FieldType.CaseHistoryViewer,
          access = {DwpresponsewriterHmrcresponsewriterCrudAccess.class, ClerkCudSuperuserCudSystemupdateCrudAccess.class, SscsRAccess.class}
  )
  private String caseHistory;
  @CCD(
          label = "FTA response state",
          hint = "The response state of a case with FTA",
          typeOverride = FieldType.FixedList,
          typeParameterOverride = "FL_dwpWorkFlow",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterRuAccess.class, HmrcresponsewriterRuAccess.class}
  )
  private DwpWorkFlow dwpResponseState;
  @CCD(
          label = "Exception Record Reference",
          access = {SscsRSuperuserCrudAccess.class, ClerkRAccess.class, SystemupdateCruAccess.class}
  )
  private String exceptionRecordReference;
  @CCD(
          label = "**Edited evidence reason:** \r\n${dwpEditedEvidenceReason}",
          typeOverride = FieldType.Label,
          access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, IbcaresponsewriterCrudAccess.class}
  )
  private String dwpEditedEvidenceReasonLabel;
  @CCD(
          label = "No action",
          access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class}
  )
  private DwpNoActionDocumentCT dwpNoActionDoc;
  @CCD(
          label = "Old case validation waiver",
          access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class, SscsCudAccess.class}
  )
  private InterlocWaiver interlocWaiverInfo;
  @CCD(
          label = "**Name**  \r\n${appeal.appellant.name.firstName} ${appeal.appellant.name.lastName}  ",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryName;
  @CCD(
          label = "**Address**  \r\n${appeal.appellant.address.line1}, ${appeal.appellant.address.line2}, ${appeal.appellant.address.town}, ${appeal.appellant.address.county}, ${appeal.appellant.address.country}  \r\n${appeal.appellant.address.postcode}  ",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryAddress;
  @CCD(
          label = "**NINO**  \r\n${appeal.appellant.identity.nino}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryNino;
  @CCD(
          label = "**Role**  \r\n${appeal.appellant.role.name}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryRole;
  @CCD(
          label = "**Role Description**  \r\n${appeal.appellant.role.description}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryRoleDescription;
  @CCD(
          label = "**Date of birth**  \r\n${appeal.appellant.identity.dob}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryDOB;
  @CCD(
          label = "**IBCA Reference**  \r\n${appeal.appellant.identity.ibcaReference}",
          typeOverride = FieldType.Label,
          access = {ClerkCrudAccess.class, IbcaresponsewriterCrudAccess.class, SscsCudAccess.class}
  )
  private String summaryIbcaReference;
  @CCD(
          label = "**Benefit type**  \r\n${appeal.benefitType.description}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryBenefitType;
  @CCD(
          label = "**Appeal type**  \r\n${appeal.benefitType.description}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryAppealType;
  @CCD(label = "**SC number**  \r\n${caseReference}", typeOverride = FieldType.Label, access = {SscsCudAccess.class})
  private String summaryCaseReference;
  @CCD(label = "**Appeal status**  \r\n${[STATE]}", typeOverride = FieldType.Label, access = {SscsCudAccess.class})
  private String summaryState;
  @CCD(
          label = "**Hearing type**  \r\n${appeal.hearingType}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryHearingType;
  @CCD(
          label = "**Appointee**  \r\n${appeal.appellant.isAppointee}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryAreAppointee;
  @CCD(
          label = "**Representative**  \r\n${appeal.rep.name.firstName} ${appeal.rep.name.lastName}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryRepresentativeName;
  @CCD(
          label = "**Joint party name**  \r\n${jointPartyName.firstName} ${jointPartyName.lastName}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryJointPartyName;
  @CCD(
          label = "**Joint party same address as appellant**  \r\n${jointPartyAddressSameAsAppellant} ",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryJointPartySameAddress;
  @CCD(
          label = "**Joint party address**  \r\n${jointPartyAddress.line1}, ${jointPartyAddress.line2}, ${jointPartyAddress.town}  \r\n${jointPartyAddress.postcode}  ",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryJointPartyAddress;
  @CCD(
          label = "**Joint party NINO**  \r\n${jointPartyIdentity.nino} ",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryJointPartyNino;
  @CCD(
          label = "**Joint party date of birth**  \r\n${jointPartyIdentity.dob} ",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryJointPartyDob;
  @CCD(
          label = "**Track Your Appeal appellant link**  \r\n${CCD_DEF_TYA_LINK}",
          typeOverride = FieldType.Label,
          access = {JudgeRegistrarRAccess.class, ClerkRAccess.class}
  )
  private String summaryTyaNumber;
  @CCD(
          label = "**Track Your Appeal appointee link**  \r\n${CCD_DEF_TYA_APPOINTEE_LINK}",
          typeOverride = FieldType.Label,
          access = {JudgeRegistrarRAccess.class, ClerkRAccess.class}
  )
  private String summaryTyaAppointeeNumber;
  @CCD(
          label = "**Manage Your Appeal appellant link**  \r\n${CCD_DEF_MYA_LINK}",
          typeOverride = FieldType.Label,
          access = {ClerkJudgeRegistrarSuperuserRAccess.class}
  )
  private String summaryMyaAppellantLink;
  @CCD(
          label = "**Manage Your Appeal representative link**  \r\n${CCD_DEF_MYA_REPRESENTATIVE_LINK}",
          typeOverride = FieldType.Label,
          access = {ClerkJudgeRegistrarSuperuserRAccess.class}
  )
  private String summaryMyaRepresentativeLink;
  @CCD(
          label = "**Manage Your Appeal appointee link**  \r\n${CCD_DEF_MYA_APPOINTEE_LINK}",
          typeOverride = FieldType.Label,
          access = {ClerkJudgeRegistrarSuperuserRAccess.class}
  )
  private String summaryMyaAppointeeLink;
  @CCD(
          label = "**Reason referred to interloc**  \r\n${interlocReferralReason}",
          typeOverride = FieldType.Label,
          access = {JudgeRegistrarCrudAccess.class, ClerkSuperuserCrudAccess.class}
  )
  private String summaryInterlocReferralReason;
  @CCD(
          label = "## Is a new digital case? ${createdInGapsFrom}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryCreatedInGapsFrom;
  @CCD(
          label = "Select parties who are to receive this notice",
          typeOverride = FieldType.Label,
          access = {SscsRAccess.class}
  )
  private String selectPartiesDirectionsNotice;
  @CCD(label = "Bulk Scan Envelopes", access = {SscsSuperuserCrudAccess.class, ClerkCrudAccess.class})
  private java.util.List<uk.gov.hmcts.ccd.sdk.type.ListValue<BulkScanEnvelope>> bulkScanEnvelopes;
  @CCD(label = "Date to restore cases for", access = {SscsCrudAccess.class})
  private java.time.LocalDate restoreCasesDate;
  @CCD(label = "File name used to restore cases", access = {SscsCrudAccess.class})
  private String restoreCaseFileName;
  @CCD(
          label = "## ![Is case confidential? ${isConfidentialCase}](https://raw.githubusercontent.com/hmcts/sscs-tribunals-case-api/master/assets/images/ConfidentialTag-200px.png \"Is case confidential? ${isConfidentialCase}\")",
          typeOverride = FieldType.Label
  )
  private String isConfidentialCaseLabel;
  @CCD(label = "## Confidentiality Request: Under Review", typeOverride = FieldType.Label)
  private String isConfidentialityRequestUnderReviewLabel;
  @CCD(
          label = "## Is progressing via GAPS? ${isProgressingViaGaps}",
          typeOverride = FieldType.Label,
          access = {SscsCudAccess.class}
  )
  private String summaryIsProgressingViaGaps;
  @CCD(label = "## PHE on this case: Granted", typeOverride = FieldType.Label)
  private String phmeGrantedLabel;
  @CCD(label = "## PHE on this case: Under Review", typeOverride = FieldType.Label)
  private String phmeUnderReviewLabel;
  @CCD(label = "## Appellant deceased", typeOverride = FieldType.Label, access = {SscsCudAccess.class})
  private String appellantDeceasedLabel;
  @CCD(label = "## Bundle check required: ${isInc5249521} ", typeOverride = FieldType.Label)
  private String isInc5249521CaseLabel;
  @CCD(
          label = "**Confidentiality Required**  \r\n${appeal.appellant.confidentialityRequired}",
          typeOverride = FieldType.Label
  )
  private String summaryConfidentialityRequired;
  @CCD(label = "Case name", access = {SscsCitizenCrudAccess.class})
  private String caseName;
  @JsonProperty("SearchCriteria")
  @CCD(label = "Search Criteria", access = {GSProfileRAccess.class, CitizenRAccess.class})
  private uk.gov.hmcts.ccd.sdk.type.SearchCriteria searchCriteria;
  @CCD(
          label = "Hearing Route",
          access = {SscsSuperuserCrudAccess.class, JudgeRegistrarCrudAccess.class, ClerkCrudAccess.class, DwpresponsewriterCrudAccess.class}
  )
  private HearingRoute2 workBasketHearingRoute;
  @CCD(label = "Active Hearing ID", access = {SscsSuperuserCrudAccess.class, SystemupdateCrudAccess.class})
  private Integer activeHearingId;
  @CCD(label = "Active Hearing Version Number", access = {SscsSuperuserCrudAccess.class, SystemupdateCrudAccess.class})
  private Integer activeHearingVersionNumber;
  @CCD(label = "Body content", typeOverride = FieldType.TextArea)
  private String bodyContentCorrection;
  @CCD(
          label = "**You are now correcting a decision notice**",
          typeOverride = FieldType.Label,
          access = {SscsCrudAccess.class}
  )
  private String writeFinalDecisionLabelCorrection;
  @CCD(
          label = "If you select 'No reply required' a reply will not be sent and you do not need to enter text into the 'Reply' text box",
          typeOverride = FieldType.Label,
          access = {SscsCrudAccess.class}
  )
  private String communicationNoActionLabel;
  @CCD(
          label = "If an interpreter is needed for the next hearing, please add an extra 30 minutes to the duration.",
          typeOverride = FieldType.Label,
          access = {SscsCrudAccess.class}
  )
  private String interpreterDurationLabel;
  @CCD(label = "Confidentiality", access = {SscsCudCitizenCrudAccess.class})
  private String confidentialityTab;
  @CCD(label = "${confidentialityTab}", typeOverride = FieldType.Label, access = {SscsCudAccess.class})
  private String confidentialityTabLabel;
  @CCD(
          label = "Please add details of the other party.",
          typeOverride = FieldType.Label,
          access = {DwpresponsewriterCrudAccess.class, SuperuserCrudAccess.class}
  )
  private String addOtherPartyDetailsLabel;
  @CCD(
          label = "If yes, please use the Communicate with Tribunal event to provide their details.",
          typeOverride = FieldType.Label,
          access = {DwpresponsewriterCrudAccess.class, SuperuserCrudAccess.class}
  )
  private String addOtherPartyCommunicationLabel;
  @CCD(
          label = "Please submit if confidentiality has been determined for all parties on the case.",
          typeOverride = FieldType.Label,
          access = {SscsSuperuserCrudAccess.class, ClerkCrudAccess.class}
  )
  private String confidentialityConfirmedLabel;
  // ==== end synthesised definition-only fields ====
}

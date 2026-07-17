package uk.gov.hmcts.reform.sscs.ccd.domain;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.INFECTED_BLOOD_COMPENSATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.UC;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.findBenefitByShortName;
import static uk.gov.hmcts.reform.sscs.ccd.domain.ConfidentialityTabBuilder.buildConfidentialityTab;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.FINAL_DECISION_ISSUED;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.ccd.predicates.BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.sscs.ccd.predicates.BenefitTypeConfidentialityPredicate;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;
import uk.gov.hmcts.reform.sscs.ccd.validation.localdate.LocalDateMustNotBeInFuture;
import uk.gov.hmcts.reform.sscs.model.PoDetails;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterHmrcresponsewriterCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.RegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkRSuperuserCrudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.PanelmemberRRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterHmrcresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.IbcaresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.PcqextractorRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudPanelmemberRCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CaseworkerWaTaskConfigurationRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CallagentCruRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.AnonymouscitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarTeamleaderCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrPlus4RolesDetidgAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCruPlus4RolesOesmduAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.RegistrarCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.PanelmemberRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CallagentCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCudSuperuserCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CallagentCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CitizenRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.IbcaresponsewriterRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCruSuperuserCrudSystemupdateCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkSystemupdateCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRIbcaresponsewriterCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.IbcaresponsewriterCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRIbcaresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudPlus9RolesJlkxvmAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRPlus9RolesXqaxtgAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.RegistrarCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRPlus6RolesJpbhhvAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkJudgeRegistrarSuperuserRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsDwpresponsewriterHmrcresponsewriterRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterCrudPlus5RolesIunvywAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRClerkCruSuperuserCruSystemupdateCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.TeamleaderRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterRuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.HmrcresponsewriterRuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.RegistrarRuAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.TeamleaderCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CallagentRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.HmrcresponsewriterRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsJudgeCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.PcqextractorCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.GSProfileRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CaseworkerRasValidationRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRDwpresponsewriterCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class SscsCaseData implements CaseData {

    @CCD(ignore = true)
    @JsonProperty(access = WRITE_ONLY)
    private String ccdCaseId;

    @CCD(label = "End state", typeOverride = FieldType.Text)
    private State state;
    @CCD(label = "Previous State", typeOverride = FieldType.Text, access = {SscsCudSystemupdateCrudAccess.class})
    private State previousState;
    @CCD(
            label = "SC Case Number",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCudAccess.class}
    )
    private String caseReference;
    @CCD(
            label = "Case Created Date",
            typeOverride = FieldType.Date,
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCudAccess.class, RegistrarCrudAccess.class}
    )
    private String caseCreated;
    @CCD(label = "Info request", access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudAccess.class})
    private InfoRequests infoRequests;
    @CCD(
            label = "Regional Centre",
            access = {DefaultAccess.class, SscsCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCudAccess.class, PanelmemberRRegistrarCrudAccess.class, JudgeCudAccess.class}
    )
    private String region;
    @CCD(
            label = "Appeal",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsCitizenCrudAccess.class, JudgeRegistrarCrudAccess.class, IbcaresponsewriterCrudAccess.class, PcqextractorRAccess.class}
    )
    private Appeal appeal;
    @CCD(
            label = "Hearings",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingDetails",
            access = {DefaultAccess.class, SscsCudPanelmemberRCitizenCrudAccess.class, JudgeCrudAccess.class, CaseworkerWaTaskConfigurationRAccess.class}
    )
    private List<Hearing> hearings;
    @CCD(
            label = "Completed Hearings",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingDetails",
            access = {SscsSuperuserCrudAccess.class, ClerkCrudAccess.class}
    )
    private List<Hearing> completedHearingsList;
    @CCD(
            label = "Hearing Outcomes",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingOutcomeDetails",
            access = {ClerkCrudAccess.class, SuperuserCudAccess.class, SscsCrudAccess.class}
    )
    private List<HearingOutcome> hearingOutcomes;
    @CCD(label = "Hearing Outcome", access = {SscsSuperuserCrudAccess.class, ClerkCrudAccess.class})
    private HearingOutcomeValue hearingOutcomeValue;
    @CCD(
            label = "Evidence",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsCudPanelmemberRCitizenCrudAccess.class, JudgeCrudAccess.class}
    )
    private Evidence evidence;
    @CCD(
            label = "Panel Member Composition",
            access = {SscsSuperuserCrudAccess.class, JudgeCudAccess.class, SystemupdateCrudAccess.class}
    )
    private PanelMemberComposition panelMemberComposition;
    @CCD(
            label = "FTA Time Extension",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "dwpTimeExtension",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    private List<DwpTimeExtension> dwpTimeExtension;
    @CCD(
            label = "Case Progression",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "event",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    private List<Event> events;
    @CCD(
            label = "Subscribers",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, CallagentCruRegistrarCrudAccess.class, AnonymouscitizenCrudAccess.class}
    )
    @Getter(AccessLevel.NONE)
    @JsonProperty("subscriptions")
    private Subscriptions subscriptions;
    @CCD(
            label = "RegionalProcessingCenter",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, RegistrarCrudAccess.class, CaseworkerWaTaskConfigurationRAccess.class}
    )
    private RegionalProcessingCenter regionalProcessingCenter;
    @CCD(
            label = "Latest Bundles",
            hint = "Bundle to hold documents",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "Bundle",
            access = {DefaultAccess.class, JudgeRegistrarCudAccess.class, CitizenCrudAccess.class}
    )
    private List<Bundle> caseBundles;
    @CCD(
            label = "Historical Bundles",
            hint = "Bundle to hold documents",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "Bundle",
            access = {JudgeRegistrarCudAccess.class, ClerkCudAccess.class, SuperuserCudAccess.class, SystemupdateCudAccess.class, CitizenCrudAccess.class}
    )
    private List<Bundle> historicalBundles;
    @CCD(
            label = "Case documents",
            regex = "No",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsDocument",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsCrPlus4RolesDetidgAccess.class}
    )
    private List<SscsDocument> sscsDocument;
    @JsonUnwrapped
    private InternalCaseDocumentData internalCaseDocumentData;
    @CCD(
            label = "Hidden document collection",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsDocument",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class}
    )
    private List<SscsDocument> draftSscsDocument;
    @CCD(
            label = "Further evidence  documents",
            regex = "No",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsFurtherEvidenceDoc",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsCrPlus4RolesDetidgAccess.class}
    )
    private List<SscsFurtherEvidenceDoc> draftSscsFurtherEvidenceDocument;
    @CCD(
            label = "Interloc Decisions",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRAccess.class}
    )
    private SscsInterlocDecisionDocument sscsInterlocDecisionDocument;
    @CCD(
            label = "Interloc Directions",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRAccess.class}
    )
    private SscsInterlocDirectionDocument sscsInterlocDirectionDocument;
    @CCD(
            label = "Interloc strike out information",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private SscsStrikeOutDocument sscsStrikeOutDocument;
    @CCD(
            label = "Is a save & return case?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCudCitizenCrudAccess.class, AnonymouscitizenCrudAccess.class}
    )
    private String isSaveAndReturn;
    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsDeprecatedFields sscsDeprecatedFields;
    @CCD(
            label = "Upload a response to a direction",
            access = {ClerkCruPlus4RolesOesmduAccess.class, JudgeCruAccess.class, RegistrarCruAccess.class, SscsRAccess.class}
    )
    private DirectionResponse directionResponse;
    @CCD(
            label = "Evidence Present",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCudAccess.class, JudgeRegistrarCudAccess.class, PanelmemberRAccess.class}
    )
    private String evidencePresent;
    @CCD(
            label = "Case reference of scanned case",
            access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class, SscsCudPanelmemberRCitizenCrudAccess.class, CallagentCruAccess.class}
    )
    private String bulkScanCaseReference;
    @CCD(
            label = "Decision Notes",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    private String decisionNotes;
    @CCD(
            label = "COR Decision?",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    @JsonProperty("isCorDecision")
    private String isCorDecision;
    @CCD(label = "Relisting reason", access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class})
    private String relistingReason;
    @CCD(
            label = "Date evidence sent to FTA",
            typeOverride = FieldType.Date,
            access = {ClerkCudSuperuserCudSystemupdateCrudAccess.class, SscsCudPanelmemberRCitizenCrudAccess.class, CallagentCrudAccess.class}
    )
    private String dateSentToDwp;
    @CCD(
            label = "FTA response due date",
            typeOverride = FieldType.Date,
            access = {DefaultAccess.class, CallagentCrudAccess.class, PanelmemberRAccess.class, SscsCrudAccess.class, CitizenRAccess.class}
    )
    private String dwpDueDate;
    @CCD(
            label = "Interlocutory review state",
            hint = "The state of a case whilst in interlocutory review",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_interlocWorkflow",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, IbcaresponsewriterRegistrarCrudAccess.class, JudgeCruAccess.class}
    )
    private InterlocReviewState interlocReviewState;
    @CCD(
            label = "Sent to FTA state",
            hint = "The state of a case after attempting to send to FTA",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_withDwpWorkflow",
            access = {DefaultAccess.class, JudgeRuAccess.class, SscsCrudAccess.class}
    )
    private String hmctsDwpState;
    @CCD(
            label = "FTA Further Evidence State",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_dwpFurtherEvidenceStates",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, IbcaresponsewriterCrudAccess.class}
    )
    private String dwpFurtherEvidenceStates;
    @CCD(
            label = "Process Audio/Video Action",
            typeOverride = FieldType.DynamicList,
            access = {JudgeRegistrarCrudAccess.class, SscsRSuperuserCrudAccess.class}
    )
    private DynamicList processAudioVideoAction;
    @CCD(
            label = "Original Sender",
            typeOverride = FieldType.DynamicList,
            access = {ClerkCruSuperuserCrudSystemupdateCruAccess.class, SscsCruAccess.class}
    )
    private DynamicList originalSender;
    @CCD(
            label = "Further Evidence Action",
            typeOverride = FieldType.DynamicList,
            access = {ClerkCruSuperuserCrudSystemupdateCruAccess.class, SscsCruAccess.class}
    )
    private DynamicList furtherEvidenceAction;
    @CCD(
            label = "Scanned Documents",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "ScannedDocument",
            access = {SscsSuperuserCrudAccess.class, ClerkSystemupdateCruAccess.class, PcqextractorRAccess.class}
    )
    private List<ScannedDocument> scannedDocuments;
    @CCD(label = "Selected Audio/Video Evidence", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList selectedAudioVideoEvidence;
    @CCD(label = "Selected Audio/Video Evidence Details", access = {SscsCrudAccess.class})
    private AudioVideoEvidenceDetails selectedAudioVideoEvidenceDetails;
    @CCD(
            label = "Evidence",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "AudioVideoEvidence",
            access = {SscsSuperuserCrudAccess.class, ClerkCuAccess.class, SystemupdateCrudAccess.class}
    )
    private List<AudioVideoEvidence> audioVideoEvidence;
    @CCD(
            label = "Audio/Video Evidence",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "AudioVideoEvidence",
            access = {ClerkCruPlus4RolesOesmduAccess.class, SscsRIbcaresponsewriterCruAccess.class}
    )
    private List<AudioVideoEvidence> dwpUploadAudioVideoEvidence;
    @CCD(
            label = "Has unprocessed Audio/Video evidence",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo hasUnprocessedAudioVideoEvidence;
    @CCD(label = "Request info from case party", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private String informationFromAppellant;
    @CCD(
            label = "Select party to request info from",
            typeOverride = FieldType.DynamicList,
            access = {SscsCrudAccess.class}
    )
    private DynamicList informationFromPartySelected;
    @CCD(
            label = "Outcome",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_outcome",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCudCitizenCrudAccess.class, CallagentCrudAccess.class}
    )
    private String outcome;
    @CCD(
            label = "Further evidence handled",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCruSuperuserCrudSystemupdateCruAccess.class, SscsRuAccess.class}
    )
    private String evidenceHandled;
    @CCD(label = "Presenting Officer's Details:", access = {SscsCrudAccess.class})
    @JsonProperty("presentingOfficersDetails")
    private PoDetails presentingOfficersDetails;
    @CCD(label = "Hearing Link", access = {SscsCrudAccess.class})
    private String presentingOfficersHearingLink;
    @CCD(label = "PO Attendance Confirmed?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo poAttendanceConfirmed;
    @CCD(label = "Tribunal direct PO to attend?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo tribunalDirectPoToAttend;

    //SSCS-10007
    @CCD(
            label = " ",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "otherPartySelection",
            access = {DefaultAccess.class}
    )
    private List<CcdValue<OtherPartySelectionDetails>> otherPartySelection;
    @CCD(
            label = " ",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "documentSelectionList",
            access = {DefaultAccess.class}
    )
    private List<CcdValue<DocumentSelectionDetails>> documentSelection;
    @CCD(ignore = true)
    private DynamicList letterAttachedDocuments;
    @CCD(label = "Add letter text", typeOverride = FieldType.TextArea, access = {DefaultAccess.class})
    private String genericLetterText;

    @CCD(label = "Send to all parties", typeOverride = FieldType.YesOrNo, access = {DefaultAccess.class})
    private YesNo sendToAllParties;
    @CCD(label = "Appellant (or Appointee)", typeOverride = FieldType.YesOrNo, access = {DefaultAccess.class})
    private YesNo sendToApellant;
    @CCD(label = "Representative", typeOverride = FieldType.YesOrNo, access = {DefaultAccess.class})
    private YesNo sendToRepresentative;
    @CCD(label = "Joint party", typeOverride = FieldType.YesOrNo, access = {DefaultAccess.class})
    private YesNo sendToJointParty;
    @CCD(label = "Other parties", typeOverride = FieldType.YesOrNo, access = {DefaultAccess.class})
    private YesNo sendToOtherParties;
    @CCD(label = "Do you want to add any documents?", typeOverride = FieldType.YesOrNo, access = {DefaultAccess.class})
    private YesNo addDocuments;

    @CCD(
            label = "Joint party toggle",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class}
    )
    private YesNo hasJointParty;
    @CCD(
            label = "Send to all parties",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class}
    )
    private YesNo hasRepresentative;
    @CCD(
            label = "Send to all parties",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class}
    )
    private YesNo hasOtherParties;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private ReissueArtifactUi reissueArtifactUi;
    @CCD(
            label = "Case code",
            regex = "[A-Za-z0-9]{5}",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, JudgeRuAccess.class, RegistrarCrudAccess.class}
    )
    private String caseCode;
    @CCD(
            label = "Benefit code",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_benefitCodes",
            access = {ClerkCruPlus4RolesOesmduAccess.class, SscsRIbcaresponsewriterCruAccess.class, RegistrarCrudAccess.class}
    )
    private String benefitCode;
    @CCD(
            label = "Benefit code",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_benefitCodesIbcaOnly",
            access = {DefaultAccess.class, IbcaresponsewriterRegistrarCrudAccess.class, SscsCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String benefitCodeIbcaOnly;
    @CCD(
            label = "Issue code",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_issueCodes",
            access = {ClerkCruPlus4RolesOesmduAccess.class, SscsRIbcaresponsewriterCruAccess.class, RegistrarCrudAccess.class}
    )
    private String issueCode;
    @CCD(
            label = "Issue code",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_issueCodesIbcaOnly",
            access = {DefaultAccess.class, IbcaresponsewriterRegistrarCrudAccess.class, SscsCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String issueCodeIbcaOnly;
    @CCD(
            label = "Originating office",
            typeOverride = FieldType.DynamicList,
            access = {ClerkCruPlus4RolesOesmduAccess.class}
    )
    private DynamicList dwpOriginatingOffice;
    @CCD(
            label = "Presenting office",
            typeOverride = FieldType.DynamicList,
            access = {ClerkCruPlus4RolesOesmduAccess.class}
    )
    private DynamicList dwpPresentingOffice;
    @CCD(
            label = "FTA recommend PO to attend?",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCruPlus4RolesOesmduAccess.class, JudgeRegistrarCrudAccess.class, IbcaresponsewriterCruAccess.class}
    )
    private String dwpIsOfficerAttending;
    @CCD(
            label = "Appellant - Unacceptable Customer Behaviour (UCB)",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCruPlus4RolesOesmduAccess.class, IbcaresponsewriterCruAccess.class}
    )
    @JsonProperty("dwpUCB")
    private String dwpUcb;
    @CCD(
            label = "FTA unacceptable customer behaviour evidence",
            hint = "All documents must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document,
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsRIbcaresponsewriterCrudAccess.class}
    )
    private DocumentLink dwpUcbEvidenceDocument;
    @CCD(
            label = "Potentially Harmful Medical Evidence (PHME)",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCruPlus4RolesOesmduAccess.class}
    )
    @JsonProperty("dwpPHME")
    private String dwpPhme;
    @CCD(
            label = "Is the appeal complex?",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCruPlus4RolesOesmduAccess.class}
    )
    private String dwpComplexAppeal;
    @CCD(
            label = "Upload contains further information to assist the tribunal",
            hint = "Only select Yes if the case needs to be reviewed by a HMCTS user e.g. additional information provided in AT38 or an agreed complex case code",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCruPlus4RolesOesmduAccess.class, IbcaresponsewriterCruAccess.class}
    )
    private String dwpFurtherInfo;
    @CCD(
            label = "Correspondence",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "correspondence",
            access = {SscsCudPlus9RolesJlkxvmAccess.class}
    )
    private List<Correspondence> correspondence;
    @CCD(label = "Reasonable adjustments", access = {SscsRPlus9RolesXqaxtgAccess.class})
    private ReasonableAdjustmentsLetters reasonableAdjustmentsLetters;
    @CCD(
            label = "Date referred to interloc",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, RegistrarCudAccess.class}
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate interlocReferralDate;
    @CCD(
            label = "Reason referred to interloc",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_interlocReferralReason",
            access = {SscsRSuperuserCrudAccess.class, ClerkCruAccess.class, JudgeRuAccess.class, RegistrarCrudAccess.class, SystemupdateCrudAccess.class}
    )
    private InterlocReferralReason interlocReferralReason;
    @CCD(
            label = "FTA Handling Office",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_dwpRegionalCentre",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCudAccess.class, CitizenCrudAccess.class}
    )
    private String dwpRegionalCentre;
    @CCD(
            label = "FTA State",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_dwpStates",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, IbcaresponsewriterRegistrarCrudAccess.class}
    )
    private DwpState dwpState;
    @CCD(label = "FTA State", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList dynamicDwpState;
    @CCD(label = "Note pad", access = {SscsRPlus6RolesJpbhhvAccess.class})
    private NotePad appealNotePad;
    @CCD(
            label = "FTA State  fe no action",
            typeOverride = FieldType.DynamicList,
            access = {DwpresponsewriterHmrcresponsewriterCrudAccess.class, SystemupdateCrudAccess.class}
    )
    private DynamicList dwpStateFeNoAction;
    @CCD(
            label = "Is a new digital case?",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_createdInGapsFrom",
            access = {SscsSuperuserCrudAccess.class, RegistrarCrudAccess.class, SystemupdateCrudAccess.class}
    )
    private String createdInGapsFrom;
    @CCD(
            label = "Date case sent to GAPS",
            typeOverride = FieldType.Date,
            access = {ClerkJudgeRegistrarSuperuserRAccess.class, SystemupdateCrudAccess.class}
    )
    private String dateCaseSentToGaps;
    @CCD(
            label = "Date Time case sent to GAPS",
            typeOverride = FieldType.DateTime,
            access = {ClerkJudgeRegistrarSuperuserRAccess.class, SystemupdateCrudAccess.class}
    )
    private String dateTimeCaseSentToGaps;
    @CCD(
            label = "Related appeal(s)",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "CaseLink",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsDwpresponsewriterHmrcresponsewriterRAccess.class}
    )
    private List<CaseLink> associatedCase;
    @CCD(label = "AT38", access = {DefaultAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class})
    private DwpResponseDocument dwpAT38Document;
    @CCD(
            label = "FTA Evidence bundle",
            access = {DefaultAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class}
    )
    private DwpResponseDocument dwpEvidenceBundleDocument;
    @CCD(
            label = "FTA Edited Evidence bundle",
            access = {DefaultAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class}
    )
    private DwpResponseDocument dwpEditedEvidenceBundleDocument;
    @CCD(
            label = "Edited evidence reason",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_editedEvidenceReason",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, IbcaresponsewriterCrudAccess.class}
    )
    private String dwpEditedEvidenceReason;
    @CCD(label = "FTA Response", access = {DefaultAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class})
    private DwpResponseDocument dwpResponseDocument;
    @CCD(
            label = "FTA Edited Response",
            access = {DefaultAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class}
    )
    private DwpResponseDocument dwpEditedResponseDocument;
    @CCD(
            label = "Supplementary response",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsRIbcaresponsewriterCrudAccess.class}
    )
    private DwpResponseDocument dwpSupplementaryResponseDoc;
    @CCD(
            label = "FTA Challenge Validity",
            access = {DefaultAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class}
    )
    private DwpResponseDocument dwpChallengeValidityDocument;
    @CCD(
            label = "Other document(s)",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsRIbcaresponsewriterCrudAccess.class}
    )
    private DwpResponseDocument dwpOtherDoc;
    @CCD(
            label = "LT203 or OM1",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class}
    )
    private DwpResponseDocument dwpLT203;
    @CCD(
            label = "Lapse letter/Review decision notice",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterCrudPlus5RolesIunvywAccess.class}
    )
    private DwpResponseDocument dwpLapseLetter;
    @CCD(
            label = "RIP 1 document",
            hint = "All documents must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document,
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsRIbcaresponsewriterCrudAccess.class}
    )
    private DocumentLink rip1Doc;
    @CCD(
            label = "Upload a RIP 1 document",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsRIbcaresponsewriterCrudAccess.class}
    )
    private String isRip1Doc;
    @CCD(label = "Upload a RIP 1 document", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo showRip1DocPage;
    @CCD(
            label = "FTA response date",
            typeOverride = FieldType.Date,
            access = {DwpresponsewriterHmrcresponsewriterCrudAccess.class, ClerkCrudAccess.class, SuperuserCruAccess.class, SystemupdateCruAccess.class}
    )
    private String dwpResponseDate;
    @CCD(
            label = "Has related appeal(s)",
            typeOverride = FieldType.YesOrNo,
            access = {SscsRClerkCruSuperuserCruSystemupdateCruAccess.class, JudgeCruAccess.class, RegistrarCrudAccess.class}
    )
    private String linkedCasesBoolean;
    @CCD(
            label = "Decision type",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_decisionTypes",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarCrudAccess.class, TeamleaderRAccess.class, SscsRAccess.class}
    )
    private String decisionType;
    @CCD(
            label = "Select who should review this case",
            typeOverride = FieldType.DynamicList,
            typeParameterOverride = "FL_selectWhoReviewsCase",
            access = {DefaultAccess.class, DwpresponsewriterRuAccess.class, HmrcresponsewriterRuAccess.class, JudgeRuAccess.class, RegistrarRuAccess.class, TeamleaderRAccess.class}
    )
    private DynamicList selectWhoReviewsCase;
    @CCD(label = "Direction type", typeOverride = FieldType.Text, access = {SscsRAccess.class})
    @Deprecated
    private DirectionType directionType;
    @CCD(
            label = "Direction type",
            typeOverride = FieldType.DynamicList,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRAccess.class}
    )
    private DynamicList directionTypeDl;
    @CCD(
            label = "Pre or post hearing?",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_prePostHearing",
            access = {SscsCrudAccess.class}
    )
    private PrePostHearing prePostHearing;
    @CCD(
            label = " ",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_confidentialityType",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsCrudAccess.class}
    )
    private String confidentialityType;
    @CCD(
            label = "FTA",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo sendDirectionNoticeToFTA;
    @CCD(
            label = "Representative",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo sendDirectionNoticeToRepresentative;
    @CCD(
            label = "Other Party Rep",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo sendDirectionNoticeToOtherPartyRep;
    @CCD(
            label = "Other Party Appointee",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo sendDirectionNoticeToOtherPartyAppointee;
    @CCD(
            label = "Other Party",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo sendDirectionNoticeToOtherParty;
    @CCD(
            label = "Joint Party",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo sendDirectionNoticeToJointParty;
    @CCD(
            label = "Appellant or Appointee",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo sendDirectionNoticeToAppellantOrAppointee;
    @CCD(
            label = "Send to all parties",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo hasOtherPartyRep;
    @CCD(
            label = "Send to all parties",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class}
    )
    private YesNo hasOtherPartyAppointee;

    @CCD(label = "What should happen next?", typeOverride = FieldType.Text, access = {SscsRAccess.class})
    @Deprecated
    private ExtensionNextEvent extensionNextEvent;
    @CCD(label = "What should happen next?", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList extensionNextEventDl;
    @CCD(
            label = "TL1 form",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, SscsRIbcaresponsewriterCrudAccess.class, JudgeRegistrarRAccess.class}
    )
    private DwpResponseDocument tl1Form;
    @CCD(
            label = "Requires Interlocutory Review?",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCruAccess.class, SuperuserCrudAccess.class}
    )
    private String isInterlocRequired;
    @CCD(
            label = "Panel",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    private Panel panel;
    @CCD(
            label = "Evidence Received",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, JudgeRAccess.class, SscsRAccess.class}
    )
    @JsonProperty("evidenceReceivedCF")
    private EvidenceReceived evidenceReceived;
    @CCD(
            label = "Urgent case",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_isUrgent",
            access = {DefaultAccess.class, JudgeRegistrarCudAccess.class, TeamleaderCrudAccess.class, SscsCrudAccess.class}
    )
    private String urgentCase;
    @CCD(label = "Urgent hearing registered", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    private String urgentHearingRegistered;
    @CCD(
            label = "Urgent hearing outcome",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_requestOutcomes",
            access = {SscsCrudAccess.class}
    )
    private String urgentHearingOutcome;
    @CCD(
            label = "Documents sent to FTA?",
            hint = "Has the direction / decision notice been sent to FTA?",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsCrudAccess.class}
    )
    private String documentSentToDwp;
    @CCD(
            label = "Due date",
            typeOverride = FieldType.Date,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsCrudAccess.class}
    )
    private String directionDueDate;
    @CCD(
            label = " ",
            typeOverride = FieldType.YesOrNo,
            access = {SscsRSuperuserCrudAccess.class, ClerkCrudAccess.class}
    )
    private YesNo shouldReadyToListBeTriggered;
    @CCD(
            label = "Reserved to (Judge):",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, CallagentRAccess.class, SscsRAccess.class}
    )
    private String reservedToJudge;
    @CCD(label = "Reserve to Judge?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo judgeReserved;
    @CCD(
            label = "Reserved to (Interloc):",
            typeOverride = FieldType.JudicialUser,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, CallagentRAccess.class, SscsRAccess.class}
    )
    private JudicialUserBase reservedToJudgeInterloc;
    @CCD(
            label = "Linked case(s)",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "CaseLink",
            access = {DefaultAccess.class, DwpresponsewriterRAccess.class, HmrcresponsewriterRAccess.class, RegistrarCrudAccess.class, TeamleaderCrudAccess.class, SscsCrudAccess.class}
    )
    private List<CaseLink> linkedCase;
    @CCD(
            label = "Is a waiver being issued for the case?",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private String isWaiverNeeded;
    @CCD(
            label = "Declaration",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_waiverDeclaration",
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private List<String> waiverDeclaration;
    @CCD(
            label = "I have identified the following requirements as being non compliant:-",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_nonCompliantRequirements",
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private List<String> waiverReason;
    @CCD(
            label = "Other reason",
            typeOverride = FieldType.TextArea,
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private String waiverReasonOther;
    @CCD(
            label = "Delegated Authority",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_clerkDelegatedAuthority",
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private List<String> clerkDelegatedAuthority;
    @CCD(
            label = "Clerk Appeal Satisfaction",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_clerkAppealSatisfactionText",
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private List<String> clerkAppealSatisfactionText;
    @CCD(
            label = "Confirmation from FTA that the appellant has been through the MRN Process",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    @JsonProperty("clerkConfirmationOfMRN")
    private String clerkConfirmationOfMrn;
    @CCD(
            label = "Other reason",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private String clerkOtherReason;
    @CCD(
            label = "Other (please specify)",
            typeOverride = FieldType.TextArea,
            access = {DefaultAccess.class, SscsJudgeCrudAccess.class}
    )
    private String clerkConfirmationOther;
    @CCD(label = "Is response required?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private String responseRequired;
    @CCD(
            label = "Time Extension Requested",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsCrudAccess.class}
    )
    private String timeExtensionRequested;
    @CCD(label = "Bundle configuration", access = {SscsCrudAccess.class})
    private String bundleConfiguration;
    @CCD(
            label = "Bundle configuration",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "Text",
            access = {SscsCrudAccess.class}
    )
    private List<MultiBundleConfig> multiBundleConfiguration;
    @CCD(label = "PCQ ID", access = {SscsCudCitizenCrudAccess.class, PcqextractorCrudAccess.class})
    private String pcqId;
    //Final decision notice fields
    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsPipCaseData pipSscsCaseData;
    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsFinalDecisionCaseData finalDecisionCaseData;
    @CCD(label = "Issue final decision date", access = {SscsCrudAccess.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate issueFinalDecisionDate;
    @CCD(
            label = "Issue interloc decision date",
            access = {SscsSuperuserCrudAccess.class, JudgeSystemupdateCrudAccess.class}
    )
    private LocalDate issueInterlocDecisionDate;
    @CCD(
            label = "Reason(s) for the case being unlistable",
            typeOverride = FieldType.TextArea,
            access = {SscsCrudAccess.class}
    )
    private String notListableProvideReasons;
    @CCD(label = "Due date", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    private String notListableDueDate;
    @CCD(
            label = "Have the requirements in the direction been fulfilled?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String updateNotListableDirectionsFulfilled;
    @CCD(
            label = "Does the case need an interlocutory review?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String updateNotListableInterlocReview;
    @CCD(
            label = "Who needs to review the case?",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_interlocNotListable",
            access = {SscsCrudAccess.class}
    )
    private String updateNotListableWhoReviewsCase;
    @CCD(
            label = "Do you need to set a new due date?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String updateNotListableSetNewDueDate;
    @CCD(label = "Due date", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    private String updateNotListableDueDate;
    @CCD(
            label = "Where should case move to?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_notListableNextState",
            access = {SscsCrudAccess.class}
    )
    private String updateNotListableWhereShouldCaseMoveTo;
    @CCD(
            label = "Is the language preference Welsh?",
            hint = "Select \"No\" for English or \"Yes\" for bilingual",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCitizenCrudAccess.class}
    )
    @JsonProperty("languagePreferenceWelsh")
    private String languagePreferenceWelsh;
    @CCD(
            label = "Elements disputed",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_elementsDisputed",
            access = {SscsCrudAccess.class}
    )
    private List<String> elementsDisputedList;
    @CCD(
            label = "General",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedGeneral;
    @CCD(
            label = "Standard allowance",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedSanctions;
    @CCD(
            label = "Standard allowance - overpayment",
            min = 0,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode"
    )
    private List<ElementDisputed> elementsDisputedOverpayment;
    @CCD(
            label = "Housing",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedHousing;
    @CCD(
            label = "Childcare",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedChildCare;
    @CCD(
            label = "Carer element",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedCare;
    @CCD(
            label = "Child element",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedChildElement;
    @CCD(
            label = "Disabled child addition",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedChildDisabled;
    @CCD(
            label = "Limited Capability for Work (WCA)",
            hint = "Select at least 1 issue code (maximum 3)",
            min = 1,
            max = 3,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "elementAndIssueCode",
            access = {SscsCrudAccess.class}
    )
    private List<ElementDisputed> elementsDisputedLimitedWork;
    @CCD(
            label = "Has this UC decision been disputed by any other party?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private String elementsDisputedIsDecisionDisputedByOthers;
    @CCD(label = "Reference of linked appeal", max = 16, access = {SscsCrudAccess.class})
    private String elementsDisputedLinkedAppealRef;
    @CCD(
            label = "Other parties",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "otherParty",
            access = {SscsCrudAccess.class}
    )
    private List<CcdValue<OtherParty>> otherParties;
    @CCD(
            label = "Other party - Unacceptable Customer Behaviour (UCB)",
            typeOverride = FieldType.YesOrNo,
            access = {SuperuserCudSystemupdateCrudAccess.class}
    )
    @JsonProperty("otherPartyUCB")
    private String otherPartyUcb;
    @CCD(label = "Child maintenance number", access = {SscsCrudAccess.class})
    private String childMaintenanceNumber;
    @CCD(
            label = "Update reasonable adjustments",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_reasonableAdjustments",
            access = {SscsCrudAccess.class}
    )
    private String reasonableAdjustmentChoice;
    @CCD(ignore = true)
    private YesNo doesOtherPersonKnowWhereYouLive;
    @CCD(ignore = true)
    private YesNo keepHomeAddressConfidential;
    @CCD(
            label = "Is there document translation work outstanding?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCudCitizenCrudAccess.class}
    )
    @JsonProperty("translationWorkOutstanding")
    private String translationWorkOutstanding;
    @CCD(
            label = "Welsh documents",
            regex = "No",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsWelshDocuments",
            access = {SscsCudAccess.class}
    )
    private List<SscsWelshDocument> sscsWelshDocuments;
    @CCD(
            label = "Welsh preview documents",
            regex = "No",
            max = 1,
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsWelshDocuments",
            access = {SscsCrudAccess.class}
    )
    private List<SscsWelshDocument> sscsWelshPreviewDocuments;
    @CCD(label = "Welsh preview next event", access = {SscsCrudAccess.class})
    private String sscsWelshPreviewNextEvent;
    @CCD(label = "Original documents", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList originalDocuments;
    @CCD(label = "Original document", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList originalNoticeDocuments;
    @CCD(label = "Document type", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList documentTypes;
    @CCD(label = "Welsh body content", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String welshBodyContent;
    @CCD(label = "English body content", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String englishBodyContent;
    @CCD(
            label = "Is a Scottish Case?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsSuperuserCrudAccess.class, CaseworkerWaTaskConfigurationRAccess.class}
    )
    private String isScottishCase;
    @CCD(label = "Reinstatement Registered", access = {SscsCudSystemupdateCrudAccess.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate reinstatementRegistered;
    @CCD(
            label = "Reinstatement Outcome",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_requestOutcomes",
            access = {SscsCudSystemupdateCrudAccess.class}
    )
    private RequestOutcome reinstatementOutcome;
    @CCD(label = "Welsh interloc next review state", access = {SscsCrudAccess.class})
    private String welshInterlocNextReviewState;
    @CCD(label = "Is case confidential?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo isConfidentialCase;
    @CCD(label = "Is Inc 5249521?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo isInc5249521;
    @CCD(label = "Confidentiality request outcome appellant", access = {SscsCrudAccess.class})
    private DatedRequestOutcome confidentialityRequestOutcomeAppellant;
    @CCD(label = "Confidentiality request outcome joint party", access = {SscsCrudAccess.class})
    private DatedRequestOutcome confidentialityRequestOutcomeJointParty;
    @CCD(
            label = "What is the outcome of the Appellant's confidentiality request?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_grantOrRefuseConfidentiality",
            access = {SscsCrudAccess.class}
    )
    private String confidentialityRequestAppellantGrantedOrRefused;
    @CCD(
            label = "What is the outcome of the Joint Party's confidentiality request?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_grantOrRefuseConfidentiality",
            access = {SscsCrudAccess.class}
    )
    private String confidentialityRequestJointPartyGrantedOrRefused;
    @CCD(label = "Bulk Scan form type", typeOverride = FieldType.Text, access = {SscsCrudAccess.class})
    @JsonProperty(value = "formType")
    private FormType formType;
    @CCD(
            label = "Is case progressing via GAPS",
            typeOverride = FieldType.YesOrNo,
            access = {SscsSuperuserCrudAccess.class}
    )
    private String isProgressingViaGaps;
    @CCD(
            label = "Is this a support group only appeal?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("supportGroupOnlyAppeal")
    private String supportGroupOnlyAppeal;
    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsEsaCaseData sscsEsaCaseData;
    @CCD(
            label = "When should FTA reassess the award?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_dwpReassessTheAward",
            access = {SscsCrudAccess.class}
    )
    private String dwpReassessTheAward;
    @CCD(
            label = "Show the final decision notice outcome page?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo showFinalDecisionNoticeSummaryOfOutcomePage;
    @CCD(label = "Show the FTA reassess award page?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo showDwpReassessAwardPage;
    @CCD(label = "Is this a WCA appeal?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    @JsonProperty("wcaAppeal")
    private YesNo wcaAppeal;
    @CCD(label = "Is appellant deceased?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo isAppellantDeceased;
    @CCD(
            label = "Case requires a Financially Qualified Panel Member (FQPM)",
            typeOverride = FieldType.YesOrNo,
            access = {SscsRSuperuserCrudAccess.class, ClerkCudAccess.class, JudgeCrudAccess.class}
    )
    private YesNo isFqpmRequired;
    @CCD(
            label = "Case requires a Medical Member",
            typeOverride = FieldType.YesOrNo,
            access = {SscsRSuperuserCrudAccess.class, ClerkCudAccess.class, JudgeCrudAccess.class}
    )
    private YesNo isMedicalMemberRequired;
    @CCD(label = "Date of appellant death", typeOverride = FieldType.Date, access = {SscsCrudAccess.class})
    @LocalDateMustNotBeInFuture(message = "Date of appellant death must not be in the future")
    private String dateOfAppellantDeath;
    @CCD(
            label = "Should the potentially harmful evidence be excluded?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsRSuperuserCrudAccess.class, JudgeSystemupdateCrudAccess.class}
    )
    @JsonProperty("phmeGranted")
    private YesNo phmeGranted;
    @CCD(label = "Appendix 12 document", access = {SscsCrudAccess.class})
    private DwpResponseDocument appendix12Doc;
    @CCD(
            label = "Case created before WA went Live",
            typeOverride = FieldType.YesOrNo,
            access = {SuperuserCruAccess.class, SystemupdateCruAccess.class, SscsRAccess.class}
    )
    private YesNo preWorkAllocation;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    @Valid
    private SscsUcCaseData sscsUcCaseData;
    @CCD(
            label = "FTA Documents",
            regex = "No",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "dwpDocuments",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, JudgeRegistrarRAccess.class}
    )
    private List<DwpDocument> dwpDocuments;
    @CCD(label = "Processing venue", access = {SscsCrudAccess.class, CaseworkerWaTaskConfigurationRAccess.class})
    private String processingVenue;
    @CCD(
            label = "Further evidence documents",
            regex = "No",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "draftSscsDocuments",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsCrPlus4RolesDetidgAccess.class}
    )
    private List<DraftSscsDocument> draftFurtherEvidenceDocuments;
    @CCD(label = "Alternative letter format", access = {SscsCrudAccess.class})
    private ReasonableAdjustments reasonableAdjustments;
    @CCD(
            label = "Are there documents that require reasonable adjustments outstanding?",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkCudSuperuserCudSystemupdateCrudAccess.class, SscsDwpresponsewriterHmrcresponsewriterRAccess.class, PanelmemberRRegistrarCrudAccess.class, JudgeRAccess.class, TeamleaderCrudAccess.class}
    )
    private YesNo reasonableAdjustmentsOutstanding;
    @CCD(label = "Show other party details", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo showOtherPartyDetails;
    @CCD(label = "Audio/Video evidence document for the bundle", access = {SscsCrudAccess.class})
    private AudioVideoEvidenceBundleDocument audioVideoEvidenceBundleDocument;

    @CCD(
            label = "Interloc Review State",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_processAudioVideoReviewState",
            access = {JudgeRegistrarCrudAccess.class, SscsRSuperuserCrudAccess.class}
    )
    @JsonProperty("processAudioVideoReviewState")
    private ProcessAudioVideoReviewState processAudioVideoReviewState;
    @CCD(label = "Enter note", typeOverride = FieldType.TextArea, access = {SscsJudgeCrudAccess.class})
    private String tempNoteDetail;
    @CCD(
            label = "Show the Work Capability Assessment page?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo showWorkCapabilityAssessmentPage;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsIndustrialInjuriesData sscsIndustrialInjuriesData;

    @CCD(
            label = "From functional test",
            typeOverride = FieldType.YesOrNo,
            gate = "!CCD_DEF_ENV:prod",
            access = {SscsCrudAccess.class}
    )
    private YesNo functionalTest;

    @CCD(
            label = "Case management location",
            access = {GSProfileRAccess.class, CaseworkerRasValidationRAccess.class, SscsCudAccess.class, CaseworkerWaTaskConfigurationRAccess.class}
    )
    private CaseManagementLocation caseManagementLocation;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsHearingRecordingCaseData sscsHearingRecordingCaseData;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private PostponementRequest postponementRequest;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private Postponement postponement;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    @Valid
    private Adjournment adjournment;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private CaseAccessManagementFields caseAccessManagementFields;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private WorkAllocationFields workAllocationFields;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SchedulingAndListingFields schedulingAndListingFields;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private CaseOutcome caseOutcome;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    private JointParty jointParty;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private WorkBasketFields workBasketFields;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private PostHearing postHearing;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private DocumentGeneration documentGeneration;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private DocumentStaging documentStaging;

    @CCD(
            label = "Ignore callback warnings",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, RegistrarCrudAccess.class, SscsCrudAccess.class}
    )
    private YesNo ignoreCallbackWarnings;

    @CCD(
            label = "Next hearing type",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class, CitizenRAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HmcHearingType hmcHearingType;

    @JsonUnwrapped
    private FtaCommunicationFields communicationFields;

    @JsonUnwrapped
    private ExtendedSscsCaseData extendedSscsCaseData;

    @JsonIgnore
    private EventDetails getLatestEvent() {
        return events != null && !events.isEmpty() ? events.getFirst().getValue() : null;
    }

    public ExtendedSscsCaseData getExtendedSscsCaseData() {
        if (extendedSscsCaseData == null) {
            extendedSscsCaseData = new ExtendedSscsCaseData();
        }
        return extendedSscsCaseData;
    }

    /**
     * Returns the "latest" hearing.
     * "Latest" in this case is defined as:
     * - the one with the highest numerical value for hearingId (the string is converted into an integer)
     * - if the hearingId is identical, the one with the highest hearingDateTime value (most recent date)
     * - if the hearingDateTime is identical, the one with the highest hearingRequested value (most recent date)
     * Remarks:
     * - No caching is applied. The sorting is applied at every call of getLatestHearing.
     * - The sorting criteria is defined in the Hearing class itself
     *
     * @return Hearing An Hearing object instance if there are hearings, null otherwise
     */
    @JsonIgnore
    public Hearing getLatestHearing() {
        if (isNotEmpty(hearings)) {
            List<Hearing> sortedHearings = new ArrayList<>(hearings);
            sortedHearings.sort(Collections.reverseOrder());
            return sortedHearings.getFirst();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isCorDecision() {
        return isCorDecision != null && isCorDecision.equalsIgnoreCase("YES");
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isSupportGroupOnlyAppeal() {
        return stringToBoolean(supportGroupOnlyAppeal);
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isThereAJointParty() {
        return isYes(getJointParty().getHasJointParty());
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isThereARepresentative() {
        return isNotEmpty(getAppeal().getRep()) && isYes(getAppeal().getRep().getHasRepresentative());
    }


    @SuppressWarnings("unused")
    @JsonIgnore
    public String getLatestEventType() {
        EventDetails latestEvent = getLatestEvent();
        return latestEvent != null ? latestEvent.getType() : null;
    }

    @JsonIgnore
    public ReissueArtifactUi getReissueArtifactUi() {
        if (reissueArtifactUi == null) {
            this.reissueArtifactUi = new ReissueArtifactUi();
        }
        return reissueArtifactUi;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isWcaAppeal() {
        return isYes(wcaAppeal);
    }

    @JsonIgnore
    public boolean isLanguagePreferenceWelsh() {
        return stringToBoolean(languagePreferenceWelsh);
    }

    @JsonIgnore
    public LanguagePreference getLanguagePreference() {
        return isLanguagePreferenceWelsh() ? LanguagePreference.WELSH : LanguagePreference.ENGLISH;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isTranslationWorkOutstanding() {
        return stringToBoolean(translationWorkOutstanding);
    }

    @JsonIgnore
    public void sortCollections() {

        if (getCorrespondence() != null) {
            getCorrespondence().sort(Collections.reverseOrder());
        }
        if (getEvents() != null) {
            getEvents().sort(Collections.reverseOrder());
        }

        if (getHearings() != null) {
            getHearings().sort(Collections.reverseOrder());
        }

        if (getEvidence() != null && getEvidence().getDocuments() != null) {
            getEvidence().getDocuments().sort(Collections.reverseOrder());
        }

        if (getSscsDocument() != null) {
            Collections.sort(getSscsDocument());
        }

        if (getScannedDocuments() != null) {
            Collections.sort(getScannedDocuments());
        }

        if (getDwpDocuments() != null) {
            Collections.sort(getDwpDocuments());
        }
    }

    public Subscriptions getSubscriptions() {
        return null != subscriptions ? subscriptions : Subscriptions.builder().build();
    }

    @JsonIgnore
    private boolean stringToBoolean(String value) {
        return "yes".equalsIgnoreCase(value);
    }

    @JsonIgnore
    public SscsDocument getLatestDocumentForDocumentType(DocumentType documentType) {

        if (getSscsDocument() != null && !getSscsDocument().isEmpty()) {
            Stream<SscsDocument> filteredStream = getSscsDocument().stream()
                    .filter(f -> documentType.getValue().equals(f.getValue().getDocumentType()));

            List<SscsDocument> filteredList = filteredStream.sorted((one, two) -> {
                if (two.getValue().getDocumentDateAdded() == null) {
                    return -1;
                }
                if (one.getValue().getDocumentDateAdded() == null) {
                    return 0;
                }
                if (one.getValue().getDocumentDateAdded().equals(two.getValue().getDocumentDateAdded())) {
                    return -1;
                }
                return -1 * one.getValue().getDocumentDateAdded().compareTo(two.getValue().getDocumentDateAdded());
            }).toList();

            if (!filteredList.isEmpty()) {
                return filteredList.getFirst();
            }
        }
        return null;
    }

    @JsonIgnore
    public Optional<SscsWelshDocument> getLatestWelshDocumentForDocumentType(DocumentType documentType) {
        return ofNullable(getSscsWelshDocuments()).stream().flatMap(Collection::stream)
                .filter(wd -> wd.getValue().getDocumentType().equals(documentType.getValue()))
                .sorted()
                .findFirst();
    }

    @JsonIgnore
    public void updateTranslationWorkOutstandingFlag() {
        List<SscsDocumentTranslationStatus> translationStatuses = Arrays.asList(SscsDocumentTranslationStatus.TRANSLATION_REQUESTED, SscsDocumentTranslationStatus.TRANSLATION_REQUIRED);
        boolean noSscsDocumentsWithTranslation = emptyIfNull(getSscsDocument()).stream().noneMatch(sd -> translationStatuses.contains(sd.getValue().getDocumentTranslationStatus()));
        boolean noDwpDocumentsWithTranslation = emptyIfNull(getDwpDocuments()).stream().noneMatch(dd -> translationStatuses.contains(dd.getValue().getDocumentTranslationStatus()));

        if (noSscsDocumentsWithTranslation && noDwpDocumentsWithTranslation) {
            this.translationWorkOutstanding = "No";
        } else {
            this.translationWorkOutstanding = "Yes";
        }
    }

    @JsonIgnore
    public void updateReasonableAdjustmentsOutstanding() {
        List<Correspondence> combinedLetters = new ArrayList<>();

        if (getReasonableAdjustmentsLetters() != null) {
            buildLetterList(combinedLetters, getReasonableAdjustmentsLetters().getAppellant());
            buildLetterList(combinedLetters, getReasonableAdjustmentsLetters().getAppointee());
            buildLetterList(combinedLetters, getReasonableAdjustmentsLetters().getRepresentative());
            buildLetterList(combinedLetters, getReasonableAdjustmentsLetters().getJointParty());
            buildLetterList(combinedLetters, getReasonableAdjustmentsLetters().getOtherParty());
        }

        if (combinedLetters.stream().allMatch(ra -> ReasonableAdjustmentStatus.ACTIONED.equals(ra.getValue().getReasonableAdjustmentStatus()))) {
            this.reasonableAdjustmentsOutstanding = NO;
        } else {
            this.reasonableAdjustmentsOutstanding = YES;
        }
    }

    @JsonIgnore
    public List<Correspondence> buildLetterList(List<Correspondence> combinedLetters, List<Correspondence> correspondence) {
        if (correspondence != null) {
            combinedLetters.addAll(correspondence);
        }
        return combinedLetters;
    }

    @JsonIgnore
    public SscsEsaCaseData getSscsEsaCaseData() {
        if (sscsEsaCaseData == null) {
            this.sscsEsaCaseData = new SscsEsaCaseData();
        }
        return sscsEsaCaseData;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SscsUcCaseData getSscsUcCaseData() {
        if (sscsUcCaseData == null) {
            this.sscsUcCaseData = new SscsUcCaseData();
        }
        return sscsUcCaseData;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SscsPipCaseData getSscsPipCaseData() {
        if (pipSscsCaseData == null) {
            this.pipSscsCaseData = new SscsPipCaseData();
        }
        return pipSscsCaseData;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SscsFinalDecisionCaseData getSscsFinalDecisionCaseData() {
        if (finalDecisionCaseData == null) {
            this.finalDecisionCaseData = new SscsFinalDecisionCaseData();
        }
        return finalDecisionCaseData;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SscsHearingRecordingCaseData getSscsHearingRecordingCaseData() {
        if (sscsHearingRecordingCaseData == null) {
            this.sscsHearingRecordingCaseData = new SscsHearingRecordingCaseData();
        }
        return sscsHearingRecordingCaseData;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public PostponementRequest getPostponementRequest() {
        if (postponementRequest == null) {
            this.postponementRequest = new PostponementRequest();
        }
        return postponementRequest;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public Postponement getPostponement() {
        if (postponement == null) {
            this.postponement = new Postponement();
        }
        return postponement;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public Adjournment getAdjournment() {
        if (adjournment == null) {
            this.adjournment = new Adjournment();
        }
        return adjournment;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public CaseAccessManagementFields getCaseAccessManagementFields() {
        if (caseAccessManagementFields == null) {
            this.caseAccessManagementFields = new CaseAccessManagementFields();
        }
        return caseAccessManagementFields;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public WorkAllocationFields getWorkAllocationFields() {
        if (workAllocationFields == null) {
            this.workAllocationFields = new WorkAllocationFields();
        }
        return workAllocationFields;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SchedulingAndListingFields getSchedulingAndListingFields() {
        if (schedulingAndListingFields == null) {
            this.schedulingAndListingFields = new SchedulingAndListingFields();
        }
        return schedulingAndListingFields;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public CaseOutcome getCaseOutcome() {
        if (caseOutcome == null) {
            this.caseOutcome = new CaseOutcome();
        }
        return caseOutcome;
    }

    @JsonIgnore
    public JointParty getJointParty() {
        if (isNull(jointParty)) {
            this.jointParty = new JointParty();
        }
        return jointParty;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public WorkBasketFields getWorkBasketFields() {
        if (isNull(workBasketFields)) {
            workBasketFields = new WorkBasketFields();
        }
        return workBasketFields;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public PostHearing getPostHearing() {
        if (isNull(postHearing)) {
            postHearing = new PostHearing();
        }
        return postHearing;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public DocumentGeneration getDocumentGeneration() {
        if (isNull(documentGeneration)) {
            documentGeneration = new DocumentGeneration();
        }
        return documentGeneration;
    }

    @JsonIgnore
    public DocumentStaging getDocumentStaging() {
        if (isNull(documentStaging)) {
            documentStaging = new DocumentStaging();
        }
        return documentStaging;
    }

    @JsonIgnore
    public Optional<Benefit> getBenefitType() {
        if (appeal != null && appeal.getBenefitType() != null && appeal.getBenefitType().getCode() != null) {
            return findBenefitByShortName(appeal.getBenefitType().getCode().toUpperCase());
        } else {
            return Optional.empty();
        }
    }

    @JsonIgnore
    public Optional<Appellant> getAppellant() {
        return Optional.ofNullable(getAppeal())
                .map(Appeal::getAppellant);
    }

    @JsonIgnore
    public boolean isIbcCase() {
        if (INFECTED_BLOOD_COMPENSATION.getBenefitCode().equals(benefitCode)) {
            return true;
        }
        
        return Optional.of(this)
                .map(SscsCaseData::getAppeal)
                .map(Appeal::getBenefitType)
                .map(BenefitType::getDescriptionSelection)
                .map(DynamicList::getValue)
                .map(DynamicListItem::getCode)
                .filter(ObjectUtils::isNotEmpty)
                .map(INFECTED_BLOOD_COMPENSATION.getBenefitCode()::equals)
                .orElseGet(() -> getBenefitType().map(INFECTED_BLOOD_COMPENSATION::equals).orElse(false));
    }

    @JsonIgnore
    public Optional<LocalDateTime> getDateTimeSentToGaps() {

        Optional<LocalDateTime> ldt = Optional.empty();

        try {
            if (this.dateTimeCaseSentToGaps != null) {
                ldt = Optional.of(LocalDateTime.parse(this.dateTimeCaseSentToGaps));
            }
        } catch (DateTimeParseException e) {
            ldt = Optional.empty();
        }
        return ldt;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SscsIndustrialInjuriesData getSscsIndustrialInjuriesData() {
        if (sscsIndustrialInjuriesData == null) {
            this.sscsIndustrialInjuriesData = new SscsIndustrialInjuriesData();
        }
        return sscsIndustrialInjuriesData;
    }

    public void setDwpState(DwpState newDwpState) {
        List<DwpState> statesToRefuse = List.of(DwpState.HEARING_POSTPONED, DwpState.HEARING_DATE_ISSUED);

        if (!(FINAL_DECISION_ISSUED.equals(dwpState) && statesToRefuse.contains(newDwpState))) {
            this.dwpState = newDwpState;
        }
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public PoDetails getPresentingOfficersDetails() {
        if (presentingOfficersDetails == null) {
            this.presentingOfficersDetails = new PoDetails();
        }
        return presentingOfficersDetails;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public void clearPoDetails() {
        setPoAttendanceConfirmed(NO);
        setPresentingOfficersDetails(null);
        setPresentingOfficersHearingLink(null);
    }

    @JsonIgnore
    public YesNo showConfidentialityTab() {
        final YesNo showConfidentialityTab = this.getExtendedSscsCaseData().getShowConfidentialityTab();
        return isNull(showConfidentialityTab) || showConfidentialityTab == YesNo.NO ? NO : YES;
    }

    public boolean isBenefitType(Benefit benefitType) {
        return getBenefitType().filter(benefitType::equals).isPresent();
    }

    @JsonIgnore
    public List<String> getIssueCodesForAllElementsDisputed() {
        List<ElementDisputed> elementDisputed = new ArrayList<>();
        elementDisputed.addAll(requireNonNullElse(elementsDisputedGeneral, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedSanctions, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedOverpayment, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedHousing, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedChildCare, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedCare, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedChildElement, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedChildDisabled, List.of()));
        elementDisputed.addAll(requireNonNullElse(elementsDisputedLimitedWork, List.of()));

        return elementDisputed.stream()
                .map(ElementDisputed::getValue)
                .map(ElementDisputedDetails::getIssueCode)
                .sorted()
                .collect(toList());
    }

    @JsonIgnore
    public void clearNotificationFields() {
        setGenericLetterText("");
        setSendToAllParties(null);
        setSendToApellant(null);
        setSendToJointParty(null);
        setSendToOtherParties(null);
        setSendToRepresentative(null);
        setAddDocuments(null);

        if (nonNull(documentSelection)) {
            documentSelection.clear();
        }

        if (nonNull(otherPartySelection)) {
            otherPartySelection.clear();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "confidentialityTab", access = READ_ONLY)
    public String getConfidentialityTab() {
        return buildConfidentialityTab(getBenefitType().orElse(null), appeal, otherParties);
    }

    @JsonIgnore
    public Optional<YesNoUndetermined> getAppellantConfidentiality() {
        return getAppellant().map(Party::getConfidentialityRequirement);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "hasUndeterminedPartyConfidentiality", access = READ_ONLY)
    public YesNo hasUndeterminedPartyConfidentiality() {

        if (isNull(getAppeal()) || !isValidBenefitTypeForConfidentiality(getAppeal().getBenefitType()) || (isBenefitType(UC)
            && isEmpty(getOtherParties()))) {
            return null;
        }

        final YesNoUndetermined appellantConfidentialitySelection = getAppellant()
            .map(Party::getConfidentialityRequirement)
            .orElse(null);

        boolean anyPartyHasUndeterminedConfidentiality = emptyIfNull(getOtherParties())
            .stream()
            .map(op -> op.getValue().getConfidentialityRequirement())
            .anyMatch(conf -> conf == null || conf == YesNoUndetermined.UNDETERMINED);

        boolean appellantHasUndeterminedConfidentiality = appellantConfidentialitySelection == null
            || appellantConfidentialitySelection == YesNoUndetermined.UNDETERMINED;

        return (appellantHasUndeterminedConfidentiality || anyPartyHasUndeterminedConfidentiality) ? YES : NO;
    }

}

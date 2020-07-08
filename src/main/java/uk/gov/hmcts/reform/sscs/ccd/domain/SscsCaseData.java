package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsCaseData implements CaseData {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String ccdCaseId;

    private State state;
    private String caseReference;
    private String caseCreated;
    private InfoRequests infoRequests;
    private String region;
    private Appeal appeal;
    private List<Hearing> hearings;
    private Evidence evidence;
    private List<DwpTimeExtension> dwpTimeExtension;
    private List<Event> events;
    @Getter(AccessLevel.NONE)
    private Subscriptions subscriptions;
    private RegionalProcessingCenter regionalProcessingCenter;
    private List<Bundle> caseBundles;
    private List<SscsDocument> sscsDocument;
    private List<SscsDocument> draftSscsDocument;
    private List<SscsFurtherEvidenceDoc> draftSscsFurtherEvidenceDocument;
    private List<CorDocument> corDocument;
    private List<CorDocument> draftCorDocument;
    private SscsInterlocDecisionDocument sscsInterlocDecisionDocument;
    private SscsInterlocDirectionDocument sscsInterlocDirectionDocument;
    private SscsStrikeOutDocument sscsStrikeOutDocument;
    private String generatedNino;
    private String generatedSurname;
    private String generatedEmail;
    private String generatedMobile;
    @JsonProperty("generatedDOB")
    private String generatedDob;
    private DirectionResponse directionResponse;
    private String evidencePresent;
    private String bulkScanCaseReference;
    private String decisionNotes;
    private String isCorDecision;
    private String relistingReason;
    private String dateSentToDwp;
    private String interlocReviewState;
    private String hmctsDwpState;
    private String dwpFurtherEvidenceStates;
    private DynamicList originalSender;
    private DynamicList furtherEvidenceAction;
    private List<ScannedDocument> scannedDocuments;
    private String informationFromAppellant;
    private String outcome;
    private String evidenceHandled;
    private String assignedToJudge;
    private String assignedToDisabilityMember;
    private String assignedToMedicalMember;
    private DynamicList reissueFurtherEvidenceDocument;
    private String resendToAppellant;
    private String resendToRepresentative;
    private String resendToDwp;
    private String caseCode;
    private String benefitCode;
    private String issueCode;
    private DynamicList dwpOriginatingOffice;
    private DynamicList dwpPresentingOffice;
    private String dwpIsOfficerAttending;
    @JsonProperty("dwpUCB")
    private String dwpUcb;
    @JsonProperty("dwpPHME")
    private String dwpPhme;
    private String dwpComplexAppeal;
    private String dwpFurtherInfo;
    private List<Correspondence> correspondence;
    private String interlocReferralDate;
    private String interlocReferralReason;
    private String dwpRegionalCentre;
    private String generateNotice;
    private DocumentLink previewDocument;
    private String bodyContent;
    private String signedBy;
    private String signedRole;
    private LocalDate dateAdded;
    private List<SscsInterlocDirectionDocuments> historicSscsInterlocDirectionDocs;
    private String dwpState;
    private NotePad appealNotePad;
    private DynamicList dwpStateFeNoAction;
    private String createdInGapsFrom;
    private String dateCaseSentToGaps;
    private List<CaseLink> associatedCase;
    private DwpResponseDocument dwpAT38Document;
    private DwpResponseDocument dwpEvidenceBundleDocument;
    private DwpResponseDocument dwpResponseDocument;
    private DwpResponseDocument dwpSupplementaryResponseDoc;
    private DwpResponseDocument dwpOtherDoc;
    private DwpLT203 dwpLT203;
    private DwpLapseLetter dwpLapseLetter;
    private String dwpResponseDate;
    private String linkedCasesBoolean;
    private String decisionType;
    private DynamicList selectWhoReviewsCase;
    @Deprecated
    private DirectionType directionType;
    private DynamicList directionTypeDl;
    @Deprecated
    private ExtensionNextEvent extensionNextEvent;
    private DynamicList extensionNextEventDl;
    private DwpResponseDocument tl1Form;
    private String isInterlocRequired;
    private Panel panel;
    @JsonProperty("evidenceReceivedCF")
    private EvidenceReceived evidenceReceived;
    private String urgentCase;
    private String documentSentToDwp;
    private String directionDueDate;
    private String reservedToJudge;
    private List<CaseLink> linkedCase;
    private String isWaiverNeeded;
    private List<String> waiverDeclaration;
    private List<String> waiverReason;
    private String waiverReasonOther;
    private List<String> clerkDelegatedAuthority;
    private List<String> clerkAppealSatisfactionText;
    private List<String> pipWriteFinalDecisionDailyLivingActivitiesQuestion;
    private List<String> pipWriteFinalDecisionMobilityActivitiesQuestion;
    @JsonProperty("clerkConfirmationOfMRN")
    private String clerkConfirmationOfMrn;
    private String clerkOtherReason;
    private String clerkConfirmationOther;
    private String responseRequired;
    private String timeExtensionRequested;
    private String bundleConfiguration;
    private String pcqId;
    //Final decision notice fields
    private String writeFinalDecisionIsDescriptorFlow;
    private String writeFinalDecisionGenerateNotice;
    private String writeFinalDecisionAllowedOrRefused;
    private String writeFinalDecisionTypeOfHearing;
    private String writeFinalDecisionPresentingOfficerAttendedQuestion;
    private String writeFinalDecisionAppellantAttendedQuestion;
    private String pipWriteFinalDecisionDailyLivingQuestion;
    @JsonProperty("pipWriteFinalDecisionComparedToDWPDailyLivingQuestion")
    private String pipWriteFinalDecisionComparedToDwpDailyLivingQuestion;
    private String pipWriteFinalDecisionMobilityQuestion;
    @JsonProperty("pipWriteFinalDecisionComparedToDWPMobilityQuestion")
    private String pipWriteFinalDecisionComparedToDwpMobilityQuestion;
    private String writeFinalDecisionStartDate;
    private String writeFinalDecisionEndDateType;
    private String writeFinalDecisionEndDate;
    private String writeFinalDecisionDisabilityQualifiedPanelMemberName;
    private String writeFinalDecisionMedicallyQualifiedPanelMemberName;
    private String writeFinalDecisionDateOfDecision;
    private String writeFinalDecisionDetailsOfDecision;
    private List<CollectionItem<String>> writeFinalDecisionReasons;
    private String pipWriteFinalDecisionPreparingFoodQuestion;
    private String pipWriteFinalDecisionTakingNutritionQuestion;
    private String pipWriteFinalDecisionManagingTherapyQuestion;
    private String pipWriteFinalDecisionWashAndBatheQuestion;
    private String pipWriteFinalDecisionManagingToiletNeedsQuestion;
    private String pipWriteFinalDecisionDressingAndUndressingQuestion;
    private String pipWriteFinalDecisionCommunicatingQuestion;
    private String pipWriteFinalDecisionReadingUnderstandingQuestion;
    private String pipWriteFinalDecisionEngagingWithOthersQuestion;
    private String pipWriteFinalDecisionBudgetingDecisionsQuestion;
    private String pipWriteFinalDecisionPlanningAndFollowingQuestion;
    private String pipWriteFinalDecisionMovingAroundQuestion;
    private String writeFinalDecisionPageSectionReference;
    private String writeFinalDecisionAnythingElse;
    private DocumentLink writeFinalDecisionPreviewDocument;
    private String writeFinalDecisionGeneratedDate;
    private String adjournCaseGenerateNotice;
    private String adjournCaseTypeOfHearing;
    private String adjournCaseCanCaseBeListedRightAway;
    private String adjournCaseAreDirectionsBeingMadeToParties;
    private String adjournCaseDirectionsDueDateDaysOffset;
    private String adjournCaseDirectionsDueDate;
    private String adjournCaseTypeOfNextHearing;
    private String adjournCaseNextHearingVenue;
    private String adjournCaseNextHearingVenueSelected;
    private String adjournCasePanelMembersExcluded;
    private String adjournCaseDisabilityQualifiedPanelMemberName;
    private String adjournCaseMedicallyQualifiedPanelMemberName;
    private String adjournCaseOtherPanelMemberName;
    private String adjournCaseNextHearingDateType;
    private String adjournCaseNextHearingDateOrPeriod;
    private String adjournCaseNextHearingDateOrTime;
    private String adjournCaseNextHearingFirstAvailableDateAfterDate;
    private String adjournCaseNextHearingFirstAvailableDateAfterPeriod;
    private String adjournCaseNextHearingSpecificDate;
    private String adjournCaseNextHearingSpecificTime;

    @JsonCreator
    public SscsCaseData(@JsonProperty(value = "ccdCaseId", access = JsonProperty.Access.WRITE_ONLY) String ccdCaseId,
                        @JsonProperty(value = "state") State state,
                        @JsonProperty("caseReference") String caseReference,
                        @JsonProperty("caseCreated") String caseCreated,
                        @JsonProperty("infoRequests") InfoRequests infoRequests,
                        @JsonProperty("region") String region,
                        @JsonProperty("appeal") Appeal appeal,
                        @JsonProperty("hearings") List<Hearing> hearings,
                        @JsonProperty("evidence") Evidence evidence,
                        @JsonProperty("dwpTimeExtension") List<DwpTimeExtension> dwpTimeExtension,
                        @JsonProperty("events") List<Event> events,
                        @JsonProperty("subscriptions") Subscriptions subscriptions,
                        @JsonProperty("regionalProcessingCenter") RegionalProcessingCenter regionalProcessingCenter,
                        @JsonProperty("caseBundles") List<Bundle> caseBundles,
                        @JsonProperty("sscsDocument") List<SscsDocument> sscsDocument,
                        @JsonProperty("draftSscsDocument") List<SscsDocument> draftSscsDocument,
                        @JsonProperty("draftSscsFurtherEvidenceDocument") List<SscsFurtherEvidenceDoc> draftSscsFurtherEvidenceDocument,
                        @JsonProperty("corDocument") List<CorDocument> corDocument,
                        @JsonProperty("draftCorDocument") List<CorDocument> draftCorDocument,
                        @JsonProperty("sscsInterlocDecisionDocument") SscsInterlocDecisionDocument sscsInterlocDecisionDocument,
                        @JsonProperty("sscsInterlocDirectionDocument") SscsInterlocDirectionDocument sscsInterlocDirectionDocument,
                        @JsonProperty("sscsStrikeOutDocument") SscsStrikeOutDocument sscsStrikeOutDocument,
                        @JsonProperty("generatedNino") String generatedNino,
                        @JsonProperty("generatedSurname") String generatedSurname,
                        @JsonProperty("generatedEmail") String generatedEmail,
                        @JsonProperty("generatedMobile") String generatedMobile,
                        @JsonProperty("generatedDOB") String generatedDob,
                        @JsonProperty("directionResponse") DirectionResponse directionResponse,
                        @JsonProperty("evidencePresent") String evidencePresent,
                        @JsonProperty("bulkScanCaseReference") String bulkScanCaseReference,
                        @JsonProperty("decisionNotes") String decisionNotes,
                        @JsonProperty("isCorDecision") String isCorDecision,
                        @JsonProperty("relistingReason") String relistingReason,
                        @JsonProperty("dateSentToDwp") String dateSentToDwp,
                        @JsonProperty("interlocReviewState") String interlocReviewState,
                        @JsonProperty("hmctsDwpState") String hmctsDwpState,
                        @JsonProperty("dwpFurtherEvidenceStates") String dwpFurtherEvidenceStates,
                        @JsonProperty("originalSender") DynamicList originalSender,
                        @JsonProperty("furtherEvidenceAction") DynamicList furtherEvidenceAction,
                        @JsonProperty("scannedDocuments") List<ScannedDocument> scannedDocuments,
                        @JsonProperty("informationFromAppellant") String informationFromAppellant,
                        @JsonProperty("outcome") String outcome,
                        @JsonProperty("evidenceHandled") String evidenceHandled,
                        @JsonProperty("assignedToJudge") String assignedToJudge,
                        @JsonProperty("assignedToDisabilityMember") String assignedToDisabilityMember,
                        @JsonProperty("assignedToMedicalMember") String assignedToMedicalMember,
                        @JsonProperty("reissueFurtherEvidenceDocument") DynamicList reissueFurtherEvidenceDocument,
                        @JsonProperty("resendToAppellant") String resendToAppellant,
                        @JsonProperty("resendToRepresentative") String resendToRepresentative,
                        @JsonProperty("resendToDwp") String resendToDwp,
                        @JsonProperty("caseCode") String caseCode,
                        @JsonProperty("benefitCode") String benefitCode,
                        @JsonProperty("issueCode") String issueCode,
                        @JsonProperty("dwpOriginatingOffice") DynamicList dwpOriginatingOffice,
                        @JsonProperty("dwpPresentingOffice") DynamicList dwpPresentingOffice,
                        @JsonProperty("dwpIsOfficerAttending") String dwpIsOfficerAttending,
                        @JsonProperty("dwpUCB") String dwpUcb,
                        @JsonProperty("dwpPHME") String dwpPhme,
                        @JsonProperty("dwpComplexAppeal") String dwpComplexAppeal,
                        @JsonProperty("dwpFurtherInfo") String dwpFurtherInfo,
                        @JsonProperty("correspondence") List<Correspondence> correspondence,
                        @JsonProperty("interlocReferralDate") String interlocReferralDate,
                        @JsonProperty("interlocReferralReason") String interlocReferralReason,
                        @JsonProperty("dwpRegionalCentre") String dwpRegionalCentre,
                        @JsonProperty("generateNotice") String generateNotice,
                        @JsonProperty("previewDocument") DocumentLink previewDocument,
                        @JsonProperty("bodyContent") String bodyContent,
                        @JsonProperty("signedBy") String signedBy,
                        @JsonProperty("signedRole") String signedRole,
                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                            @JsonSerialize(using = LocalDateSerializer.class)
                            @JsonProperty("dateAdded") LocalDate dateAdded,
                        @JsonProperty("historicSscsInterlocDirectionDocs") List<SscsInterlocDirectionDocuments> historicSscsInterlocDirectionDocs,
                        @JsonProperty("dwpState") String dwpState,
                        @JsonProperty("appealNotePad") NotePad appealNotePad,
                        @JsonProperty("dwpStateFeNoAction") DynamicList dwpStateFeNoAction,
                        @JsonProperty("createdInGapsFrom") String createdInGapsFrom,
                        @JsonProperty("dateCaseSentToGaps") String dateCaseSentToGaps,
                        @JsonProperty("associatedCase") List<CaseLink> associatedCase,
                        @JsonProperty("dwpAT38Document") DwpResponseDocument dwpAT38Document,
                        @JsonProperty("dwpEvidenceBundleDocument") DwpResponseDocument dwpEvidenceBundleDocument,
                        @JsonProperty("dwpResponseDocument") DwpResponseDocument dwpResponseDocument,
                        @JsonProperty("dwpSupplementaryResponseDoc") DwpResponseDocument dwpSupplementaryResponseDoc,
                        @JsonProperty("dwpOtherDoc") DwpResponseDocument dwpOtherDoc,
                        @JsonProperty("dwpLT203") DwpLT203 dwpLT203,
                        @JsonProperty("dwpLapseLetter") DwpLapseLetter dwpLapseLetter,
                        @JsonProperty("dwpResponseDate") String dwpResponseDate,
                        @JsonProperty("linkedCasesBoolean") String linkedCasesBoolean,
                        @JsonProperty("decisionType") String decisionType,
                        @JsonProperty("selectWhoReviewsCase") DynamicList selectWhoReviewsCase,
                        @JsonProperty("directionType") DirectionType directionType,
                        @JsonProperty("directionTypeDl") DynamicList directionTypeDl,
                        @JsonProperty("extensionNextEvent") ExtensionNextEvent extensionNextEvent,
                        @JsonProperty("extensionNextEventDl") DynamicList extensionNextEventDl,
                        @JsonProperty("tl1Form") DwpResponseDocument tl1Form,
                        @JsonProperty("isInterlocRequired") String isInterlocRequired,
                        @JsonProperty("panel") Panel panel,
                        @JsonProperty("evidenceReceivedCF") EvidenceReceived evidenceReceived,
                        @JsonProperty("urgentCase") String urgentCase,
                        @JsonProperty("documentSentToDwp") String documentSentToDwp,
                        @JsonProperty("directionDueDate") String directionDueDate,
                        @JsonProperty("reservedToJudge") String reservedToJudge,
                        @JsonProperty("linkedCase") List<CaseLink> linkedCase,
                        @JsonProperty("isWaiverNeeded") String isWaiverNeeded,
                        @JsonProperty("waiverDeclaration") List<String> waiverDeclaration,
                        @JsonProperty("waiverReason") List<String> waiverReason,
                        @JsonProperty("waiverReasonOther") String waiverReasonOther,
                        @JsonProperty("clerkDelegatedAuthority") List<String> clerkDelegatedAuthority,
                        @JsonProperty("clerkAppealSatisfactionText") List<String> clerkAppealSatisfactionText,
                        @JsonProperty("pipWriteFinalDecisionDailyLivingActivitiesQuestion") List<String> pipWriteFinalDecisionDailyLivingActivitiesQuestion,
                        @JsonProperty("pipWriteFinalDecisionMobilityActivitiesQuestion") List<String> pipWriteFinalDecisionMobilityActivitiesQuestion,
                        @JsonProperty("clerkConfirmationOfMRN") String clerkConfirmationOfMrn,
                        @JsonProperty("clerkOtherReason") String clerkOtherReason,
                        @JsonProperty("clerkConfirmationOther") String clerkConfirmationOther,
                        @JsonProperty("responseRequired") String responseRequired,
                        @JsonProperty("timeExtensionRequested") String timeExtensionRequested,
                        @JsonProperty("bundleConfiguration") String bundleConfiguration,
                        @JsonProperty("pcqId") String pcqId,
                        @JsonProperty("writeFinalDecisionIsDescriptorFlow") String writeFinalDecisionIsDescriptorFlow,
                        @JsonProperty("writeFinalDecisionGenerateNotice") String writeFinalDecisionGenerateNotice,
                        @JsonProperty("writeFinalDecisionAllowedOrRefused") String writeFinalDecisionAllowedOrRefused,
                        @JsonProperty("writeFinalDecisionTypeOfHearing") String writeFinalDecisionTypeOfHearing,
                        @JsonProperty("writeFinalDecisionPresentingOfficerAttendedQuestion") String writeFinalDecisionPresentingOfficerAttendedQuestion,
                        @JsonProperty("writeFinalDecisionAppellantAttendedQuestion") String writeFinalDecisionAppellantAttendedQuestion,
                        @JsonProperty("pipWriteFinalDecisionDailyLivingQuestion") String pipWriteFinalDecisionDailyLivingQuestion,
                        @JsonProperty("pipWriteFinalDecisionComparedToDWPDailyLivingQuestion") String pipWriteFinalDecisionComparedToDwpDailyLivingQuestion,
                        @JsonProperty("pipWriteFinalDecisionMobilityQuestion") String pipWriteFinalDecisionMobilityQuestion,
                        @JsonProperty("pipWriteFinalDecisionComparedToDWPMobilityQuestion") String pipWriteFinalDecisionComparedToDwpMobilityQuestion,
                        @JsonProperty("writeFinalDecisionStartDate") String writeFinalDecisionStartDate,
                        @JsonProperty("writeFinalDecisionEndDateType") String writeFinalDecisionEndDateType,
                        @JsonProperty("writeFinalDecisionEndDate") String writeFinalDecisionEndDate,
                        @JsonProperty("writeFinalDecisionDisabilityQualifiedPanelMemberName") String writeFinalDecisionDisabilityQualifiedPanelMemberName,
                        @JsonProperty("writeFinalDecisionMedicallyQualifiedPanelMemberName") String writeFinalDecisionMedicallyQualifiedPanelMemberName,
                        @JsonProperty("writeFinalDecisionDateOfDecision") String writeFinalDecisionDateOfDecision,
                        @JsonProperty("writeFinalDecisionDetailsOfDecision") String writeFinalDecisionDetailsOfDecision,
                        @JsonProperty("writeFinalDecisionReasons") List<CollectionItem<String>> writeFinalDecisionReasons,
                        @JsonProperty("pipWriteFinalDecisionPreparingFoodQuestion") String pipWriteFinalDecisionPreparingFoodQuestion,
                        @JsonProperty("pipWriteFinalDecisionTakingNutritionQuestion") String pipWriteFinalDecisionTakingNutritionQuestion,
                        @JsonProperty("pipWriteFinalDecisionManagingTherapyQuestion") String pipWriteFinalDecisionManagingTherapyQuestion,
                        @JsonProperty("pipWriteFinalDecisionWashAndBatheQuestion") String pipWriteFinalDecisionWashAndBatheQuestion,
                        @JsonProperty("pipWriteFinalDecisionManagingToiletNeedsQuestion") String pipWriteFinalDecisionManagingToiletNeedsQuestion,
                        @JsonProperty("pipWriteFinalDecisionDressingAndUndressingQuestion") String pipWriteFinalDecisionDressingAndUndressingQuestion,
                        @JsonProperty("pipWriteFinalDecisionCommunicatingQuestion") String pipWriteFinalDecisionCommunicatingQuestion,
                        @JsonProperty("pipWriteFinalDecisionReadingUnderstandingQuestion") String pipWriteFinalDecisionReadingUnderstandingQuestion,
                        @JsonProperty("pipWriteFinalDecisionEngagingWithOthersQuestion") String pipWriteFinalDecisionEngagingWithOthersQuestion,
                        @JsonProperty("pipWriteFinalDecisionBudgetingDecisionsQuestion") String pipWriteFinalDecisionBudgetingDecisionsQuestion,
                        @JsonProperty("pipWriteFinalDecisionPlanningAndFollowingQuestion") String pipWriteFinalDecisionPlanningAndFollowingQuestion,
                        @JsonProperty("pipWriteFinalDecisionMovingAroundQuestion") String pipWriteFinalDecisionMovingAroundQuestion,
                        @JsonProperty("writeFinalDecisionPageSectionReference") String writeFinalDecisionPageSectionReference,
                        @JsonProperty("writeFinalDecisionAnythingElse") String writeFinalDecisionAnythingElse,
                        @JsonProperty("writeFinalDecisionPreviewDocument") DocumentLink writeFinalDecisionPreviewDocument,
                        @JsonProperty("writeFinalDecisionGeneratedDate") String writeFinalDecisionGeneratedDate,
                        @JsonProperty("adjournCaseGenerateNotice") String adjournCaseGenerateNotice,
                        @JsonProperty("adjournCaseTypeOfHearing") String adjournCaseTypeOfHearing,
                        @JsonProperty("adjournCaseCanCaseBeListedRightAway") String adjournCaseCanCaseBeListedRightAway,
                        @JsonProperty("adjournCaseAreDirectionsBeingMadeToParties") String adjournCaseAreDirectionsBeingMadeToParties,
                        @JsonProperty("adjournCaseDirectionsDueDateDaysOffset") String adjournCaseDirectionsDueDateDaysOffset,
                        @JsonProperty("adjournCaseDirectionsDueDate") String adjournCaseDirectionsDueDate,
                        @JsonProperty("adjournCaseTypeOfNextHearing") String adjournCaseTypeOfNextHearing,
                        @JsonProperty("adjournCaseNextHearingVenue") String adjournCaseNextHearingVenue,
                        @JsonProperty("adjournCaseNextHearingVenueSelected") String adjournCaseNextHearingVenueSelected,
                        @JsonProperty("adjournCasePanelMembersExcluded") String adjournCasePanelMembersExcluded,
                        @JsonProperty("adjournCaseDisabilityQualifiedPanelMemberName") String adjournCaseDisabilityQualifiedPanelMemberName,
                        @JsonProperty("adjournCaseMedicallyQualifiedPanelMemberName") String adjournCaseMedicallyQualifiedPanelMemberName,
                        @JsonProperty("adjournCaseOtherPanelMemberName") String adjournCaseOtherPanelMemberName,
                        @JsonProperty("adjournCaseNextHearingDateType") String adjournCaseNextHearingDateType,
                        @JsonProperty("adjournCaseNextHearingDateOrPeriod") String adjournCaseNextHearingDateOrPeriod,
                        @JsonProperty("adjournCaseNextHearingDateOrTime") String adjournCaseNextHearingDateOrTime,
                        @JsonProperty("adjournCaseNextHearingFirstAvailableDateAfterDate")  String adjournCaseNextHearingFirstAvailableDateAfterDate,
                        @JsonProperty("adjournCaseNextHearingFirstAvailableDateAfterPeriod") String adjournCaseNextHearingFirstAvailableDateAfterPeriod,
                        @JsonProperty("adjournCaseNextHearingSpecificDate") String adjournCaseNextHearingSpecificDate,
                        @JsonProperty("adjournCaseNextHearingSpecificTime") String adjournCaseNextHearingSpecificTime) {
        this.ccdCaseId = ccdCaseId;
        this.state = state;
        this.caseReference = caseReference;
        this.caseCreated = caseCreated;
        this.infoRequests = infoRequests;
        this.region = region;
        this.appeal = appeal;
        this.hearings = hearings;
        this.evidence = evidence;
        this.dwpTimeExtension = dwpTimeExtension;
        this.events = events;
        this.subscriptions = subscriptions;
        this.regionalProcessingCenter = regionalProcessingCenter;
        this.caseBundles = caseBundles;
        this.sscsDocument = sscsDocument;
        this.draftSscsDocument = draftSscsDocument;
        this.draftSscsFurtherEvidenceDocument = draftSscsFurtherEvidenceDocument;
        this.corDocument = corDocument;
        this.draftCorDocument = draftCorDocument;
        this.generatedNino = generatedNino;
        this.generatedSurname = generatedSurname;
        this.generatedEmail = generatedEmail;
        this.generatedMobile = generatedMobile;
        this.generatedDob = generatedDob;
        this.directionResponse = directionResponse;
        this.evidencePresent = evidencePresent;
        this.bulkScanCaseReference = bulkScanCaseReference;
        this.decisionNotes = decisionNotes;
        this.isCorDecision = isCorDecision;
        this.relistingReason = relistingReason;
        this.dateSentToDwp = dateSentToDwp;
        this.interlocReviewState = interlocReviewState;
        this.hmctsDwpState = hmctsDwpState;
        this.dwpFurtherEvidenceStates = dwpFurtherEvidenceStates;
        this.originalSender = originalSender;
        this.furtherEvidenceAction = furtherEvidenceAction;
        this.scannedDocuments = scannedDocuments;
        this.outcome = outcome;
        this.sscsInterlocDirectionDocument = sscsInterlocDirectionDocument;
        this.sscsInterlocDecisionDocument = sscsInterlocDecisionDocument;
        this.sscsStrikeOutDocument = sscsStrikeOutDocument;
        this.informationFromAppellant = informationFromAppellant;
        this.evidenceHandled = evidenceHandled;
        this.assignedToJudge = assignedToJudge;
        this.assignedToDisabilityMember = assignedToDisabilityMember;
        this.assignedToMedicalMember = assignedToMedicalMember;
        this.reissueFurtherEvidenceDocument = reissueFurtherEvidenceDocument;
        this.resendToAppellant = resendToAppellant;
        this.resendToRepresentative = resendToRepresentative;
        this.resendToDwp = resendToDwp;
        this.caseCode = caseCode;
        this.benefitCode = benefitCode;
        this.issueCode = issueCode;
        this.dwpOriginatingOffice = dwpOriginatingOffice;
        this.dwpPresentingOffice = dwpPresentingOffice;
        this.dwpIsOfficerAttending = dwpIsOfficerAttending;
        this.dwpUcb = dwpUcb;
        this.dwpPhme = dwpPhme;
        this.dwpComplexAppeal = dwpComplexAppeal;
        this.dwpFurtherInfo = dwpFurtherInfo;
        this.correspondence = correspondence;
        this.interlocReferralDate = interlocReferralDate;
        this.interlocReferralReason = interlocReferralReason;
        this.dwpRegionalCentre = dwpRegionalCentre;
        this.generateNotice = generateNotice;
        this.previewDocument = previewDocument;
        this.bodyContent = bodyContent;
        this.signedBy = signedBy;
        this.signedRole = signedRole;
        this.dateAdded = dateAdded;
        this.historicSscsInterlocDirectionDocs = historicSscsInterlocDirectionDocs;
        this.dwpState = dwpState;
        this.appealNotePad = appealNotePad;
        this.dwpStateFeNoAction = dwpStateFeNoAction;
        this.createdInGapsFrom = createdInGapsFrom;
        this.dateCaseSentToGaps = dateCaseSentToGaps;
        this.associatedCase = associatedCase;
        this.dwpAT38Document = dwpAT38Document;
        this.dwpEvidenceBundleDocument = dwpEvidenceBundleDocument;
        this.dwpResponseDocument = dwpResponseDocument;
        this.dwpSupplementaryResponseDoc = dwpSupplementaryResponseDoc;
        this.dwpOtherDoc = dwpOtherDoc;
        this.dwpLT203 = dwpLT203;
        this.dwpLapseLetter = dwpLapseLetter;
        this.dwpResponseDate = dwpResponseDate;
        this.linkedCasesBoolean = linkedCasesBoolean;
        this.decisionType = decisionType;
        this.selectWhoReviewsCase = selectWhoReviewsCase;
        this.directionType = directionType;
        this.directionTypeDl = directionTypeDl;
        this.extensionNextEvent = extensionNextEvent;
        this.extensionNextEventDl = extensionNextEventDl;
        this.tl1Form = tl1Form;
        this.isInterlocRequired = isInterlocRequired;
        this.panel = panel;
        this.evidenceReceived = evidenceReceived;
        this.urgentCase = urgentCase;
        this.documentSentToDwp = documentSentToDwp;
        this.directionDueDate = directionDueDate;
        this.reservedToJudge = reservedToJudge;
        this.linkedCase = linkedCase;
        this.isWaiverNeeded = isWaiverNeeded;
        this.waiverDeclaration = waiverDeclaration;
        this.waiverReason = waiverReason;
        this.waiverReasonOther = waiverReasonOther;
        this.clerkDelegatedAuthority = clerkDelegatedAuthority;
        this.clerkAppealSatisfactionText = clerkAppealSatisfactionText;
        this.clerkConfirmationOfMrn = clerkConfirmationOfMrn;
        this.clerkOtherReason = clerkOtherReason;
        this.clerkConfirmationOther = clerkConfirmationOther;
        this.responseRequired = responseRequired;
        this.timeExtensionRequested = timeExtensionRequested;
        this.bundleConfiguration = bundleConfiguration;
        this.pcqId = pcqId;
        this.writeFinalDecisionIsDescriptorFlow = writeFinalDecisionIsDescriptorFlow;
        this.writeFinalDecisionGenerateNotice = writeFinalDecisionGenerateNotice;
        this.writeFinalDecisionAllowedOrRefused = writeFinalDecisionAllowedOrRefused;
        this.writeFinalDecisionTypeOfHearing = writeFinalDecisionTypeOfHearing;
        this.writeFinalDecisionPresentingOfficerAttendedQuestion = writeFinalDecisionPresentingOfficerAttendedQuestion;
        this.writeFinalDecisionAppellantAttendedQuestion = writeFinalDecisionAppellantAttendedQuestion;
        this.pipWriteFinalDecisionDailyLivingQuestion = pipWriteFinalDecisionDailyLivingQuestion;
        this.pipWriteFinalDecisionComparedToDwpDailyLivingQuestion = pipWriteFinalDecisionComparedToDwpDailyLivingQuestion;
        this.pipWriteFinalDecisionMobilityQuestion = pipWriteFinalDecisionMobilityQuestion;
        this.pipWriteFinalDecisionComparedToDwpMobilityQuestion = pipWriteFinalDecisionComparedToDwpMobilityQuestion;
        this.writeFinalDecisionStartDate = writeFinalDecisionStartDate;
        this.writeFinalDecisionEndDateType = writeFinalDecisionEndDateType;
        this.writeFinalDecisionEndDate = writeFinalDecisionEndDate;
        this.writeFinalDecisionDisabilityQualifiedPanelMemberName = writeFinalDecisionDisabilityQualifiedPanelMemberName;
        this.writeFinalDecisionMedicallyQualifiedPanelMemberName = writeFinalDecisionMedicallyQualifiedPanelMemberName;
        this.writeFinalDecisionDateOfDecision = writeFinalDecisionDateOfDecision;
        this.writeFinalDecisionDetailsOfDecision = writeFinalDecisionDetailsOfDecision;
        this.writeFinalDecisionReasons = writeFinalDecisionReasons;
        this.pipWriteFinalDecisionDailyLivingActivitiesQuestion = pipWriteFinalDecisionDailyLivingActivitiesQuestion;
        this.pipWriteFinalDecisionMobilityActivitiesQuestion = pipWriteFinalDecisionMobilityActivitiesQuestion;
        this.pipWriteFinalDecisionPreparingFoodQuestion = pipWriteFinalDecisionPreparingFoodQuestion;
        this.pipWriteFinalDecisionTakingNutritionQuestion = pipWriteFinalDecisionTakingNutritionQuestion;
        this.pipWriteFinalDecisionManagingTherapyQuestion = pipWriteFinalDecisionManagingTherapyQuestion;
        this.pipWriteFinalDecisionWashAndBatheQuestion = pipWriteFinalDecisionWashAndBatheQuestion;
        this.pipWriteFinalDecisionManagingToiletNeedsQuestion = pipWriteFinalDecisionManagingToiletNeedsQuestion;
        this.pipWriteFinalDecisionDressingAndUndressingQuestion = pipWriteFinalDecisionDressingAndUndressingQuestion;
        this.pipWriteFinalDecisionCommunicatingQuestion = pipWriteFinalDecisionCommunicatingQuestion;
        this.pipWriteFinalDecisionReadingUnderstandingQuestion = pipWriteFinalDecisionReadingUnderstandingQuestion;
        this.pipWriteFinalDecisionEngagingWithOthersQuestion = pipWriteFinalDecisionEngagingWithOthersQuestion;
        this.pipWriteFinalDecisionBudgetingDecisionsQuestion = pipWriteFinalDecisionBudgetingDecisionsQuestion;
        this.pipWriteFinalDecisionPlanningAndFollowingQuestion = pipWriteFinalDecisionPlanningAndFollowingQuestion;
        this.pipWriteFinalDecisionMovingAroundQuestion = pipWriteFinalDecisionMovingAroundQuestion;
        this.writeFinalDecisionPageSectionReference = writeFinalDecisionPageSectionReference;
        this.writeFinalDecisionAnythingElse = writeFinalDecisionAnythingElse;
        this.writeFinalDecisionPreviewDocument = writeFinalDecisionPreviewDocument;
        this.writeFinalDecisionGeneratedDate = writeFinalDecisionGeneratedDate;
        this.adjournCaseGenerateNotice = adjournCaseGenerateNotice;
        this.adjournCaseTypeOfHearing = adjournCaseTypeOfHearing;
        this.adjournCaseCanCaseBeListedRightAway = adjournCaseCanCaseBeListedRightAway;
        this.adjournCaseAreDirectionsBeingMadeToParties = adjournCaseAreDirectionsBeingMadeToParties;
        this.adjournCaseDirectionsDueDateDaysOffset = adjournCaseDirectionsDueDateDaysOffset;
        this.adjournCaseDirectionsDueDate = adjournCaseDirectionsDueDate;
        this.adjournCaseTypeOfNextHearing = adjournCaseTypeOfNextHearing;
        this.adjournCaseNextHearingVenue = adjournCaseNextHearingVenue;
        this.adjournCaseNextHearingVenueSelected = adjournCaseNextHearingVenueSelected;
        this.adjournCasePanelMembersExcluded = adjournCasePanelMembersExcluded;
        this.adjournCaseDisabilityQualifiedPanelMemberName = adjournCaseDisabilityQualifiedPanelMemberName;
        this.adjournCaseMedicallyQualifiedPanelMemberName = adjournCaseMedicallyQualifiedPanelMemberName;
        this.adjournCaseOtherPanelMemberName = adjournCaseOtherPanelMemberName;
        this.adjournCaseNextHearingDateType = adjournCaseNextHearingDateType;
        this.adjournCaseNextHearingDateOrPeriod = adjournCaseNextHearingDateOrPeriod;
        this.adjournCaseNextHearingDateOrTime = adjournCaseNextHearingDateOrTime;
        this.adjournCaseNextHearingFirstAvailableDateAfterDate = adjournCaseNextHearingFirstAvailableDateAfterDate;
        this.adjournCaseNextHearingFirstAvailableDateAfterPeriod = adjournCaseNextHearingFirstAvailableDateAfterPeriod;
        this.adjournCaseNextHearingSpecificDate = adjournCaseNextHearingSpecificDate;
        this.adjournCaseNextHearingSpecificTime = adjournCaseNextHearingSpecificTime;
    }

    @JsonIgnore
    private EventDetails getLatestEvent() {
        return events != null && !events.isEmpty() ? events.get(0).getValue() : null;
    }

    @JsonIgnore
    public boolean isCorDecision() {
        return isCorDecision != null && isCorDecision.toUpperCase().equals("YES");
    }

    @JsonIgnore
    public boolean isDailyLivingAndOrMobilityDecision() {
        return stringToBoolean(writeFinalDecisionIsDescriptorFlow);
    }

    @JsonIgnore
    public boolean isResendToAppellant() {
        return stringToBoolean(resendToAppellant);
    }

    @JsonIgnore
    public boolean isResendToRepresentative() {
        return stringToBoolean(resendToRepresentative);
    }

    @JsonIgnore
    public boolean isResendToDwp() {
        return stringToBoolean(resendToDwp);
    }

    @JsonIgnore
    public boolean isAdjournCaseGenerateNotice() {
        return stringToBoolean(adjournCaseGenerateNotice);
    }

    @JsonIgnore
    public boolean isAdjournCaseAbleToBeListedRightAway() {
        return stringToBoolean(adjournCaseCanCaseBeListedRightAway);
    }

    @JsonIgnore
    public boolean isAdjournCaseDirectionsMadeToParties() {
        return stringToBoolean(adjournCaseAreDirectionsBeingMadeToParties);
    }

    @JsonIgnore
    public boolean isGenerateNotice() {
        return stringToBoolean(generateNotice);
    }

    @JsonIgnore
    public String getLatestEventType() {
        EventDetails latestEvent = getLatestEvent();
        return latestEvent != null ? latestEvent.getType() : null;
    }

    @JsonIgnore
    public void sortCollections() {

        if (getCorrespondence() != null) {
            Collections.sort(getCorrespondence(), Collections.reverseOrder());
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
    }

    public Subscriptions getSubscriptions() {
        return null != subscriptions ? subscriptions : Subscriptions.builder().build();
    }

    @JsonIgnore
    private boolean stringToBoolean(String value) {
        return StringUtils.equalsIgnoreCase("yes", value);
    }

    @JsonIgnore
    public SscsDocument getLatestDocumentForDocumentType(DocumentType documentType) {

        if (getSscsDocument() != null && getSscsDocument().size() > 0) {
            Stream<SscsDocument> filteredStream = getSscsDocument().stream()
                .filter(f -> documentType.getValue().equals(f.getValue().getDocumentType()));

            List<SscsDocument> filteredList = filteredStream.collect(Collectors.toList());

            Collections.sort(filteredList, (one, two) -> {
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
            });
            if (filteredList.size() > 0) {
                return filteredList.get(0);
            }
        }
        return null;
    }
}

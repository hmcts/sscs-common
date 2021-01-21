package uk.gov.hmcts.reform.sscs.ccd.domain;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;
import uk.gov.hmcts.reform.sscs.ccd.validation.documentlink.DocumentLinkMustBePdf;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;
import uk.gov.hmcts.reform.sscs.ccd.validation.localdate.LocalDateMustBeInFuture;
import uk.gov.hmcts.reform.sscs.ccd.validation.localdate.LocalDateMustNotBeInFuture;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class SscsCaseData implements CaseData {

    @JsonProperty(access = WRITE_ONLY)
    private String ccdCaseId;

    private State state;
    private State previousState;
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
    @JsonProperty("subscriptions")
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
    private String isSaveAndReturn;
    private String generatedMobile;
    @JsonProperty("generatedDOB")
    private String generatedDob;
    private DirectionResponse directionResponse;
    private String evidencePresent;
    private String bulkScanCaseReference;
    private String decisionNotes;
    @JsonProperty("isCorDecision")
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
    @JsonProperty("resendToAppellant")
    private String resendToAppellant;
    @JsonProperty("resendToRepresentative")
    private String resendToRepresentative;
    @JsonProperty("resendToDwp")
    private String resendToDwp;
    private String caseCode;
    private String benefitCode;
    private String issueCode;
    private DynamicList dwpOriginatingOffice;
    private DynamicList dwpPresentingOffice;
    private String dwpIsOfficerAttending;
    @JsonProperty("dwpUCB")
    private String dwpUcb;
    private DocumentLink dwpUcbEvidenceDocument;
    @JsonProperty("dwpPHME")
    private String dwpPhme;
    private String dwpComplexAppeal;
    private String dwpFurtherInfo;
    private List<Correspondence> correspondence;
    private List<Correspondence> reasonableAdjustmentsLetters;
    private String interlocReferralDate;
    private String interlocReferralReason;
    private String dwpRegionalCentre;
    @JsonProperty("generateNotice")
    private String generateNotice;
    private DocumentLink previewDocument;
    private String bodyContent;
    private String signedBy;
    private String signedRole;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
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
    private DwpResponseDocument dwpEditedEvidenceBundleDocument;
    private String dwpEditedEvidenceReason;
    private DwpResponseDocument dwpResponseDocument;
    private DwpResponseDocument dwpEditedResponseDocument;
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
    private String urgentHearingRegistered;
    private String urgentHearingOutcome;
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
    private String writeFinalDecisionOtherPanelMemberName;
    @LocalDateMustNotBeInFuture(message = "Decision notice date of decision must not be in the future")
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
    @DocumentLinkMustBePdf(message = "You need to upload PDF documents only")
    private DocumentLink writeFinalDecisionPreviewDocument;
    private String writeFinalDecisionGeneratedDate;
    @JsonProperty("adjournCaseGenerateNotice")
    private String adjournCaseGenerateNotice;
    private String adjournCaseTypeOfHearing;
    @JsonProperty("adjournCaseCanCaseBeListedRightAway")
    private String adjournCaseCanCaseBeListedRightAway;
    @JsonProperty("adjournCaseAreDirectionsBeingMadeToParties")
    private String adjournCaseAreDirectionsBeingMadeToParties;
    private String adjournCaseDirectionsDueDateDaysOffset;
    @LocalDateMustBeInFuture(message = "Directions due date must be in the future")
    private String adjournCaseDirectionsDueDate;
    private String adjournCaseTypeOfNextHearing;
    private String adjournCaseNextHearingVenue;
    private DynamicList adjournCaseNextHearingVenueSelected;
    private String adjournCasePanelMembersExcluded;
    private String adjournCaseDisabilityQualifiedPanelMemberName;
    private String adjournCaseMedicallyQualifiedPanelMemberName;
    private String adjournCaseOtherPanelMemberName;
    private String adjournCaseNextHearingListingDurationType;
    private String adjournCaseNextHearingListingDuration;
    private String adjournCaseNextHearingListingDurationUnits;
    private String adjournCaseInterpreterRequired;
    private String adjournCaseInterpreterLanguage;
    private String adjournCaseNextHearingDateType;
    private String adjournCaseNextHearingDateOrPeriod;
    private String adjournCaseNextHearingDateOrTime;
    private String adjournCaseNextHearingFirstAvailableDateAfterDate;
    private String adjournCaseNextHearingFirstAvailableDateAfterPeriod;
    private AdjournCaseTime adjournCaseTime;
    private List<CollectionItem<String>> adjournCaseReasons;
    private List<CollectionItem<String>> adjournCaseAdditionalDirections;
    @DocumentLinkMustBePdf(message = "You need to upload PDF documents only")
    private DocumentLink adjournCasePreviewDocument;
    private String adjournCaseGeneratedDate;
    private String notListableProvideReasons;
    @LocalDateMustBeInFuture(message = "Directions due date must be in the future")
    private String notListableDueDate;
    private String updateNotListableDirectionsFulfilled;
    private String updateNotListableInterlocReview;
    private String updateNotListableWhoReviewsCase;
    private String updateNotListableSetNewDueDate;
    @LocalDateMustBeInFuture(message = "Directions due date must be in the future")
    private String updateNotListableDueDate;
    private String updateNotListableWhereShouldCaseMoveTo;
    @JsonProperty("languagePreferenceWelsh")
    private String languagePreferenceWelsh;
    private List<String> elementsDisputedList;
    private List<ElementDisputed> elementsDisputedGeneral;
    private List<ElementDisputed> elementsDisputedSanctions;
    private List<ElementDisputed> elementsDisputedOverpayment;
    private List<ElementDisputed> elementsDisputedHousing;
    private List<ElementDisputed> elementsDisputedChildCare;
    private List<ElementDisputed> elementsDisputedCare;
    private List<ElementDisputed> elementsDisputedChildElement;
    private List<ElementDisputed> elementsDisputedChildDisabled;
    private String elementsDisputedIsDecisionDisputedByOthers;
    private String elementsDisputedLinkedAppealRef;
    private String jointParty;
    private JointPartyName jointPartyName;
    private String reasonableAdjustmentChoice;
    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    private Identity jointPartyIdentity;
    @JsonProperty("jointPartyAddressSameAsAppellant")
    private String jointPartyAddressSameAsAppellant;
    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    private Address jointPartyAddress;
    @JsonProperty("translationWorkOutstanding")
    private String translationWorkOutstanding;
    private List<SscsWelshDocument> sscsWelshDocuments;
    private List<SscsWelshDocument> sscsWelshPreviewDocuments;
    private String sscsWelshPreviewNextEvent;
    private DynamicList originalDocuments;
    private DynamicList originalNoticeDocuments;
    private DynamicList documentTypes;
    private String welshBodyContent;
    private String englishBodyContent;
    private String isScottishCase;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate reinstatementRegistered;
    private RequestOutcome reinstatementOutcome;
    private String welshInterlocNextReviewState;
    private YesNo isConfidentialCase;
    private DatedRequestOutcome confidentialityRequestOutcomeAppellant;
    private DatedRequestOutcome confidentialityRequestOutcomeJointParty;
    private String confidentialityRequestAppellantGrantedOrRefused;
    private String confidentialityRequestJointPartyGrantedOrRefused;
    @JsonProperty(value = "formType")
    private FormType formType;
    private String isProgressingViaGaps;
    @JsonProperty("supportGroupOnlyAppeal")
    private String supportGroupOnlyAppeal;
    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsEsaCaseData sscsEsaCaseData;
    private String dwpReassessTheAward;
    private YesNo showFinalDecisionNoticeSummaryOfOutcomePage;
    private YesNo showDwpReassessAwardPage;
    @LocalDateMustNotBeInFuture(message = "Date of appellant death must not be in the future")
    private String dateOfAppellantDeath;
    @JsonProperty("phmeGranted")
    private YesNo phmeGranted;
    private DwpResponseDocument appendix12Doc;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsUcCaseData sscsUcCaseData;
    private List<DwpDocument> dwpDocuments;
    private String processingVenue;
    private ReasonableAdjustments reasonableAdjustments;
    private YesNo reasonableAdjustmentsOutstanding;

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
    public boolean isSupportGroupOnlyAppeal() {
        return stringToBoolean(supportGroupOnlyAppeal);
    }

    @JsonIgnore
    public boolean isThereAJointParty() {
        return stringToBoolean(jointParty);
    }

    @JsonIgnore
    public boolean isJointPartyAddressSameAsAppeallant() {
        return stringToBoolean(jointPartyAddressSameAsAppellant);
    }

    @JsonIgnore
    public String getLatestEventType() {
        EventDetails latestEvent = getLatestEvent();
        return latestEvent != null ? latestEvent.getType() : null;
    }

    @JsonIgnore
    public boolean isLanguagePreferenceWelsh() {
        return stringToBoolean(languagePreferenceWelsh);
    }

    @JsonIgnore
    public LanguagePreference getLanguagePreference() {
        return isLanguagePreferenceWelsh() ? LanguagePreference.WELSH : LanguagePreference.ENGLISH;
    }

    @JsonIgnore
    public boolean isTranslationWorkOutstanding() {
        return stringToBoolean(translationWorkOutstanding);
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

    @JsonIgnore
    public Optional<SscsWelshDocument> getLatestWelshDocumentForDocumentType(DocumentType documentType) {
        return Optional.ofNullable(getSscsWelshDocuments()).map(Collection::stream).orElseGet(Stream::empty)
                .filter(wd -> wd.getValue().getDocumentType().equals(documentType.getValue()))
                .sorted()
                .findFirst();
    }

    @JsonIgnore
    public void updateTranslationWorkOutstandingFlag() {
        if (getSscsDocument().stream().noneMatch(sd -> Arrays.asList(SscsDocumentTranslationStatus.TRANSLATION_REQUESTED, SscsDocumentTranslationStatus.TRANSLATION_REQUIRED).contains(sd.getValue().getDocumentTranslationStatus()))) {
            this.translationWorkOutstanding = "No";
        } else {
            this.translationWorkOutstanding = "Yes";
        }
    }

    @JsonIgnore
    public void updateReasonableAdjustmentsOutstanding() {
        this.reasonableAdjustmentsOutstanding = YesNo.NO;
    }

    @JsonIgnore
    public SscsEsaCaseData getSscsEsaCaseData() {
        if (sscsEsaCaseData == null) {
            this.sscsEsaCaseData = new SscsEsaCaseData();
        }
        return sscsEsaCaseData;
    }

    @JsonIgnore
    public SscsUcCaseData getSscsUcCaseData() {
        if (sscsUcCaseData == null) {
            this.sscsUcCaseData = new SscsUcCaseData();
        }
        return sscsUcCaseData;
    }
    
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.INFECTED_BLOOD_COMPENSATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.findBenefitByShortName;
import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.FINAL_DECISION_ISSUED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;
import uk.gov.hmcts.reform.sscs.ccd.validation.localdate.LocalDateMustNotBeInFuture;
import uk.gov.hmcts.reform.sscs.model.PoDetails;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

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
    private List<Hearing> completedHearingsList;
    private List<HearingOutcome> hearingOutcomes;
    private HearingOutcomeValue hearingOutcomeValue;
    private Evidence evidence;
    private List<DwpTimeExtension> dwpTimeExtension;
    private List<Event> events;
    @Getter(AccessLevel.NONE)
    @JsonProperty("subscriptions")
    private Subscriptions subscriptions;
    private RegionalProcessingCenter regionalProcessingCenter;
    private List<Bundle> caseBundles;
    private List<Bundle> historicalBundles;
    private List<SscsDocument> sscsDocument;
    private List<SscsDocument> draftSscsDocument;
    private List<SscsFurtherEvidenceDoc> draftSscsFurtherEvidenceDocument;
    private SscsInterlocDecisionDocument sscsInterlocDecisionDocument;
    private SscsInterlocDirectionDocument sscsInterlocDirectionDocument;
    private SscsStrikeOutDocument sscsStrikeOutDocument;
    private String isSaveAndReturn;
    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsDeprecatedFields sscsDeprecatedFields;
    private DirectionResponse directionResponse;
    private String evidencePresent;
    private String bulkScanCaseReference;
    private String decisionNotes;
    @JsonProperty("isCorDecision")
    private String isCorDecision;
    private String relistingReason;
    private String dateSentToDwp;
    private String dwpDueDate;
    private InterlocReviewState interlocReviewState;
    private String hmctsDwpState;
    private String dwpFurtherEvidenceStates;
    private DynamicList processAudioVideoAction;
    private DynamicList originalSender;
    private DynamicList furtherEvidenceAction;
    private List<ScannedDocument> scannedDocuments;
    private DynamicList selectedAudioVideoEvidence;
    private AudioVideoEvidenceDetails selectedAudioVideoEvidenceDetails;
    private List<AudioVideoEvidence> audioVideoEvidence;
    private List<AudioVideoEvidence> dwpUploadAudioVideoEvidence;
    private YesNo hasUnprocessedAudioVideoEvidence;
    private String informationFromAppellant;
    private DynamicList informationFromPartySelected;
    private String outcome;
    private String evidenceHandled;
    @JsonProperty("presentingOfficersDetails")
    private PoDetails presentingOfficersDetails;
    private String presentingOfficersHearingLink;
    private YesNo poAttendanceConfirmed;
    private YesNo tribunalDirectPoToAttend;

    //SSCS-10007
    private List<CcdValue<OtherPartySelectionDetails>> otherPartySelection;
    private List<CcdValue<DocumentSelectionDetails>> documentSelection;
    private DynamicList letterAttachedDocuments;
    private String genericLetterText;

    private YesNo sendToAllParties;
    private YesNo sendToApellant;
    private YesNo sendToRepresentative;
    private YesNo sendToJointParty;
    private YesNo sendToOtherParties;
    private YesNo addDocuments;

    private YesNo hasJointParty;
    private YesNo hasRepresentative;
    private YesNo hasOtherParties;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private ReissueArtifactUi reissueArtifactUi;
    private String caseCode;
    private String benefitCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String benefitCodeIbcaOnly;
    private String issueCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String issueCodeIbcaOnly;
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
    private ReasonableAdjustmentsLetters reasonableAdjustmentsLetters;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate interlocReferralDate;
    private InterlocReferralReason interlocReferralReason;
    private String dwpRegionalCentre;
    private DwpState dwpState;
    private DynamicList dynamicDwpState;
    private NotePad appealNotePad;
    private DynamicList dwpStateFeNoAction;
    private String createdInGapsFrom;
    private String dateCaseSentToGaps;
    private String dateTimeCaseSentToGaps;
    private List<CaseLink> associatedCase;
    private DwpResponseDocument dwpAT38Document;
    private DwpResponseDocument dwpEvidenceBundleDocument;
    private DwpResponseDocument dwpEditedEvidenceBundleDocument;
    private String dwpEditedEvidenceReason;
    private DwpResponseDocument dwpResponseDocument;
    private DwpResponseDocument dwpEditedResponseDocument;
    private DwpResponseDocument dwpSupplementaryResponseDoc;
    private DwpResponseDocument dwpChallengeValidityDocument;
    private DwpResponseDocument dwpOtherDoc;
    private DwpResponseDocument dwpLT203;
    private DwpResponseDocument dwpLapseLetter;
    private DocumentLink rip1Doc;
    private String isRip1Doc;
    private YesNo showRip1DocPage;
    private String dwpResponseDate;
    private String linkedCasesBoolean;
    private String decisionType;
    private DynamicList selectWhoReviewsCase;
    @Deprecated
    private DirectionType directionType;
    private DynamicList directionTypeDl;
    private PrePostHearing prePostHearing;
    private String confidentialityType;
    private YesNo sendDirectionNoticeToFTA;
    private YesNo sendDirectionNoticeToRepresentative;
    private YesNo sendDirectionNoticeToOtherPartyRep;
    private YesNo sendDirectionNoticeToOtherPartyAppointee;
    private YesNo sendDirectionNoticeToOtherParty;
    private YesNo sendDirectionNoticeToJointParty;
    private YesNo sendDirectionNoticeToAppellantOrAppointee;
    private YesNo hasOtherPartyRep;
    private YesNo hasOtherPartyAppointee;

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
    private YesNo judgeReserved;
    private JudicialUserBase reservedToJudgeInterloc;
    private List<CaseLink> linkedCase;
    private String isWaiverNeeded;
    private List<String> waiverDeclaration;
    private List<String> waiverReason;
    private String waiverReasonOther;
    private List<String> clerkDelegatedAuthority;
    private List<String> clerkAppealSatisfactionText;
    @JsonProperty("clerkConfirmationOfMRN")
    private String clerkConfirmationOfMrn;
    private String clerkOtherReason;
    private String clerkConfirmationOther;
    private String responseRequired;
    private String timeExtensionRequested;
    private String bundleConfiguration;
    private List<MultiBundleConfig> multiBundleConfiguration;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate issueFinalDecisionDate;
    private LocalDate issueInterlocDecisionDate;
    private String notListableProvideReasons;
    private String notListableDueDate;
    private String updateNotListableDirectionsFulfilled;
    private String updateNotListableInterlocReview;
    private String updateNotListableWhoReviewsCase;
    private String updateNotListableSetNewDueDate;
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
    private List<ElementDisputed> elementsDisputedLimitedWork;
    private String elementsDisputedIsDecisionDisputedByOthers;
    private String elementsDisputedLinkedAppealRef;
    private List<CcdValue<OtherParty>> otherParties;
    @JsonProperty("otherPartyUCB")
    private String otherPartyUcb;
    private String childMaintenanceNumber;
    private String reasonableAdjustmentChoice;
    private YesNo doesOtherPersonKnowWhereYouLive;
    private YesNo keepHomeAddressConfidential;
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
    private YesNo isInc5249521;
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
    @JsonProperty("wcaAppeal")
    private YesNo wcaAppeal;
    private YesNo isAppellantDeceased;
    private YesNo isFqpmRequired;
    private YesNo isMedicalMemberRequired;
    @LocalDateMustNotBeInFuture(message = "Date of appellant death must not be in the future")
    private String dateOfAppellantDeath;
    @JsonProperty("phmeGranted")
    private YesNo phmeGranted;
    private DwpResponseDocument appendix12Doc;
    private YesNo preWorkAllocation;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsUcCaseData sscsUcCaseData;
    private List<DwpDocument> dwpDocuments;
    private String processingVenue;
    private List<DraftSscsDocument> draftFurtherEvidenceDocuments;
    private ReasonableAdjustments reasonableAdjustments;
    private YesNo reasonableAdjustmentsOutstanding;
    private YesNo showOtherPartyDetails;
    private AudioVideoEvidenceBundleDocument audioVideoEvidenceBundleDocument;

    @JsonProperty("processAudioVideoReviewState")
    private ProcessAudioVideoReviewState processAudioVideoReviewState;
    private String tempNoteDetail;
    private YesNo showWorkCapabilityAssessmentPage;

    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    private SscsIndustrialInjuriesData sscsIndustrialInjuriesData;

    private YesNo functionalTest;

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

    private YesNo ignoreCallbackWarnings;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo selectNextHmcHearingType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("hmcHearingType")
    private HmcHearingType hmcHearingType;

    @JsonIgnore
    private EventDetails getLatestEvent() {
        return events != null && !events.isEmpty() ? events.get(0).getValue() : null;
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
            return sortedHearings.get(0);
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
        return StringUtils.equalsIgnoreCase("yes", value);
    }

    @JsonIgnore
    public SscsDocument getLatestDocumentForDocumentType(DocumentType documentType) {

        if (getSscsDocument() != null && getSscsDocument().size() > 0) {
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
            }).collect(Collectors.toList());

            if (!filteredList.isEmpty()) {
                return filteredList.get(0);
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

    public boolean isBenefitType(Benefit benefitType) {
        return getBenefitType().filter(benefitType::equals).isPresent();
    }
}

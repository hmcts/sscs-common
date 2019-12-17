package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("clerkConfirmationOfMRN")
    private String clerkConfirmationOfMrn;
    private String clerkOtherReason;
    private String clerkConfirmationOther;
    private String dwpRequestTimeExtension;

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
                        @JsonProperty("clerkConfirmationOfMRN") String clerkConfirmationOfMrn,
                        @JsonProperty("clerkOtherReason") String clerkOtherReason,
                        @JsonProperty("clerkConfirmationOther") String clerkConfirmationOther,
                        @JsonProperty("dwpRequestTimeExtension") String dwpRequestTimeExtension
    ) {
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
        this.dwpRequestTimeExtension = dwpRequestTimeExtension;
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
            Stream<SscsDocument> filteredDocs = getSscsDocument().stream()
                .filter(f -> documentType.getValue().equals(f.getValue().getDocumentType()));

            List<SscsDocument> docs = filteredDocs.sorted().collect(Collectors.toList());

            if (docs.size() > 0) {
                return docs.get(0);
            }
        }
        return null;
    }
}

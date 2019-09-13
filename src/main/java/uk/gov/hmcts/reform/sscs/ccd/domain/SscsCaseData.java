package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsCaseData implements CaseData {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String ccdCaseId;

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

    @JsonCreator
    public SscsCaseData(@JsonProperty(value = "ccdCaseId", access = JsonProperty.Access.WRITE_ONLY) String ccdCaseId,
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
                        @JsonProperty("createdInGapsFrom") String createdInGapsFrom) {
        this.ccdCaseId = ccdCaseId;
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
    }

    public Subscriptions getSubscriptions() {
        return null != subscriptions ? subscriptions : Subscriptions.builder().build();
    }

    @JsonIgnore
    private boolean stringToBoolean(String value) {
        return StringUtils.equalsIgnoreCase("yes", value);
    }
}

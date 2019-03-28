package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsCaseData implements CaseData {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String ccdCaseId;

    private String caseReference;
    private String caseCreated;
    private String region;
    private Appeal appeal;
    private List<Hearing> hearings;
    private Evidence evidence;
    private List<DwpTimeExtension> dwpTimeExtension;
    private List<Event> events;
    private Subscriptions subscriptions;
    private RegionalProcessingCenter regionalProcessingCenter;
    private List<SscsDocument> sscsDocument;
    private List<SscsDocument> draftSscsDocument;
    private List<CorDocument> corDocument;
    private List<CorDocument> draftCorDocument;
    private String generatedNino;
    private String generatedSurname;
    private String generatedEmail;
    private String generatedMobile;
    @JsonProperty("generatedDOB")
    private String generatedDob;
    private String evidencePresent;
    private OnlinePanel onlinePanel;
    private String bulkScanCaseReference;
    private String decisionNotes;
    private String isCorDecision;

    @JsonCreator
    public SscsCaseData(@JsonProperty(value = "ccdCaseId", access = JsonProperty.Access.WRITE_ONLY) String ccdCaseId,
                        @JsonProperty("caseReference") String caseReference,
                        @JsonProperty("caseCreated") String caseCreated,
                        @JsonProperty("region") String region,
                        @JsonProperty("appeal") Appeal appeal,
                        @JsonProperty("hearings") List<Hearing> hearings,
                        @JsonProperty("evidence") Evidence evidence,
                        @JsonProperty("dwpTimeExtension") List<DwpTimeExtension> dwpTimeExtension,
                        @JsonProperty("events") List<Event> events,
                        @JsonProperty("subscriptions") Subscriptions subscriptions,
                        @JsonProperty("regionalProcessingCenter") RegionalProcessingCenter regionalProcessingCenter,
                        @JsonProperty("sscsDocument") List<SscsDocument> sscsDocument,
                        @JsonProperty("draftSscsDocument") List<SscsDocument> draftSscsDocument,
                        @JsonProperty("corDocument") List<CorDocument> corDocument,
                        @JsonProperty("draftCorDocument") List<CorDocument> draftCorDocument,
                        @JsonProperty("generatedNino") String generatedNino,
                        @JsonProperty("generatedSurname") String generatedSurname,
                        @JsonProperty("generatedEmail") String generatedEmail,
                        @JsonProperty("generatedMobile") String generatedMobile,
                        @JsonProperty("generatedDOB") String generatedDob,
                        @JsonProperty("evidencePresent") String evidencePresent,
                        @JsonProperty("onlinePanel") OnlinePanel onlineOnlinePanel,
                        @JsonProperty("bulkScanCaseReference") String bulkScanCaseReference,
                        @JsonProperty("decisionNotes") String decisionNotes,
                        @JsonProperty("isCorDecision") String isCorDecision) {
        this.ccdCaseId = ccdCaseId;
        this.caseReference = caseReference;
        this.caseCreated = caseCreated;
        this.region = region;
        this.appeal = appeal;
        this.hearings = hearings;
        this.evidence = evidence;
        this.dwpTimeExtension = dwpTimeExtension;
        this.events = events;
        this.subscriptions = subscriptions;
        this.regionalProcessingCenter = regionalProcessingCenter;
        this.sscsDocument = sscsDocument;
        this.draftSscsDocument = draftSscsDocument;
        this.corDocument = corDocument;
        this.draftCorDocument = draftCorDocument;
        this.generatedNino = generatedNino;
        this.generatedSurname = generatedSurname;
        this.generatedEmail = generatedEmail;
        this.generatedMobile = generatedMobile;
        this.generatedDob = generatedDob;
        this.evidencePresent = evidencePresent;
        this.onlinePanel = onlineOnlinePanel;
        this.bulkScanCaseReference = bulkScanCaseReference;
        this.decisionNotes = decisionNotes;
        this.isCorDecision = isCorDecision;
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
    public String getLatestEventType() {
        EventDetails latestEvent = getLatestEvent();
        return latestEvent != null ? latestEvent.getType() : null;
    }
}

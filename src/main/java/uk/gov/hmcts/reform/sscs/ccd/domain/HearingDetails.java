package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class HearingDetails {
    private Venue venue;
    private String hearingDate;
    private String time;
    private String adjourned;
    private String eventDate;
    private String hearingId;
    private String venueId;

    //  HearingDetails // replaced the existing fields above which are used by robotics
    private boolean autolistFlag;
    private String hearingType;
    private HearingWindow hearingWindow;
    private int duration;
    private List<String> nonStandardHearingDurationReasons;
    private String hearingPriorityType;
    private int numberOfPhysicalAttendees;
    private boolean hearingInWelshFlag;
    private List<HearingLocation> hearingLocations;
    private List<String> facilitiesRequired;
    private String listingComments;
    private String hearingRequester;
    private boolean privateHearingRequiredFlag;
    private boolean leadJudgeContractType;
    private PanelRequirements panelRequirements;
    private boolean hearingIsLinkedFlag;

    //  CaseDetails
    private String hmctsServiceCode; //4 letter code? Ask BA
    //    private String caseRef; //I think it's pulled from SscsCaseData.ccdCaseId, shouldn't be stored if so.
    //    private DateTime requestTimeStamp; //Same as Hearing.requestDetails.requestTimeStamp, shouldn't be stored if so.
    private String externalCaseReference; // Not used by HMC or List Assist, for 'other components / Notifications'
    private String caseDeepLink; // Not sure what this is, will be generated, but should it be stored?
    //    private String hmctsInternalCaseName; // Pulled from WorkAllocationFields.caseNameHmctsInternal, shouldn't be stored.
    //    private String publicCaseName; // Pulled from WorkAllocationFields.caseNamePublic, shouldn't be stored.
    private boolean caseAdditionalSecurityFlag; // Might be the same as robotics flag OtherParty.unacceptableCustomerBehaviour, I suspect if any party has this flag, this is flagged. Should be stored?
    private boolean caseInterpreterRequiredFlag; // If any party needs an Interpreter, this is flagged. Should be stored?
    private List<HmcCaseCategory> caseCategories; // Should be stored?
    private String caseManagementLocationCode; // Not sure what this is or if we can generate this, BA?
    private boolean caserestrictedFlag; // Might be the same as isConfidentialCase and/or OtherParty.confidentialityRequired, not sure. Should be stored?
    //    private String caseSLAStartDate; // I'm pretty sure this is sscsCaseData.caseCreated, format may need refactoring, if not caseCreated Event.EventDetails.date, Event.EventDetails.type = APPEAL_RECEIVED could be used. shouldn't be stored?

    @JsonCreator
    public HearingDetails(@JsonProperty("venue") Venue venue,
                          @JsonProperty("hearingDate") String hearingDate,
                          @JsonProperty("time") String time,
                          @JsonProperty("adjourned") String adjourned,
                          @JsonProperty("eventDate") String eventDate,
                          @JsonProperty("hearingId") String hearingId,
                          @JsonProperty("venueId") String venueId,
                          @JsonProperty("autolistFlag") boolean autolistFlag,
                          @JsonProperty("hearingType") String hearingType,
                          @JsonProperty("hearingWindow") HearingWindow hearingWindow,
                          @JsonProperty("duration") int duration,
                          @JsonProperty("nonStandardHearingDurationReasons") List<String> nonStandardHearingDurationReasons,
                          @JsonProperty("hearingPriorityType") String hearingPriorityType,
                          @JsonProperty("numberOfPhysicalAttendees") int numberOfPhysicalAttendees,
                          @JsonProperty("hearingInWelshFlag") boolean hearingInWelshFlag,
                          @JsonProperty("hearingLocations") List<HearingLocation> hearingLocations,
                          @JsonProperty("facilitiesRequired") List<String> facilitiesRequired,
                          @JsonProperty("listingComments") String listingComments,
                          @JsonProperty("hearingRequester") String hearingRequester,
                          @JsonProperty("privateHearingRequiredFlag") boolean privateHearingRequiredFlag,
                          @JsonProperty("leadJudgeContractType") boolean leadJudgeContractType,
                          @JsonProperty("panelRequirements") PanelRequirements panelRequirements,
                          @JsonProperty("hearingIsLinkedFlag") boolean hearingIsLinkedFlag,
                          @JsonProperty("hmctsServiceCode") String hmctsServiceCode,
                          @JsonProperty("externalCaseReference") String externalCaseReference,
                          @JsonProperty("caseDeepLink") String caseDeepLink,
                          @JsonProperty("caseAdditionalSecurityFlag") boolean caseAdditionalSecurityFlag,
                          @JsonProperty("caseInterpreterRequiredFlag") boolean caseInterpreterRequiredFlag,
                          @JsonProperty("caseCategories") List<HmcCaseCategory> caseCategories,
                          @JsonProperty("caseManagementLocationCode") String caseManagementLocationCode,
                          @JsonProperty("caserestrictedFlag") boolean caserestrictedFlag) {
        this.venue = venue;
        this.hearingDate = hearingDate;
        this.time = time;
        this.adjourned = adjourned;
        this.eventDate = eventDate;
        this.hearingId = hearingId;
        this.venueId = venueId;
        this.autolistFlag = autolistFlag;
        this.hearingType = hearingType;
        this.hearingWindow = hearingWindow;
        this.duration = duration;
        this.nonStandardHearingDurationReasons = nonStandardHearingDurationReasons;
        this.hearingPriorityType = hearingPriorityType;
        this.numberOfPhysicalAttendees = numberOfPhysicalAttendees;
        this.hearingInWelshFlag = hearingInWelshFlag;
        this.hearingLocations = hearingLocations;
        this.facilitiesRequired = facilitiesRequired;
        this.listingComments = listingComments;
        this.hearingRequester = hearingRequester;
        this.privateHearingRequiredFlag = privateHearingRequiredFlag;
        this.leadJudgeContractType = leadJudgeContractType;
        this.panelRequirements = panelRequirements;
        this.hearingIsLinkedFlag = hearingIsLinkedFlag;
        this.hmctsServiceCode = hmctsServiceCode;
        this.externalCaseReference = externalCaseReference;
        this.caseDeepLink = caseDeepLink;
        this.caseAdditionalSecurityFlag = caseAdditionalSecurityFlag;
        this.caseInterpreterRequiredFlag = caseInterpreterRequiredFlag;
        this.caseCategories = caseCategories;
        this.caseManagementLocationCode = caseManagementLocationCode;
        this.caserestrictedFlag = caserestrictedFlag;
    }

    @JsonIgnore
    public LocalDateTime getHearingDateTime() {
        return LocalDateTime.of(LocalDate.parse(hearingDate), LocalTime.parse(time));
    }
}

package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionCaseCodeMapping {
    private String benefitDescription;
    private String benefitCode;
    private String issueDescription;
    private String caseCode;
    private String ccdKey;
    private Number sessionCat;
    private Number otherSessionCat;
    private String durationFaceToFace;
    private String durationPaper;
    private String panelMembers;
    private String comment;

    public SessionCaseCodeMapping(@JsonProperty("benefitDescription") String benefitDescription,
                                  @JsonProperty("benefitCode") String benefitCode,
                                  @JsonProperty("issueDescription") String issueDescription,
                                  @JsonProperty("caseCode") String caseCode,
                                  @JsonProperty("ccdKey") String ccdKey,
                                  @JsonProperty("sessionCat") Number sessionCat,
                                  @JsonProperty("otherSessionCat") Number otherSessionCat,
                                  @JsonProperty("durationFaceToFace") String durationFaceToFace,
                                  @JsonProperty("durationPaper") String durationPaper,
                                  @JsonProperty("panelMembers") String panelMembers,
                                  @JsonProperty("comment") String comment) {
        this.benefitDescription = benefitDescription;
        this.benefitCode = benefitCode;
        this.issueDescription = issueDescription;
        this.caseCode = caseCode;
        this.ccdKey = ccdKey;
        this.sessionCat = sessionCat;
        this.otherSessionCat = otherSessionCat;
        this.durationFaceToFace = durationFaceToFace;
        this.durationPaper = durationPaper;
        this.panelMembers = panelMembers;
        this.comment = comment;
    }
}

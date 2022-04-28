package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
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

}

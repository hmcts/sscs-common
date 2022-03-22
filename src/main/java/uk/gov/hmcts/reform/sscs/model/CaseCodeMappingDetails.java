package uk.gov.hmcts.reform.sscs.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder

public class CaseCodeMappingDetails {
    private String benefitDescription;
    private String benefitCode;
    private String issueDescription;
    private String caseCode;
    private String ccdKey;
    private String sessionCategory;
    private String otherSessionCategory;
    private int duration;
    private String panelMembers;
}

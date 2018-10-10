package uk.gov.hmcts.reform.sscs.domain.robotics;

import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@Value
@Builder
public class RoboticsWrapper {

    private SscsCaseData sscsCaseData;

    private long ccdCaseId;

    private String venueName;

    private String evidencePresent;
}

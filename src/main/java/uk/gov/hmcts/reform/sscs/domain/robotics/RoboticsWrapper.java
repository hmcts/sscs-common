package uk.gov.hmcts.reform.sscs.domain.robotics;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@Data
@Builder
public class RoboticsWrapper {

    private SscsCaseData sscsCaseData;

    private Long ccdCaseId;

    private String venueName;

    private String evidencePresent;
}

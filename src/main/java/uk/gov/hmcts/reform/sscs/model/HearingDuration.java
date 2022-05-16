package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class HearingDuration {

    private BenefitCode benefitCode;
    private Issue issue;
    private Integer durationFaceToFace;
    private Integer durationInterpreter;
    private Integer durationPaper;

    @Override
    public int hashCode() {
        return Objects.hash(benefitCode, issue);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}

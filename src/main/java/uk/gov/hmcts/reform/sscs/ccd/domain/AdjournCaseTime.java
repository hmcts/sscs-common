package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class AdjournCaseTime {
    private List<String> adjournCaseNextHearingFirstOnSession;
    private String adjournCaseNextHearingSpecificTime;

    public AdjournCaseTime(@JsonProperty("adjournCaseNextHearingFirstOnSession") List<String> adjournCaseNextHearingFirstOnSession,
                           @JsonProperty("adjournCaseNextHearingSpecificTime") String adjournCaseNextHearingSpecificTime) {
        this.adjournCaseNextHearingFirstOnSession = adjournCaseNextHearingFirstOnSession;
        this.adjournCaseNextHearingSpecificTime = adjournCaseNextHearingSpecificTime;
    }
}

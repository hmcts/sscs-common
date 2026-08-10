package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "adjournCaseTimeCT", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class AdjournCaseTime {
    @CCD(
            label = " ",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_adjournCaseFirstOnSession",
            typeParameterClass = AdjournCaseFirstOnSession.class
    )
    private List<String> adjournCaseNextHearingFirstOnSession;
    @CCD(
            label = "Provide time",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_adjournCaseNextHearingTime",
            typeParameterClass = AdjournCaseNextHearingTime.class
    )
    private String adjournCaseNextHearingSpecificTime;

    public AdjournCaseTime(@JsonProperty("adjournCaseNextHearingFirstOnSession") List<String> adjournCaseNextHearingFirstOnSession,
                           @JsonProperty("adjournCaseNextHearingSpecificTime") String adjournCaseNextHearingSpecificTime) {
        this.adjournCaseNextHearingFirstOnSession = adjournCaseNextHearingFirstOnSession;
        this.adjournCaseNextHearingSpecificTime = adjournCaseNextHearingSpecificTime;
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "elementAndIssueCode", generate = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class ElementDisputedDetails {

    @CCD(
            label = "Issue code",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_issueCodesUc",
            typeParameterClass = IssueCodesUc.class
    )
    private String issueCode;
    @CCD(label = "Outcome")
    private String outcome;

    @JsonCreator
    public ElementDisputedDetails(@JsonProperty("issueCode") String issueCode,
                                  @JsonProperty("outcome") String outcome) {
        this.issueCode = issueCode;
        this.outcome = outcome;
    }
}

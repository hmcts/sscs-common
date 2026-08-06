package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "otherPartyAttendedQuestion", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherPartyAttendedQuestionDetails {

    @CCD(label = "Other party name")
    private String otherPartyName;
    @CCD(label = "Other party attend the hearing?", typeOverride = FieldType.YesOrNo)
    private YesNo attendedOtherParty;

    @JsonCreator
    public OtherPartyAttendedQuestionDetails(@JsonProperty("otherPartyName") String otherPartyName,
                                             @JsonProperty("attendedOtherParty") YesNo attendedOtherParty) {
        this.otherPartyName = otherPartyName;
        this.attendedOtherParty = attendedOtherParty;
    }
}

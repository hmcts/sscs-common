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

@ComplexType(name = "OtherPartyOption", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherPartyOptionDetails {

    @CCD(label = "Other party id")
    private String otherPartyOptionId;
    @CCD(label = "Other party name")
    private String otherPartyOptionName;
    @CCD(label = "Reissue", typeOverride = FieldType.YesOrNo)
    private YesNo resendToOtherParty;

    @JsonCreator
    public OtherPartyOptionDetails(@JsonProperty("otherPartyOptionId") String otherPartyOptionId,
                                   @JsonProperty("otherPartyOptionName") String otherPartyOptionName,
                                   @JsonProperty("resendToOtherParty") YesNo resendToOtherParty) {
        this.otherPartyOptionId = otherPartyOptionId;
        this.otherPartyOptionName = otherPartyOptionName;
        this.resendToOtherParty = resendToOtherParty;
    }
}

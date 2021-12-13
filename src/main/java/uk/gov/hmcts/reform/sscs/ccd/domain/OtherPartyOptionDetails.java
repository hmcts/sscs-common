package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherPartyOptionDetails {

    private String otherPartyOptionId;
    private String otherPartyOptionName;
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

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class Contact {
    @CCD(label = "Contact Email")
    private String email;
    @CCD(label = "Contact Number")
    private String phone;
    @CCD(label = "Mobile Number")
    private String mobile;

    @JsonCreator
    public Contact(@JsonProperty("email") String email,
                   @JsonProperty("phone") String phone,
                   @JsonProperty("mobile") String mobile) {
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
    }
}

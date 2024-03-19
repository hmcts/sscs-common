package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class Contact {
    private String email;
    private String phone;
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

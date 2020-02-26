package uk.gov.hmcts.reform.sscs.idam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails {
    String id;
    String email;
    List<String> roles;

    public UserDetails(@JsonProperty("id") String id,
                       @JsonProperty("email") String email,
                       @JsonProperty("roles") List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}

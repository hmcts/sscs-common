package uk.gov.hmcts.reform.sscs.idam;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    String forename;
    String surname;
    List<String> roles;

    public UserDetails(@JsonProperty("id") String id,
                       @JsonProperty("email") String email,
                       @JsonProperty("roles") List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.forename = null;
        this.surname = null;
    }

    public UserDetails(@JsonProperty("id") String id,
                       @JsonProperty("email") String email,
                       @JsonProperty("forename") String forename,
                       @JsonProperty("surname") String surname,
                       @JsonProperty("roles") List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.forename = forename;
        this.surname = surname;
    }

    @JsonIgnore
    public boolean hasJudgeRole() {
        return roles != null && roles.contains("caseworker-sscs-judge");
    }

}

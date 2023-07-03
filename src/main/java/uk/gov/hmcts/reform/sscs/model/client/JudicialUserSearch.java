package uk.gov.hmcts.reform.sscs.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudicialUserSearch {
    @JsonProperty("email_id")
    private String emailId;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("known_as")
    private String knownAs;
    @JsonProperty("title")
    private String title;
    @JsonProperty("idam_id")
    private String idamId;
    @JsonProperty("personal_code")
    private String personalCode;
}

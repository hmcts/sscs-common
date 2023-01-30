package uk.gov.hmcts.reform.sscs.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudicialUser {

    private List<JudicialMemberAppointments> appointments;
    private List<JudicialMemberAuthorisations> authorisations;
    @JsonProperty("email_id")
    private String emailId;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("known_As")
    private String knownAs;
    @JsonProperty("post_nominals")
    private String postNominals;
    @JsonProperty("object_Id")
    private String objectId;
    @JsonProperty("sidam_Id")
    private String sidamId;
    private String surname;
    @JsonProperty("personal_Code")
    private String personalCode;
}

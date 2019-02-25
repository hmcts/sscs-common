package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Identity {
    private String dob;
    private String nino;

    @JsonCreator
    public Identity(@JsonProperty("dob") String dob,
                    @JsonProperty("nino") String nino) {
        this.dob = dob;
        this.nino = nino;
    }
}

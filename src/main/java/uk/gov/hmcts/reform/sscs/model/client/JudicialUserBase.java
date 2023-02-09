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
public class JudicialUserBase {
    @JsonProperty("idamId")
    private String idamId;
    @JsonProperty("personalCode")
    private String personalCode;

    @Override
    public boolean equals(Object object) {
        if (object instanceof JudicialUserBase) {
            return this.idamId.equals(((JudicialUserBase) object).getIdamId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
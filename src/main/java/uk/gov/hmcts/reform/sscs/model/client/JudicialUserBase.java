package uk.gov.hmcts.reform.sscs.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class JudicialUserBase {
    @JsonProperty("idamId")
    private String idamId;
    @JsonProperty("personalCode")
    private String personalCode;

    @Override
    public boolean equals(Object object) {
        log.info("hit2 {}", object);
        if (object instanceof JudicialUserBase) {
            return this.idamId.equals(((JudicialUserBase) object).getIdamId());
        }

        return false;
    }
}
package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class BenefitType {
    private String code;
    private String description;

    @JsonCreator
    public BenefitType(@JsonProperty("code") String code,
                       @JsonProperty("description") String description) {
        this.code = code;
        this.description = description;
    }
}

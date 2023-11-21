package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class BenefitType {
    private String code;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DynamicList descriptionSelection;

    @JsonCreator
    public BenefitType(@JsonProperty("code") String code,
                       @JsonProperty("description") String description,
                       @JsonProperty("descriptionSelection") DynamicList descriptionSelection) {
        this.code = code;
        this.description = description;
        this.descriptionSelection = descriptionSelection;
    }
}

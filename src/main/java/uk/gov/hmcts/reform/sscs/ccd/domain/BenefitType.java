package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class BenefitType {
    @CCD(
            label = "Benefit Code",
            showCondition = "code=\"DO_NOT_SHOW\""
    )
    private String code;

    @CCD(
            label = " ",
            showCondition = "code=\"DO_NOT_SHOW\""
    )
    private String description;

    @CCD(label = "Benefit Description")
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

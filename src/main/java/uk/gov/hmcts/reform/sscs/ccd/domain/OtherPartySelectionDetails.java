package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "otherPartySelection", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherPartySelectionDetails {
    @CCD(label = "Other party", typeOverride = FieldType.DynamicList)
    private DynamicList otherPartiesList;

    @JsonCreator
    public OtherPartySelectionDetails(@JsonProperty("otherPartiesList") DynamicList otherPartiesList) {
        this.otherPartiesList = otherPartiesList;
    }
}

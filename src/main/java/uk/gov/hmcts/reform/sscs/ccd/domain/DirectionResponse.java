package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "directionResponseColl", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DirectionResponse {

    @CCD(label = "-", typeOverride = FieldType.Collection, typeParameterOverride = "directionResponsedDocumentCT")
    private List<DirectionResponses> directionResponses;

    @JsonCreator
    public DirectionResponse(@JsonProperty("directionResponses") List<DirectionResponses> directionResponses) {
        this.directionResponses = directionResponses;
    }
}

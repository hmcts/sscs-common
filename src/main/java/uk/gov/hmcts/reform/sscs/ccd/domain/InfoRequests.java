package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "infoRequestsCT", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class InfoRequests {
    @CCD(
            label = "Information requests",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "appellantInfoRequestDetails"
    )
    @JsonProperty("appellantInfoRequestCollection")
    private List<AppellantInfoRequest> appellantInfoRequest;

    @JsonCreator
    public InfoRequests(@JsonProperty(
        value = "appellantInfoRequestCollection") List<AppellantInfoRequest> appellantInfoRequest
    ) {
        this.appellantInfoRequest = appellantInfoRequest;
    }
}

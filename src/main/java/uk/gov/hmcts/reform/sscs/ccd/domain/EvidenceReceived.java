package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "evidenceReceivedCollection", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvidenceReceived {
    @CCD(
            label = "Information requests",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "evidenceReceivedInformation"
    )
    private List<EvidenceReceivedInformation> appellantInfoRequestCollection;

    @JsonCreator
    public EvidenceReceived(@JsonProperty("appellantInfoRequestCollection") List<EvidenceReceivedInformation> appellantInfoRequestCollection) {
        this.appellantInfoRequestCollection = appellantInfoRequestCollection;
    }
}

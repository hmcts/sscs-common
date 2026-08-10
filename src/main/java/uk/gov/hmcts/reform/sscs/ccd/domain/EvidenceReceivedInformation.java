package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "evidenceReceivedInformation", generate = false)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class EvidenceReceivedInformation {

    private EvidenceReceivedInformationDetails value;

    @JsonCreator
    public EvidenceReceivedInformation(@JsonProperty("value") EvidenceReceivedInformationDetails value) {
        this.value = value;
    }
}

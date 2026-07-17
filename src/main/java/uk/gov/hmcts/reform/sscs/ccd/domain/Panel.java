package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class Panel {
    @CCD(label = "Judge")
    private String assignedTo;
    @CCD(label = "Medical member")
    private String medicalMember;
    @CCD(label = "Disability qualified member")
    private String disabilityQualifiedMember;

    @JsonCreator
    public Panel(@JsonProperty("assignedTo") String assignedTo,
                 @JsonProperty("medicalMember") String medicalMember,
                 @JsonProperty("disabilityQualifiedMember") String disabilityQualifiedMember) {
        this.assignedTo = assignedTo;
        this.medicalMember = medicalMember;
        this.disabilityQualifiedMember = disabilityQualifiedMember;
    }
}

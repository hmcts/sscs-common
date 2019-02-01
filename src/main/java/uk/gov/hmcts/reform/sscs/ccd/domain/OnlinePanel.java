package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class OnlinePanel {
    private String assignedTo;
    private String medicalMember;
    private String disabilityQualifiedMember;

    @JsonCreator
    public OnlinePanel(@JsonProperty(value = "assignedTo") String assignedTo,
                       @JsonProperty(value = "medicalMember") String medicalMember,
                       @JsonProperty(value = "disabilityQualifiedMember") String disabilityQualifiedMember) {
        this.assignedTo = assignedTo;
        this.medicalMember = medicalMember;
        this.disabilityQualifiedMember = disabilityQualifiedMember;
    }
}

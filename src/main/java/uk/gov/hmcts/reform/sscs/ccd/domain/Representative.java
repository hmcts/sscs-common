package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Representative {

    private String hasRepresentative;
    private String organisation;
    private Name name;
    private Address address;
    private Contact contact;
    private YesNo wantsReasonableAdjustment;
    private String reasonableAdjustmentRequirements;

    @JsonCreator
    public Representative(@JsonProperty("hasRepresentative") String hasRepresentative,
                          @JsonProperty("organisation") String organisation,
                          @JsonProperty("name") Name name,
                          @JsonProperty("address") Address address,
                          @JsonProperty("contact") Contact contact,
                          @JsonProperty("wantsReasonableAdjustment") YesNo wantsReasonableAdjustment,
                          @JsonProperty("reasonableAdjustmentRequirements") String reasonableAdjustmentRequirements) {
        this.hasRepresentative = hasRepresentative;
        this.organisation = organisation;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.wantsReasonableAdjustment = wantsReasonableAdjustment;
        this.reasonableAdjustmentRequirements = reasonableAdjustmentRequirements;
    }
}

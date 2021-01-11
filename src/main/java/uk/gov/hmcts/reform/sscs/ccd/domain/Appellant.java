package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Appellant {

    private Name name;
    private Address address;
    private Contact contact;
    private Identity identity;
    private String isAppointee;
    private Appointee appointee;
    private String isAddressSameAsAppointee;
    private YesNo wantsReasonableAdjustment;
    private String reasonableAdjustmentRequirements;

    @JsonCreator
    public Appellant(@JsonProperty("name") Name name,
                     @JsonProperty("address") Address address,
                     @JsonProperty("contact") Contact contact,
                     @JsonProperty("identity") Identity identity,
                     @JsonProperty("isAppointee") String isAppointee,
                     @JsonProperty("appointee") Appointee appointee,
                     @JsonProperty("isAddressSameAsAppointee") String isAddressSameAsAppointee,
                     @JsonProperty("wantsReasonableAdjustment") YesNo wantsReasonableAdjustment,
                     @JsonProperty("reasonableAdjustmentRequirements") String reasonableAdjustmentRequirements
                     ) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.identity = identity;
        this.isAppointee = isAppointee;
        this.appointee = appointee;
        this.isAddressSameAsAppointee = isAddressSameAsAppointee;
        this.wantsReasonableAdjustment = wantsReasonableAdjustment;
        this.reasonableAdjustmentRequirements = reasonableAdjustmentRequirements;
    }
}

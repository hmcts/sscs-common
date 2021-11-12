package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherParty {

    private String id;
    private Name name;
    private Address address;
    private Contact contact;
    private Role role;
    private YesNo confidentialityRequired;
    private YesNo unacceptableCustomerBehaviour;
    private HearingOptions hearingOptions;
    private HearingSubtype hearingSubtype;
    private String isAppointee;
    private Appointee appointee;
    private Representative rep;
    private ReasonableAdjustmentDetails reasonableAdjustment;
    private ReasonableAdjustmentDetails appointeeReasonableAdjustment;
    private ReasonableAdjustmentDetails repReasonableAdjustment;
}

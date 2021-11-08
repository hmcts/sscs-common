package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Appointee appointee;
    private Representative rep;
    private Subscription otherPartySubscription;
    private Subscription otherPartyAppointeeSubscription;
    private Subscription otherPartyRepresentativeSubscription;
    private YesNo sendNewOtherPartyNotification;
    private ReasonableAdjustmentDetails reasonableAdjustment;
    private ReasonableAdjustmentDetails appointeeReasonableAdjustment;
    private ReasonableAdjustmentDetails repReasonableAdjustment;

    @JsonIgnore
    public boolean hasAppointee() {
        return appointee != null && isYes(isAppointee);
    }

    @JsonIgnore
    public boolean hasRepresentative() {
        return rep != null && isYes(rep.getHasRepresentative());
    }
}

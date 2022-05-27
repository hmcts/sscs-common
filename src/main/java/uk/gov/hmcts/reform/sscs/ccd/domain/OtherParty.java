package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherParty extends Party {

    private YesNo showRole;

    private YesNo unacceptableCustomerBehaviour;

    private HearingOptions hearingOptions;
    private HearingSubtype hearingSubtype;

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
        return nonNull(getAppointee()) && isYes(getIsAppointee());
    }

    @JsonIgnore
    public boolean hasRepresentative() {
        return nonNull(rep) && isYes(rep.getHasRepresentative());
    }
}

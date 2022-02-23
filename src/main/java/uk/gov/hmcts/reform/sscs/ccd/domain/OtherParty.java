package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherParty extends Party {

    private HearingOptions hearingOptions;
    private HearingSubtype hearingSubtype;

    private Subscription otherPartyAppointeeSubscription;
    private Subscription otherPartyRepresentativeSubscription;

    private ReasonableAdjustmentDetails appointeeReasonableAdjustment;
    private ReasonableAdjustmentDetails repReasonableAdjustment;

    @JsonIgnore
    public boolean hasAppointee() {
        return isYes(getIsAppointee());
    }

    @JsonIgnore
    public boolean hasRepresentative() {
        return isYes(getHasRepresentative());
    }
}

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

    private HearingOptions hearingOptions;
    private HearingSubtype hearingSubtype;

    @JsonIgnore
    public boolean hasAppointee() {
        return isYes(getIsAppointee());
    }

    @JsonIgnore
    public boolean hasRepresentative() {
        return nonNull(getRep()) && isYes(getRep().getHasRepresentative());
    }

    public YesNo getSendNewOtherPartyNotification() {
        return getSendNotification();
    }

    public Subscription getOtherPartySubscription() {
        return getSubscription();
    }

    public Subscription getOtherPartyAppointeeSubscription() {
        return nonNull(getAppointee()) ? getAppointee().getSubscription() : null;
    }

    public Subscription getOtherPartyRepresentativeSubscription() {
        return nonNull(getRep()) ? getRep().getSubscription() : null;
    }

    public ReasonableAdjustmentDetails getAppointeeReasonableAdjustment() {
        return nonNull(getAppointee()) ? getAppointee().getReasonableAdjustment() : null;
    }

    public ReasonableAdjustmentDetails getRepReasonableAdjustment() {
        return nonNull(getRep()) ? getRep().getReasonableAdjustment() : null;
    }

    public void setHearingOptions(HearingOptions hearingOptions) {
        this.hearingOptions = hearingOptions;
    }

    public void setHearingSubtype(HearingSubtype hearingSubtype) {
        this.hearingSubtype = hearingSubtype;
    }

    public void setSendNewOtherPartyNotification(YesNo value) {
        setSendNotification(value);
    }

    public void setOtherPartySubscription(Subscription value) {
        setSubscription(value);
    }

    public void setOtherPartyAppointeeSubscription(Subscription value) {
        if (nonNull(getAppointee())) {
            getAppointee().setSubscription(value);
        }
    }

    public void setOtherPartyRepresentativeSubscription(Subscription value) {
        if (nonNull(getRep())) {
            getRep().setSubscription(value);
        }
    }

    public void setAppointeeReasonableAdjustment(ReasonableAdjustmentDetails value) {
        if (nonNull(getAppointee())) {
            getAppointee().setReasonableAdjustment(value);
        }
    }

    public void setRepReasonableAdjustment(ReasonableAdjustmentDetails value) {
        if (nonNull(getRep())) {
            getRep().setReasonableAdjustment(value);
        }
    }
}

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
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherParty extends Party {

    @CCD(
            label = "Show role for Child support",
            showCondition = "name=\"DUMMY_VALUE_TO_HIDE_FIELD\"",
            typeOverride = FieldType.YesOrNo
    )
    private YesNo showRole;

    @CCD(label = "Unacceptable Customer Behaviour", typeOverride = FieldType.YesOrNo)
    private YesNo unacceptableCustomerBehaviour;

    @CCD(label = "Hearing Options")
    private HearingOptions hearingOptions;
    @CCD(label = "Hearing Subtype")
    private HearingSubtype hearingSubtype;

    @CCD(label = "Representative Details")
    private Representative rep;

    @CCD(label = "Other party subscription")
    private Subscription otherPartySubscription;
    @CCD(label = "Other party appointee subscription")
    private Subscription otherPartyAppointeeSubscription;
    @CCD(label = "Other party representative subscription")
    private Subscription otherPartyRepresentativeSubscription;

    @CCD(
            label = "Send other party added notification",
            showCondition = "name=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.YesOrNo
    )
    private YesNo sendNewOtherPartyNotification;

    @CCD(label = "Reasonable Adjustment")
    private ReasonableAdjustmentDetails reasonableAdjustment;
    @CCD(label = "Other Party Appointee Reasonable Adjustment")
    private ReasonableAdjustmentDetails appointeeReasonableAdjustment;
    @CCD(label = "Other Party Representative Reasonable Adjustment")
    private ReasonableAdjustmentDetails repReasonableAdjustment;

    @CCD(label = "DV marker?")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNoUnknown domesticViolenceMarker;

    @JsonIgnore
    public boolean hasAppointee() {
        return nonNull(getAppointee()) && isYes(getIsAppointee());
    }

    @JsonIgnore
    public boolean hasRepresentative() {
        return nonNull(rep) && isYes(rep.getHasRepresentative());
    }
}

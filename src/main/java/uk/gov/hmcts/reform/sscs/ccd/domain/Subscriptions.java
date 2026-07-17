package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Subscriptions {
    @CCD(label = "Appellant Subscription")
    Subscription appellantSubscription;
    @CCD(label = "Supporter Subscription")
    Subscription supporterSubscription;
    @CCD(label = "Representative Subscription")
    Subscription representativeSubscription;
    @CCD(label = "Appointee Subscription")
    Subscription appointeeSubscription;
    @CCD(label = "Joint Party Subscription")
    Subscription jointPartySubscription;

    @JsonCreator
    public Subscriptions(@JsonProperty("appellantSubscription") Subscription appellantSubscription,
                         @JsonProperty("supporterSubscription") Subscription supporterSubscription,
                         @JsonProperty("representativeSubscription") Subscription representativeSubscription,
                         @JsonProperty("appointeeSubscription") Subscription appointeeSubscription,
                         @JsonProperty("jointPartySubscription") Subscription jointPartySubscription) {
        this.appellantSubscription = appellantSubscription;
        this.supporterSubscription = supporterSubscription;
        this.representativeSubscription = representativeSubscription;
        this.appointeeSubscription = appointeeSubscription;
        this.jointPartySubscription = jointPartySubscription;
    }
}

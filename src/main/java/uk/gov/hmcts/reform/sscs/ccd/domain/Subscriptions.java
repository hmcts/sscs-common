package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class Subscriptions {
    @Getter(AccessLevel.NONE) Subscription appellantSubscription;
    @Getter(AccessLevel.NONE) Subscription supporterSubscription;
    @Getter(AccessLevel.NONE) Subscription representativeSubscription;
    @Getter(AccessLevel.NONE) Subscription appointeeSubscription;

    @JsonCreator
    public Subscriptions(@JsonProperty("appellantSubscription") Subscription appellantSubscription,
                         @JsonProperty("supporterSubscription") Subscription supporterSubscription,
                         @JsonProperty("representativeSubscription") Subscription representativeSubscription,
                         @JsonProperty("appointeeSubscription") Subscription appointeeSubscription) {
        this.appellantSubscription = appellantSubscription;
        this.supporterSubscription = supporterSubscription;
        this.representativeSubscription = representativeSubscription;
        this.appointeeSubscription = appointeeSubscription;
    }


    public Subscription getAppellantSubscription() {
        return null != appellantSubscription ? appellantSubscription : Subscription.builder().build();
    }

    public Subscription getSupporterSubscription() {
        return null != supporterSubscription ? supporterSubscription : Subscription.builder().build();
    }

    public Subscription getRepresentativeSubscription() {
        return null != representativeSubscription ? representativeSubscription : Subscription.builder().build();
    }

    public Subscription getAppointeeSubscription() {
        return null != appointeeSubscription ? appointeeSubscription : Subscription.builder().build();
    }
}
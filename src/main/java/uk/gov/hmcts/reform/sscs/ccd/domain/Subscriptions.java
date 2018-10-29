package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class Subscriptions {
    Subscription appellantSubscription;
    Subscription representativeSubscription;

    @JsonCreator
    public Subscriptions(@JsonProperty("appellantSubscription") Subscription appellantSubscription,
                        @JsonProperty("representativeSubscription") Subscription representativeSubscription) {
        this.appellantSubscription = appellantSubscription;
        this.representativeSubscription = representativeSubscription;
    }
}

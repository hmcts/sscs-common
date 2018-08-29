package uk.gov.hmcts.reform.sscscorbackend.service.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Subscriptions {
    Subscription appellantSubscription;
    Subscription supporterSubscription;

    @JsonCreator
    public Subscriptions(@JsonProperty("appellantSubscription") Subscription appellantSubscription,
                        @JsonProperty("supporterSubscription") Subscription supporterSubscription) {
        this.appellantSubscription = appellantSubscription;
        this.supporterSubscription = supporterSubscription;
    }
}

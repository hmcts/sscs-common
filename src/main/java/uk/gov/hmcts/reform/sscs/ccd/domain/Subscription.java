package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Subscription {

    YesNo wantSmsNotifications;
    String tya;
    String email;
    String mobile;
    YesNo subscribeEmail;
    YesNo subscribeSms;
    String reason;
    String lastLoggedIntoMya;

    @JsonCreator
    public Subscription(@JsonProperty("wantSmsNotifications") YesNo wantSmsNotifications,
                        @JsonProperty("tya") String tya,
                        @JsonProperty("email") String email,
                        @JsonProperty("mobile") String mobile,
                        @JsonProperty("subscribeEmail") YesNo subscribeEmail,
                        @JsonProperty("subscribeSms") YesNo subscribeSms,
                        @JsonProperty("reason") String reason,
                        @JsonProperty("lastLoggedIntoMya") String lastLoggedIntoMya) {
        this.wantSmsNotifications = wantSmsNotifications;
        this.tya = tya;
        this.email = email;
        this.mobile = mobile;
        this.subscribeEmail = subscribeEmail;
        this.subscribeSms = subscribeSms;
        this.reason = reason;
        this.lastLoggedIntoMya = lastLoggedIntoMya;
    }

    @JsonIgnore
    public Boolean isSmsSubscribed() {
        return isYes(wantSmsNotifications) && isYes(subscribeSms);
    }

    @JsonIgnore
    public Boolean isEmailSubscribed() {
        return isYes(subscribeEmail);
    }

    @JsonIgnore
    public Boolean doesCaseHaveSubscriptions() {
        return isSmsSubscribed() || isEmailSubscribed();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return (this.wantSmsNotifications == null &&
                this.tya == null &&
                this.email == null &&
                this.mobile == null &&
                this.subscribeEmail == null &&
                this.subscribeSms == null &&
                this.reason == null &&
                this.lastLoggedIntoMya == null
            );
    }

}

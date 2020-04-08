package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Subscription {

    String wantSmsNotifications;
    String tya;
    String email;
    String mobile;
    String subscribeEmail;
    String subscribeSms;
    String reason;
    String lastLoggedIntoMya;

    @JsonCreator
    public Subscription(@JsonProperty("wantSmsNotifications") String wantSmsNotifications,
                        @JsonProperty("tya") String tya,
                        @JsonProperty("email") String email,
                        @JsonProperty("mobile") String mobile,
                        @JsonProperty("subscribeEmail") String subscribeEmail,
                        @JsonProperty("subscribeSms") String subscribeSms,
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
        if (StringUtils.isNotBlank(wantSmsNotifications)
            && wantSmsNotifications.equalsIgnoreCase("yes")) {
            return !StringUtils.isBlank(subscribeSms) && !subscribeSms.equalsIgnoreCase("no");
        }
        return false;
    }

    @JsonIgnore
    public Boolean isEmailSubscribed() {
        return !StringUtils.isBlank(subscribeEmail) && !subscribeEmail.equalsIgnoreCase("no");
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

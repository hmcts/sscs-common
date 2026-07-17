package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Subscription {

    @CCD(label = "Want SMS Notifications", typeOverride = FieldType.YesOrNo)
    String wantSmsNotifications;
    @CCD(label = "Track Your Appeal Number")
    String tya;
    @CCD(label = "Email Address")
    String email;
    @CCD(
            label = "Mobile Number",
            regex = "^((?:(?:\\(?(?:0(?:0|11)\\)?[\\s-]?\\(?|\\+)\\d{1,4}\\)?[\\s-]?(?:\\(?0\\)?[\\s-]?)?)|(?:\\(?0))(?:(?:\\d{5}\\)?[\\s-]?\\d{4,5})|(?:\\d{4}\\)?[\\s-]?(?:\\d{5}|\\d{3}[\\s-]?\\d{3}))|(?:\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{3,4})|(?:\\d{2}\\)?[\\s-]?\\d{4}[\\s-]?\\d{4}))(?:[\\s-]?(?:x|ext\\.?|\\#)\\d{3,4})?)?$"
    )
    String mobile;
    @CCD(label = "Subscribed to Email", typeOverride = FieldType.YesOrNo)
    String subscribeEmail;
    @CCD(label = "Subscribed to Text", showCondition = "wantSmsNotifications=\"Yes\"", typeOverride = FieldType.YesOrNo)
    String subscribeSms;
    @CCD(label = "Reason for not subscribing / unsubscribing")
    String reason;
    @CCD(label = "Last logged into MYA on", typeOverride = FieldType.DateTime)
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

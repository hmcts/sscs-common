package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SubscriptionTest {

    @Test
    public void givenExistingSubscriptionHasEmptyStrings_shouldNotSubscribe() {
        Subscription subscription = Subscription.builder()
            .mobile("")
            .email("")
            .subscribeEmail("")
            .subscribeSms("")
            .wantSmsNotifications("")
            .build();

        assertFalse(subscription.isSmsSubscribed());
    }
}
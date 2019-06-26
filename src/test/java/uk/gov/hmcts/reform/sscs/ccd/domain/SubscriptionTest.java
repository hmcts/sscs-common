package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class SubscriptionTest {

    @Test
    @Parameters({
        ",,false,false",
        "yes,yes,true,true",
        "no,yes,false,true",
        "yes,no,true,false",
        "null,null,false,false",
    })
    public void givenExistingSubscriptionHasEmptyStrings_shouldNotSubscribe(
        @Nullable String subscribeEmail, @Nullable String subscribeSms,
        boolean expectedEmailSubs, boolean expectedSmsSubs) {

        Subscription subscription = Subscription.builder()
            .mobile("")
            .email("")
            .subscribeEmail(subscribeEmail)
            .subscribeSms(subscribeSms)
            .wantSmsNotifications("")
            .build();

        assertEquals(expectedEmailSubs, subscription.isEmailSubscribed());
        assertEquals(expectedSmsSubs, subscription.isSmsSubscribed());
    }
}
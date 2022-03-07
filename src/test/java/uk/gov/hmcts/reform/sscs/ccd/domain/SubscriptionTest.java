package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class SubscriptionTest {

    @Test
    @Parameters(method = "generateEmailSubscriptionScenarios")
    public void givenSubscription_shouldReturnWhetherEmailSubscribedOrNotCorrectly(Subscription subscription,
                                                                                   boolean expectedEmailSubs) {
        assertEquals(expectedEmailSubs, subscription.isEmailSubscribed());
    }

    @SuppressWarnings("unused")
    private Object[] generateEmailSubscriptionScenarios() {
        Subscription subscriptionWithNullValues = Subscription.builder()
            .subscribeEmail(null)
            .wantSmsNotifications(null)
            .build();

        Subscription subscriptionIsNull = Subscription.builder().build();

        Subscription subscriptionWithSubscribedEmailToYes = Subscription.builder()
            .subscribeEmail(YES)
            .wantSmsNotifications(NO)
            .build();

        Subscription subscriptionWithSubscribedEmailToNo = Subscription.builder()
            .subscribeEmail(NO)
            .wantSmsNotifications(YES)
            .build();

        return new Object[]{
            new Object[]{subscriptionWithNullValues, false},
            new Object[]{subscriptionIsNull, false},
            new Object[]{subscriptionWithSubscribedEmailToYes, true},
            new Object[]{subscriptionWithSubscribedEmailToNo, false}
        };
    }

    @Test
    @Parameters(method = "generateSmsSubscriptionScenarios")
    public void givenSubscription_shouldReturnWhetherSmsSubscribedOrNotCorrectly(Subscription subscription,
                                                                                 boolean expectedSmsSubs) {
        assertEquals(expectedSmsSubs, subscription.isSmsSubscribed());
    }

    @SuppressWarnings("unused")
    private Object[] generateSmsSubscriptionScenarios() {
        Subscription subscriptionWithNullValues = Subscription.builder()
            .subscribeSms(null)
            .wantSmsNotifications(null)
            .build();

        Subscription subscriptionIsNull = Subscription.builder().build();

        Subscription subscriptionWithSubscribedSmsToYes = Subscription.builder()
            .subscribeSms(YES)
            .wantSmsNotifications(NO)
            .build();

        Subscription subscriptionWithSubscribedSmsToNo = Subscription.builder()
            .subscribeSms(NO)
            .wantSmsNotifications(NO)
            .build();

        Subscription subscriptionWithWantSmsNotificationsToYesAndSmsNo = Subscription.builder()
            .subscribeSms(NO)
            .wantSmsNotifications(YES)
            .build();

        Subscription subscriptionWithWantSmsNotificationsToYesAndSmsYes = Subscription.builder()
            .subscribeSms(YES)
            .wantSmsNotifications(YES)
            .build();

        return new Object[]{
            new Object[]{subscriptionWithNullValues, false},
            new Object[]{subscriptionIsNull, false},
            new Object[]{subscriptionWithSubscribedSmsToYes, false},
            new Object[]{subscriptionWithSubscribedSmsToNo, false},
            new Object[]{subscriptionWithWantSmsNotificationsToYesAndSmsNo, false},
            new Object[]{subscriptionWithWantSmsNotificationsToYesAndSmsYes, true}
        };
    }
}

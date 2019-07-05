package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

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
        Subscription subscriptionWithEmptyValues = Subscription.builder()
            .subscribeEmail("")
            .wantSmsNotifications("")
            .build();

        Subscription subscriptionWithNullValues = Subscription.builder()
            .subscribeEmail(null)
            .wantSmsNotifications(null)
            .build();

        Subscription subscriptionIsNull = Subscription.builder().build();

        Subscription subscriptionWithSubscribedEmailToYes = Subscription.builder()
            .subscribeEmail("Yes")
            .wantSmsNotifications("no")
            .build();

        Subscription subscriptionWithSubscribedEmailToNo = Subscription.builder()
            .subscribeEmail("no")
            .wantSmsNotifications("yes")
            .build();

        return new Object[]{
            new Object[]{subscriptionWithNullValues, false},
            new Object[]{subscriptionIsNull, false},
            new Object[]{subscriptionWithEmptyValues, false},
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
        Subscription subscriptionWithEmptyValues = Subscription.builder()
            .subscribeSms("")
            .wantSmsNotifications("")
            .build();

        Subscription subscriptionWithNullValues = Subscription.builder()
            .subscribeSms(null)
            .wantSmsNotifications(null)
            .build();

        Subscription subscriptionIsNull = Subscription.builder().build();

        Subscription subscriptionWithSubscribedSmsToYes = Subscription.builder()
            .subscribeSms("Yes")
            .wantSmsNotifications("")
            .build();

        Subscription subscriptionWithSubscribedSmsToNo = Subscription.builder()
            .subscribeSms("no")
            .wantSmsNotifications("")
            .build();


        Subscription subscriptionWithSubscribedEmailToYes = Subscription.builder()
            .subscribeSms("")
            .wantSmsNotifications("")
            .build();

        Subscription subscriptionWithSubscribedEmailToNo = Subscription.builder()
            .subscribeSms("")
            .wantSmsNotifications("")
            .build();

        Subscription subscriptionWithWantSmsNotificationsToYesAndSmsNo = Subscription.builder()
            .subscribeSms("no")
            .wantSmsNotifications("yes")
            .build();

        Subscription subscriptionWithWantSmsNotificationsToYesAndSmsYes = Subscription.builder()
            .subscribeSms("yes")
            .wantSmsNotifications("yes")
            .build();

        return new Object[]{
            new Object[]{subscriptionWithNullValues, false},
            new Object[]{subscriptionIsNull, false},
            new Object[]{subscriptionWithEmptyValues, false},
            new Object[]{subscriptionWithSubscribedSmsToYes, false},
            new Object[]{subscriptionWithSubscribedSmsToNo, false},
            new Object[]{subscriptionWithSubscribedEmailToYes, false},
            new Object[]{subscriptionWithSubscribedEmailToNo, false},
            new Object[]{subscriptionWithWantSmsNotificationsToYesAndSmsNo, false},
            new Object[]{subscriptionWithWantSmsNotificationsToYesAndSmsYes, true}
        };
    }
}
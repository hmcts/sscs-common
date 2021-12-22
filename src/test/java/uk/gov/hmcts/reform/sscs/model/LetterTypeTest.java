package uk.gov.hmcts.reform.sscs.model;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class LetterTypeTest {

    @Test
    @Parameters({"appellantLetter, APPELLANT", "representativeLetter, REPRESENTATIVE"})
    public void givenAFurtherEvidenceLetterType_thenFindLetterType(String letter, LetterType expected) {
        assertEquals(expected, LetterType.findLetterTypeFromFurtherEvidenceLetterType(letter));
    }

    @Test
    @Parameters({"APPELLANT, APPELLANT", "REPRESENTATIVE, REPRESENTATIVE", "APPOINTEE, APPOINTEE", "JOINT_PARTY, JOINT_PARTY", "OTHER_PARTY, OTHER_PARTY"})
    public void givenASubscriptionType_thenFindLetterType(String subscription, LetterType expected) {
        assertEquals(expected, LetterType.findLetterTypeFromSubscription(subscription));

    }

}
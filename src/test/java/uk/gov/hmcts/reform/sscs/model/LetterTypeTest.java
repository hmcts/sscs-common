package uk.gov.hmcts.reform.sscs.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LetterTypeTest {

    @ParameterizedTest
    @CsvSource({"appellantLetter, APPELLANT", "representativeLetter, REPRESENTATIVE"})
    public void givenAFurtherEvidenceLetterType_thenFindLetterType(String letter, LetterType expected) {
        assertEquals(expected, LetterType.findLetterTypeFromFurtherEvidenceLetterType(letter));
    }

    @ParameterizedTest
    @CsvSource({"APPELLANT, APPELLANT", "REPRESENTATIVE, REPRESENTATIVE", "APPOINTEE, APPOINTEE", "JOINT_PARTY, JOINT_PARTY", "OTHER_PARTY, OTHER_PARTY"})
    public void givenASubscriptionType_thenFindLetterType(String subscription, LetterType expected) {
        assertEquals(expected, LetterType.findLetterTypeFromSubscription(subscription));

    }

}
package uk.gov.hmcts.reform.sscs.model;

public enum LetterType {
    APPELLANT, REPRESENTATIVE, APPOINTEE, JOINT_PARTY, OTHER_PARTY;

    public static LetterType findLetterTypeFromFurtherEvidenceLetterType(String letter) {
        if (letter.equals("appellantLetter")) {
            return APPELLANT;
        } else if (letter.equals("representativeLetter")) {
            return REPRESENTATIVE;
        }
        return null;
    }

    public static LetterType findLetterTypeFromSubscription(String subscriptionType) {
        return LetterType.valueOf(subscriptionType.toUpperCase());
    }
}

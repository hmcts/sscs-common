package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum Outcome {
    DECISION_UPHELD("decisionUpheld"),
    DECISION_IN_FAVOUR_OF_APPELLANT("decisionInFavourOfAppellant"),
    ABATED("abated");

    private String id;

    public String getId() {
        return id;
    }

    Outcome(String id) {
        this.id = id;
    }
}

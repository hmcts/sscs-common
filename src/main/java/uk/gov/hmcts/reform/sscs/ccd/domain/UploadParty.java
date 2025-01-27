package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Arrays.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UploadParty {

    @JsonProperty("ctsc")
    CTSC("ctsc", "CTSC clerk"),
    @JsonProperty("appellant")
    APPELLANT("appellant", "Appellant"),
    @JsonProperty("appointee")
    APPOINTEE("appointee", "Appointee"),
    @JsonProperty("rep")
    REP("rep", "Representative"),
    @JsonProperty("jointParty")
    JOINT_PARTY("jointParty", "Joint party"),
    @JsonProperty("dwp")
    DWP("dwp", "DWP"),
    @JsonProperty("fta")
    FTA("fta", "FTA"),
    @JsonProperty("otherParty")
    OTHER_PARTY("otherParty", "Other party"),
    @JsonProperty("otherPartyRep")
    OTHER_PARTY_REP("otherPartyRep", "Other party representative"),
    @JsonProperty("otherPartyAppointee")
    OTHER_PARTY_APPOINTEE("otherPartyAppointee", "Other party appointee");

    private final String value;
    private final String label;

    UploadParty(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static UploadParty fromValue(String text) {

        return stream(UploadParty.values())
                .filter(party -> party.getValue().equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return value;
    }
}

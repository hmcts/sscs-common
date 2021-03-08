package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UploadParty {

    @JsonProperty("ctsc")
    CTSC("ctsc", "CTSC clerk"),
    @JsonProperty("appellant")
    APPELLANT("appellant", "Appellant"),
    @JsonProperty("appellant")
    APPOINTEE("appointee", "Appointee"),
    @JsonProperty("rep")
    REP("rep", "Representative"),
    @JsonProperty("jointParty")
    JOINT_PARTY("jointParty", "Joint party"),
    @JsonProperty("dwp")
    DWP("dwp", "DWP");

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

        for (UploadParty party : UploadParty.values()) {
            if (text != null && party.getValue() != null && party.getValue().equalsIgnoreCase(text)) {
                return party;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}

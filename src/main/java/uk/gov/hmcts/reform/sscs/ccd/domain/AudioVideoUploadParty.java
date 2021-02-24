package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AudioVideoUploadParty {

    @JsonProperty("ctsc")
    CTSC("caseworker-sscs-clerk", "CTSC clerk"),
    @JsonProperty("appellant")
    APPELLANT("appellant", "Appellant"),
    @JsonProperty("rep")
    REP("rep", "Representative"),
    @JsonProperty("jointParty")
    JOINT_PARTY("jointParty", "Joint party"),
    @JsonProperty("dwp")
    DWP("caseworker-sscs-dwpresponsewriter", "DWP");

    private final String value;
    private final String label;

    AudioVideoUploadParty(String value, String label) {
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

    public static AudioVideoUploadParty fromValue(String text) {

        for (AudioVideoUploadParty party : AudioVideoUploadParty.values()) {
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

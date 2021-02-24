package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum AudioVideoUploadParty {

    @JsonProperty("ctsc")
    CTSC("ctsc", "CTSC clerk"),
    @JsonProperty("appellant")
    APPELLANT("appellant", "Appellant"),
    @JsonProperty("rep")
    REP("rep", "Representative"),
    @JsonProperty("jointParty")
    JOINT_PARTY("jointParty", "Joint party"),
    @JsonProperty("dwp")
    DWP("dwp", "DWP");

    private final String value;
    private final String label;

    AudioVideoUploadParty(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static AudioVideoUploadParty fromValue(String text) {

        for (AudioVideoUploadParty party : AudioVideoUploadParty.values()) {
            if (text != null && party.getValue() != null && party.getValue().equalsIgnoreCase(text)) {
                return party;
            }
        }
        return null;
    }
}

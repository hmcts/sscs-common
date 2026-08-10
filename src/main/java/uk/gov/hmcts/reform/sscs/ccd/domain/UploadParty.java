package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Arrays.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_audioVideoPartyUploaded", generate = true)
public enum UploadParty {

    @CCD(label = "CTSC clerk")
    @JsonProperty("ctsc")
    CTSC("ctsc", "CTSC clerk"),
    @CCD(label = "Appellant")
    @JsonProperty("appellant")
    APPELLANT("appellant", "Appellant"),
    @CCD(label = "Appointee")
    @JsonProperty("appointee")
    APPOINTEE("appointee", "Appointee"),
    @CCD(label = "Representative")
    @JsonProperty("rep")
    REP("rep", "Representative"),
    @CCD(label = "Joint party")
    @JsonProperty("jointParty")
    JOINT_PARTY("jointParty", "Joint party"),
    @CCD(label = "FTA")
    @JsonProperty("dwp")
    DWP("dwp", "DWP"),
    @JsonProperty("fta")
    FTA("fta", "FTA"),
    @CCD(label = "Other party")
    @JsonProperty("otherParty")
    OTHER_PARTY("otherParty", "Other party"),
    @CCD(label = "Other party representative")
    @JsonProperty("otherPartyRep")
    OTHER_PARTY_REP("otherPartyRep", "Other party representative"),
    @CCD(label = "Other party appointee")
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

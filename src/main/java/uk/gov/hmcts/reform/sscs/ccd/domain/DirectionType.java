package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DirectionType {

    @JsonProperty("appealToProceed")
    APPEAL_TO_PROCEED("appealToProceed"),

    @JsonProperty("provideInformation")
    PROVIDE_INFORMATION("provideInformation"),

    @JsonProperty("grantExtension")
    GRANT_EXTENSION("grantExtension"),

    @JsonProperty("refuseExtension")
    REFUSE_EXTENSION("refuseExtension"),

    @JsonProperty("grantReinstatement")
    GRANT_REINSTATEMENT("grantReinstatement"),

    @JsonProperty("refuseReinstatement")
    REFUSE_REINSTATEMENT("refuseReinstatement");

    // needed only for the toString method
    private final String id;

    DirectionType(String id) {
        this.id = id;
    }

    // todo: get rid of the need to override toString as READ_ENUMS_USING_TO_STRING is enabled in other projects (i.e. remove READ_ENUMS_USING_TO_STRING)
    @Override
    public String toString() {
        return id;
    }
}

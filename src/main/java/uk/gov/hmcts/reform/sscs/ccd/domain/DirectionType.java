package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum DirectionType {

    @JsonProperty("appealToProceed")
    APPEAL_TO_PROCEED("appealToProceed", "Appeal to proceed"),

    @JsonProperty("provideInformation")
    PROVIDE_INFORMATION("provideInformation", "Provide information"),

    @JsonProperty("grantExtension")
    GRANT_EXTENSION("grantExtension", "Grant extension"),

    @JsonProperty("denyExtension")
    DENY_EXTENSION("denyExtension", "Deny extension");

    private final String id;
    private final String label;

    DirectionType(String id, String label) {
        this.id = id;
        this.label = label;
    }

    // todo: get rid of the need to override toString as READ_ENUMS_USING_TO_STRING is enabled in other projects (i.e. remove READ_ENUMS_USING_TO_STRING)
    @Override
    public String toString() {
        return id;
    }
}

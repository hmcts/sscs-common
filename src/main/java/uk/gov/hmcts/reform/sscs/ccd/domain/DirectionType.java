package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
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
}

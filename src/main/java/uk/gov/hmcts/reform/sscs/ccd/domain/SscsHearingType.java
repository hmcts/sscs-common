package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SscsHearingType {

    SUBSTANTIVE("substantive", "BBA3-substantive", "Substantive", "BBA3"),
    DIRECTIONHEARINGS("directionHearings", "BBA3-directionHearings", "Direction Hearings", "BBA3"),
    CHAMBERSOUTCOME("chambersOutcome", "BBA3-chambersOutcome", "Chambers Outcome", "BBA3"),
    @JsonEnumDefaultValue
    UNKNOWN("unknown", "unknown", "Unknown", "unknown");

    private final String type;
    private final String key;
    private final String descriptionEn;
    private final String serviceCode;

    SscsHearingType(String type, String key, String descriptionEn, String serviceCode) {
        this.type = type;
        this.key = key;
        this.descriptionEn = descriptionEn;
        this.serviceCode = serviceCode;
    }

    @JsonValue
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormType {

    @JsonProperty("sscs1")
    SSCS1("sscs1"),

    @JsonProperty("sscs1pe")
    SSCS1PE("sscs1pe"),

    @JsonProperty("sscs1peu")
    SSCS1PEU("sscs1peu"),

    @JsonProperty("sscs1u")
    SSCS1U("sscs1u"),

    @JsonProperty("sscs2")
    SSCS2("sscs2"),

    @JsonProperty("sscs5")
    SSCS5("sscs5"),

    @JsonProperty("sscs8")
    SSCS8("sscs8"),

    @JsonProperty("unknown")
    @JsonEnumDefaultValue
    UNKNOWN("unknown");
    private final String id;

    FormType(String id) {
        this.id = id;
    }

    public static FormType getById(String id) {
        for (FormType e : values()) {
            if (e.id.equalsIgnoreCase(id)) {
                return e;
            }
        }
        return UNKNOWN;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}

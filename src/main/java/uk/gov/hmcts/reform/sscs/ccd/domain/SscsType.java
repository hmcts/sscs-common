package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SscsType {

    @JsonProperty("sscs1")
    SSCS1("sscs1"),

    @JsonProperty("sscs2")
    SSCS2("sscs2"),

    @JsonProperty("sscs5")
    SSCS5("sscs5"),

    @JsonProperty("sscs8")
    SSCS8("sscs8");

    private final String id;

    SscsType(String id) {
        this.id = id;
    }

    public static SscsType getById(String id) {
        for (SscsType e : values()) {
            if (e.id.equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}

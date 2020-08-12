package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;

public enum SscsDocumentTranslationStatus {

    @JsonProperty("translationRequired")
    TRANSLATION_REQUIRED("translationRequired"),

    @JsonProperty("translationNotRequired")
    @JsonEnumDefaultValue
    TRANSLATION_NOT_REQUIRED("translationNotRequired"),

    @JsonProperty("translationRequested")
    TRANSLATION_REQUESTED("translationRequested"),

    @JsonProperty("translationComplete")
    TRANSLATION_COMPLETE("translationComplete");

    private final String id;

    SscsDocumentTranslationStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}

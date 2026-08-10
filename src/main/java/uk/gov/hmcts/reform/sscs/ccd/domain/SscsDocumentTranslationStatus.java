package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "documentTranslationStatus", generate = true)
public enum SscsDocumentTranslationStatus {

    @CCD(label = "Translation Required")
    @JsonProperty("translationRequired")
    TRANSLATION_REQUIRED("translationRequired"),

    @CCD(label = "Translation Not Required")
    @JsonProperty("translationNotRequired")
    @JsonEnumDefaultValue
    TRANSLATION_NOT_REQUIRED("translationNotRequired"),

    @CCD(label = "Translation Requested")
    @JsonProperty("translationRequested")
    TRANSLATION_REQUESTED("translationRequested"),

    @CCD(label = "Translation Complete")
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

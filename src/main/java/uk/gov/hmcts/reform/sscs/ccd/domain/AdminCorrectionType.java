package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminCorrectionType {
    BODY("bodyCorrection", "Body correction - Send to judge"),
    HEADER("headerCorrection", "Header correction");

    final String ccdDefinition;
    final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

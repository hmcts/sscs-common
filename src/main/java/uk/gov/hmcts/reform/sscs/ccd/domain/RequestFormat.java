package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_requestFormat", generate = true)
@Getter
@AllArgsConstructor
public enum RequestFormat {
    @CCD(label = "Enter request details")
    GENERATE("generate","Enter request details"),
    @CCD(label = "Upload request")
    UPLOAD("upload","Upload request");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

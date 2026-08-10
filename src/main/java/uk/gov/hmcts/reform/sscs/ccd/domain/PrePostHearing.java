package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_prePostHearing", generate = true)
@Getter
@AllArgsConstructor
public enum PrePostHearing {
    @CCD(label = "Pre Hearing")
    PRE("pre", "Pre Hearing"),
    @CCD(label = "Post Hearing")
    POST("post", "Post Hearing");

    private final String ccdDefinition;
    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

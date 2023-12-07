package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrePostHearing {
    PRE("pre", "Pre Hearing"),
    POST("post", "Post Hearing");

    private final String ccdDefinition;
    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

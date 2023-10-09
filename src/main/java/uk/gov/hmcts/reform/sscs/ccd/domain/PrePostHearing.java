package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrePostHearing {
    PRE("pre", "Pre Hearing"),
    POST("post", "Post Hearing");

    private final String value;
    private final String label;
}

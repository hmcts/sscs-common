package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminCorrectionType {
    BODY("Body correction - Send to judge"),
    HEADER("Header correction");

    final String description;
}

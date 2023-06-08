package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfidentialityType {

    GENERAL("general", "General"),
    CONFIDENTIAL("confidential", "Confidential");

    private final String code;
    private final String label;
}

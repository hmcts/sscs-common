package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PanelMemberType {
    // Note: these are based on mock data and subject to change.
    DQPM("BBA3-DQPM", "Disability Qualified Panel Member"),
    MQPM_1("BBA3-MQPM1", "Medically Qualified Panel Member"),
    MQPM_2("BBA3-MQPM2", "Medically Qualified Panel Member"),
    FQPM("BBA3-FQPM", "Financially Qualified Panel Member"),
    RMM("BBA3-RMM", "Regional Medical Member");

    private final String key;
    private final String description;
}

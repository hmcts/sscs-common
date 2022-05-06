package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMember {
    SALARIED_JUDGE("BBA3-?","Salaried Judge"),
    JUDGE("BBA3-?","Judge"),
    DOCTOR("BBA3-?","Doctor"),
    SPECIALIST_DOCTOR("BBA3-?","Specialist Doctor"),
    DQPM("BBA3-DQPM","Disability Qualified Panel Member"),
    FQPM("BBA3-FQPM","Financially Qualified Panel Member"),
    SPECIALIST_MEMBER("BBA3-?","Specialist Member");

    // TODO SSCS-10273 to add reference values

    private final String reference;
    private final String description;
}

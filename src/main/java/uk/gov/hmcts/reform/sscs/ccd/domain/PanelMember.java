package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMember {
    SALARIED_JUDGE("Salaried Judge"),
    JUDGE("Judge"),
    DOCTOR("Doctor"),
    SPECIALIST_DOCTOR("Specialist_Doctor"),
    DQPM("Disability Qualified Panel Member (DQPM)"),
    FQPM("Financially Qualified Panel Member (FQPM)"),
    SPECIALIST_MEMBER("Specialist Member");

    private final String name;
}

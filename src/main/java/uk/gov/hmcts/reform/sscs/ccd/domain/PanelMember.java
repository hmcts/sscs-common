package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberMedicallyQualified.getPanelMemberMedicallyQualified;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMember {
    // TODO These are based on mock data and subject to change.
    SALARIED_JUDGE("BBA3-?","Salaried Judge"),
    JUDGE("BBA3-?","Judge"),
    DOCTOR("BBA3-?","Doctor"),
    SPECIALIST_MEMBER("BBA3-?","Specialist Member"),
    DQPM("BBA3-DQPM", "Disability Qualified Panel Member"),
    MQPM1("BBA3-MQPM1", "Medically Qualified Panel Member"),
    MQPM2("BBA3-MQPM2", "Medically Qualified Panel Member"),
    FQPM("BBA3-FQPM", "Financially Qualified Panel Member"),
    RMM("BBA3-RMM", "Regional Medical Member");

    private final String reference;
    private final String description;

    public String getReference(String panelMemberSubtypeCcdRef) {
        PanelMemberMedicallyQualified subType = getPanelMemberMedicallyQualified(panelMemberSubtypeCcdRef);
        return nonNull(subType)
                ? String.format("%s-%s", reference, subType.getHmcReference())
                : reference;
    }
}

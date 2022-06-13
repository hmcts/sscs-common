package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberMedicallyQualified.getPanelMemberMedicallyQualified;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMember {
    // TODO These are based on mock data and subject to change.
//    SALARIED_JUDGE("BBA3-?","Salaried Judge"),
//    JUDGE("BBA3-?","Judge"),
//    DOCTOR("BBA3-?","Doctor"),
//    SPECIALIST_MEMBER("BBA3-?","Specialist Member"),
    DQPM(TRIBUNALS_MEMBER_DISABILITY.getReference(), TRIBUNALS_MEMBER_DISABILITY),
    MQPM1(TRIBUNALS_MEMBER_MEDICAL.getReference(), TRIBUNALS_MEMBER_MEDICAL),
    MQPM2(TRIBUNALS_MEMBER_MEDICAL.getReference(), TRIBUNALS_MEMBER_MEDICAL),
    FQPM(TRIBUNALS_MEMBER_FINANCIALLY_QUALIFIED.getReference(), TRIBUNALS_MEMBER_FINANCIALLY_QUALIFIED),
    RMM(TRIBUNALS_MEMBER_MEDICAL.getReference(), TRIBUNALS_MEMBER_MEDICAL);

    private final String reference;
    private final PanelMemberType panelMemberType;

    public String getReference(String panelMemberSubtypeCcdRef) {
        PanelMemberMedicallyQualified subType = getPanelMemberMedicallyQualified(panelMemberSubtypeCcdRef);
        return nonNull(subType)
                ? String.format("%s-%s", reference, subType.getHmcReference())
                : reference;
    }
}

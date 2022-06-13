package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberMedicallyQualified.getPanelMemberMedicallyQualified;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMember {

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

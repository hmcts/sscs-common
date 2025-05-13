package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMember {

    DQPM(TRIBUNAL_MEMBER_DISABILITY.toRef(), TRIBUNAL_MEMBER_DISABILITY),
    MQPM1(TRIBUNAL_MEMBER_MEDICAL.toRef(), TRIBUNAL_MEMBER_MEDICAL),
    MQPM2(TRIBUNAL_MEMBER_MEDICAL.toRef(), TRIBUNAL_MEMBER_MEDICAL),
    FQPM(TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef(), TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED),
    RMM(TRIBUNAL_MEMBER_MEDICAL.toRef(), TRIBUNAL_MEMBER_MEDICAL);

    private final String reference;
    private final PanelMemberType panelMemberType;
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMemberType {

    TRIBUNAL_MEMBER_DISABILITY("44", "Tribunal Member Disability"),
    TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED("50", "Tribunal Member Financially Qualified"),
    TRIBUNAL_MEMBER_MEDICAL("58", "Tribunal Member Medical"),
    REGIONAL_MEDICAL_MEMBER("69", "Regional Medical Member"),

    TRIBUNAL_PRESIDENT("65","President of Tribunal"),
    DISTRICT_TRIBUNAL_JUDGE("9000","District Tribunal Judge"),
    REGIONAL_TRIBUNAL_JUDGE("74","Regional Tribunal Judge"),
    TRIBUNAL_JUDGE("84","Tribunal Judge");

    private final String reference;
    private final String en;

    public static PanelMemberType getPanelMemberType(String reference) {
        return Arrays.stream(values())
                .filter(panelMember -> panelMember.reference.equals(reference))
                .findFirst()
                .orElse(null);
    }

    public String toRef() {
        return reference;
    }
}

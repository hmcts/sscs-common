package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMemberType {

    TRIBUNALS_MEMBER_DISABILITY("44", "Tribunal Member Disability"),
    TRIBUNALS_MEMBER_FINANCIALLY_QUALIFIED("50", "Tribunal Member Financially Qualified"),
    TRIBUNALS_MEMBER_MEDICAL("58", "Tribunal Member Medical"),
    REGIONAL_MEDICAL_MEMBER("69", "Regional Medical Member");

    private final String reference;
    private final String en;

    public static PanelMemberType getPanelMemberType(String reference) {
        return Arrays.stream(values())
                .filter(panelMember -> panelMember.reference.equals(reference))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return en;
    }
}

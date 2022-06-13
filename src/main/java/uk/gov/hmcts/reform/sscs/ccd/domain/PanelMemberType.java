package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMemberType {

    TRIBUNALS_MEMBER_DISABILITY(44, "Tribunal Member Disability"),
    TRIBUNALS_MEMBER_FINANCIALLY_QUALIFIED(50, "Tribunal Member Financially Qualified"),
    TRIBUNALS_MEMBER_LAY(55, "Tribunal Member Lay"),
    TRIBUNALS_MEMBER_MEDICAL(58, "Tribunal Member Medical"),
    TRIBUNALS_MEMBER_OPTOMETRIST(60, "Tribunal Member Optometrist"),
    TRIBUNALS_MEMBER_SERVICE(81, "Tribunal Member Service"),
    TRIBUNALS_MEMBER(85, "Tribunal Member");

    private final int key;
    private final String en;

    public static PanelMemberType getPanelMemberType(int key) {
        return Arrays.stream(values())
                .filter(panelMember -> panelMember.key == key)
                .findFirst()
                .orElse(null);
    }
}

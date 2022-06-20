package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JudgeType {

    PRESIDENT_OF_TRIBUNAL("65", "President of Tribunal"),
    REGIONAL_TRIBUNAL_JUDGE("74", "Regional Tribunal Judge"),
    TRIBUNAL_JUDGE("84", "Tribunal Judge");

    private final String reference;
    private final String en;

    public static JudgeType getJudgeType(String reference) {
        return Arrays.stream(values())
                .filter(panelMember -> panelMember.reference.equals(reference))
                .findFirst()
                .orElse(null);
    }

}

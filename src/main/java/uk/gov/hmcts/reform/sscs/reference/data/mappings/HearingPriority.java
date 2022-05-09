package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingPriority {

    NORMAL("normal", "Normal", "", ""),
    HIGH("high", "High", "", ""),
    CRITICAL("critical", "Critical", "", ""),
    PENDING("pending", "Pending", "", "");

    private final String key;
    private final String valueEn;
    private final String valueCy;
    private final String hintText;

    public static HearingPriority getHearingPriority(String value) {
        for (HearingPriority hp : HearingPriority.values()) {
            if (hp.getValueEn().equals(value)) {
                return hp;
            }
        }
        return null;
    }
}
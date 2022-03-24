package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingPriority {
    NORMAL("normal", "Normal"),
    HIGH("high", "High"),
    CRITICAL("critical", "Critical"),
    PENDING("pending", "Pending");

    private final String key;
    private final String value;

    public static HearingPriority getHearingPriority(String value) {
        for (HearingPriority hp : HearingPriority.values()) {
            if (hp.getValue().equals(value)) {
                return hp;
            }
        }
        return null;
    }
}

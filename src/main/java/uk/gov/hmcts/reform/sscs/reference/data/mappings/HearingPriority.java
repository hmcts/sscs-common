package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingPriority {

    NORMAL("normal", "Normal", null),
    HIGH("high", "High", null),
    CRITICAL("critical", "Critical", null),
    PENDING("pending", "Pending", null);

    private final String key;
    private final String valueEn;
    private final String valueCy;

}
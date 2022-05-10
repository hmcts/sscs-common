package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingPriority {

    NORMAL("normal", "Normal", null, null),
    HIGH("high", "High", null, null),
    CRITICAL("critical", "Critical", null, null),
    PENDING("pending", "Pending", null, null);

    private final String key;
    private final String valueEn;
    private final String valueCy;
    private final String hintText;

}
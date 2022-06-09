package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingPriority {

    STANDARD("standard", "Standard", null),
    URGENT("urgent", "Urgent", null);

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}

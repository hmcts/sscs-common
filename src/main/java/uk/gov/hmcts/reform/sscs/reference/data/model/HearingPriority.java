package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HearingPriority {

    STANDARD("Standard", "Standard", null),
    URGENT("Urgent", "Urgent", null);

    private final String hmcReference;
    private final String valueEn;
    private final String valueCy;

}

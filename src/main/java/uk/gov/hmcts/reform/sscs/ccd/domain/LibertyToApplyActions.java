package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LibertyToApplyActions implements CcdCallbackMap {
    REFUSE("refuse","Refuse Liberty to Apply Application", null, "", "", null);

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;
    private final DwpState postCallbackDwpState;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

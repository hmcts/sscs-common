package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionToAppealActions implements CcdCallbackMap {
    REFUSE("refuse","Refuse Permission to Appeal Application", null, "", "");

    private final String ccdDefinition;
    private final String descriptionEn;
    private final EventType callbackEvent;
    private final String callbackSummary;
    private final String callbackDescription;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

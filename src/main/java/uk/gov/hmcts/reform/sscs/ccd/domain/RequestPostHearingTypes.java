package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.DwpState.SET_ASIDE_REQUESTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.REQUEST_CORRECTION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.REQUEST_LIBERTY_APPLY;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.REQUEST_PERMISSION_APPEAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.REQUEST_SET_ASIDE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.REQUEST_SOR;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestPostHearingTypes implements CcdCallbackMap {
    SET_ASIDE("setAside","Set Aside", REQUEST_SET_ASIDE, "Set Aside Request Made", "Set Aside Request Made", SET_ASIDE_REQUESTED),
    CORRECTION("correction","Correction", REQUEST_CORRECTION, "Correction Request Made", "Correction Request Made", null),
    STATEMENT_OF_REASONS("statementOfReasons","Statement of Reasons", REQUEST_SOR, "Statement of Reasons Request Made", "Statement of Reasons Request Made", null),
    PERMISSION_TO_APPEAL("permissionToAppeal","Permission to Appeal", REQUEST_PERMISSION_APPEAL, "Permission to Appeal Request Made", "Permission to Appeal Request Made", null),
    LIBERTY_TO_APPLY("libertyToApply","Liberty to Apply", REQUEST_LIBERTY_APPLY, "Liberty to Apply Request Made", "Liberty to Apply Request Made", null);

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

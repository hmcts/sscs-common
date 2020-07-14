package uk.gov.hmcts.reform.sscs.model;

import static uk.gov.hmcts.reform.sscs.model.RefKeyField.ADMIN_TEAM_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.APPEAL_STATUS_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.BAT_CODE;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.BEN_ASSESS_TYPE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.BUSINESS_RULES_GRP_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.CASE_CODE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.DISTRICT_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.FUR_EVID_TYPE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.HEARING_OUTCOME_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.ITS_CENTRE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.LANGUAGE_TYPE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.OFFICE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.POSTPONE_REASON_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.PTTP_ROLE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.SEPARATE_CORRESPONDENCE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.TRIBUNAL_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.TRIBUNAL_TYPE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.VENUE_ID;

public enum RefKey {
    APPEAL_STATUS(APPEAL_STATUS_ID),
    CASE_CODE(CASE_CODE_ID),
    BUSINESS_RULES_GROUP(BUSINESS_RULES_GRP_ID),
    TRIBUNAL(TRIBUNAL_ID),
    TRIBUNAL_TYPE(TRIBUNAL_TYPE_ID),
    OFFICE(OFFICE_ID),
    BEN_ASSESS_TYPE(BEN_ASSESS_TYPE_ID),
    ADMIN_TEAM(ADMIN_TEAM_ID),
    VENUE(VENUE_ID),
    DISTRICT(DISTRICT_ID),
    ITS_CENTRE(ITS_CENTRE_ID),
    PTTP_ROLE(PTTP_ROLE_ID),
    SEPARATE_CORRESPONDENCE(SEPARATE_CORRESPONDENCE_ID),
    LANGUAGE_TYPE(LANGUAGE_TYPE_ID),
    FUR_EVID_TYPE(FUR_EVID_TYPE_ID),
    HEARING_OUTCOME(HEARING_OUTCOME_ID),
    POSTPONE_REASON(POSTPONE_REASON_ID),
    BAT_CODE_MAP(BAT_CODE);

    public final RefKeyField keyField;

    RefKey(RefKeyField caseCodeId) {

        keyField = caseCodeId;
    }
}

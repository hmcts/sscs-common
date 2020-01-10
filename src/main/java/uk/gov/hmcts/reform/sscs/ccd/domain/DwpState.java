package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum DwpState {
    WITHDRAWN("Withdrawn", "Withdrawn"),
    WITHDRAWAL_RECEIVED("withdrawalReceived", "Withdrawal received"),
    VALIDITY_CHALLENGE("validityChallenge", "Validity challenge"),
    SUPPLEMENTARY_RESPONSE("supplementaryResponse", "Supplementary response"),
    RESPONSE_SUBMITTED_DWP("responseSubmittedDwp", "Response submitted (DWP)"),
    REP_ADDED("repAdded", "Rep added"),
    HEARING_POSTPONED("hearingPostponed", "Postponed"),
    NO_ACTION("noAction", "No action"),
    LAPSED("lapsed", "Lapsed"),
    IN_PROGRESS("inProgress", "In progress"),
    FE_RECEIVED("feReceived", "FE received"),
    FE_ACTIONED_NR("feActionedNR", "FE Actioned - NR"),
    FE_ACTIONED_NA("feActionedNA", "FE Actioned - NA"),
    EXTENSION_REQUESTED("extensionRequested", "Extension requested"),
    STRIKE_OUT_ACTIONED("strikeOutActioned", "Strike-out actioned"),
    STRUCK_OUT("struckOut", "Struck out"),
    EXCEPTION("Exception", "Exception"),
    DIRECTION_RESPONDED("directionResponded", "Direction - responded"),
    DIRECTION_ACTION_REQUIRED("directionActionRequired", "Direction - action req'd"),
    UNREGISTERED("UnRegistered", "Appeal to-be registered"),
    REGISTERED("Registered", "Appeal registered (DWP)");

    private String id;
    private String label;

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    DwpState(String id, String label) {
        this.id = id;
        this.label = label;
    }
}

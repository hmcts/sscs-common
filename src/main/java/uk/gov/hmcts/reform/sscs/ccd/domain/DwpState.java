package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum DwpState {
    ADJOURNMENT_NOTICE_ISSUED("adjournmentNoticeIssued", "Adjournment notice issued"),
    APPEAL_ABATED("appealAbated", "Appeal abated"),
    APPOINTEE_DETAILS_NEEDED("appointeeDetailsNeeded", "Appointee details needed"),
    DECISION_ACTIONED("decisionActioned", "Decision actioned"),
    DIRECTION_ACTION_REQUIRED("directionActionRequired", "Direction - action req'd"),
    DIRECTION_RESPONDED("directionResponded", "Direction - responded"),
    EXCEPTION("Exception", "Exception"),
    EXTENSION_REQUESTED("extensionRequested", "Extension requested"),
    FE_ACTIONED_NA("feActionedNA", "FE Actioned - NA"),
    FE_ACTIONED_NR("feActionedNR", "FE Actioned - NR"),
    FE_RECEIVED("feReceived", "FE received"),
    FINAL_DECISION_ISSUED("finalDecisionIssued", "Final decision issued"),
    HEARING_DATE_ISSUED("hearingDateIssued", "Hearing date issued"),
    HEARING_POSTPONED("hearingPostponed", "Postponed"),
    HEARING_RECORDING_ACTIONED("hearingRecordingActioned","Hearing recording actioned"),
    HEARING_RECORDING_PROCESSED("hearingRecordingProcessed","Hearing recording processed"),
    IN_PROGRESS("inProgress", "In progress"),
    LAPSED("lapsed", "Lapsed"),
    NO_ACTION("noAction", "No action"),
    PHE_GRANTED("phmeGranted", "PHE granted"),
    PHE_REFUSED("phmeRefused", "PHE refused"),
    REGISTERED("Registered", "Appeal registered (FTA)"),
    REINSTATEMENT_GRANTED("reinstatementGranted", "Reinstatement Granted"),
    REINSTATEMENT_REFUSED("reinstatementRefused", "Reinstatement Refused"),
    REP_ADDED("repAdded", "Rep added"),
    RESPONSE_SUBMITTED_DWP("responseSubmittedDwp", "Response submitted (FTA)"),
    SET_ASIDE_REFUSED("setAsideRefused", "Set Aside Refused"),
    STRIKE_OUT_ACTIONED("strikeOutActioned", "Strike-out actioned"),
    STRUCK_OUT("struckOut", "Struck out"),
    SUPPLEMENTARY_RESPONSE("supplementaryResponse", "Supplementary response"),
    UNREGISTERED("UnRegistered", "Appeal to-be registered"),
    VALIDITY_CHALLENGE("validityChallenge", "Validity challenge"),
    WITHDRAWAL_RECEIVED("withdrawalReceived", "Withdrawal received"),
    WITHDRAWN("Withdrawn", "Withdrawn");

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

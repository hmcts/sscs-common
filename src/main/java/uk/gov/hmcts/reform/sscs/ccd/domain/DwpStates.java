package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DwpStates implements DwpState {
    ADJOURNMENT_NOTICE_ISSUED("adjournmentNoticeIssued", "Adjournment notice issued"),
    APPEAL_ABATED("appealAbated", "Appeal abated"),
    APPOINTEE_DETAILS_NEEDED("appointeeDetailsNeeded", "Appointee details needed"),
    CORRECTED_DECISION_NOTICE_ISSUED("correctedDecisionNoticeIssued", "Corrected decision notice issued"),
    CORRECTION_GRANTED("correctionGranted", "Correction Granted"),
    CORRECTION_REFUSED("correctionRefused", "Correction Refused"),
    CORRECTION_REQUESTED("correctionRequested", "Correction requested"),
    DECISION_REMADE("decisionRemade", "Decision remade"),
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
    LIBERTY_TO_APPLY_GRANTED("libertyToApplyGranted", "Liberty to apply granted"),
    LIBERTY_TO_APPLY_REFUSED("libertyToApplyRefused", "Liberty to apply refused"),
    LIBERTY_TO_APPLY_REQUESTED("libertyToApplyRequested", "Liberty to apply requested"),
    NO_ACTION("noAction", "No action"),
    PERMISSION_TO_APPEAL_GRANTED("permissionToAppealGranted", "Permission to Appeal Granted"),
    PERMISSION_TO_APPEAL_REFUSED("permissionToAppealRefused", "Permission to Appeal Refused"),
    PERMISSION_TO_APPEAL_REQUESTED("permissionToAppealRequested", "Permission to Appeal Requested"),
    PHE_GRANTED("phmeGranted", "PHE granted"),
    PHE_REFUSED("phmeRefused", "PHE refused"),
    REGISTERED("Registered", "Appeal registered (FTA)"),
    REINSTATEMENT_GRANTED("reinstatementGranted", "Reinstatement Granted"),
    REINSTATEMENT_REFUSED("reinstatementRefused", "Reinstatement Refused"),
    REP_ADDED("repAdded", "Rep added"),
    RESPONSE_SUBMITTED_DWP("responseSubmittedDwp", "Response submitted (FTA)"),
    SET_ASIDE_REFUSED("setAsideRefused", "Set Aside Refused"),
    SET_ASIDE_GRANTED("setAsideGranted", "Set Aside Granted"),
    SET_ASIDE_REQUESTED("setAsideRequested", "Set Aside Requested"),
    STATEMENT_OF_REASONS_GRANTED("statementOfReasonsGranted", "SOR Granted"),
    STATEMENT_OF_REASONS_ISSUED("statementOfReasonsIssued", "SOR Issued"),
    STATEMENT_OF_REASONS_REFUSED("statementOfReasonsRefused", "SOR Refused"),
    STATEMENT_OF_REASONS_REQUESTED("statementOfReasonsRequested", "SOR Requested"),
    STRIKE_OUT_ACTIONED("strikeOutActioned", "Strike-out actioned"),
    STRUCK_OUT("struckOut", "Struck out"),
    SUPPLEMENTARY_RESPONSE("supplementaryResponse", "Supplementary response"),
    UNREGISTERED("UnRegistered", "Appeal to-be registered"),
    VALIDITY_CHALLENGE("validityChallenge", "Validity challenge"),
    WITHDRAWAL_RECEIVED("withdrawalReceived", "Withdrawal received"),
    WITHDRAWN("Withdrawn", "Withdrawn");

    private final String ccdDefinition;
    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

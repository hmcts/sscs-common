package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Arrays.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_dwpStates", generate = true)
@Getter
@AllArgsConstructor
public enum DwpState {
    @CCD(label = "Adjournment notice issued")
    ADJOURNMENT_NOTICE_ISSUED("adjournmentNoticeIssued", "Adjournment notice issued"),
    @CCD(label = "Appeal abated")
    APPEAL_ABATED("appealAbated", "Appeal abated"),
    @CCD(label = "Appointee details needed")
    APPOINTEE_DETAILS_NEEDED("appointeeDetailsNeeded", "Appointee details needed"),
    @CCD(label = "Corrected decision notice issued")
    CORRECTED_DECISION_NOTICE_ISSUED("correctedDecisionNoticeIssued", "Corrected decision notice issued"),
    @CCD(label = "Correction Granted")
    CORRECTION_GRANTED("correctionGranted", "Correction Granted"),
    @CCD(label = "Correction Refused")
    CORRECTION_REFUSED("correctionRefused", "Correction Refused"),
    @CCD(label = "Correction Requested")
    CORRECTION_REQUESTED("correctionRequested", "Correction requested"),
    @CCD(label = "Review Decision")
    DECISION_REMADE("decisionRemade", "Decision remade"),
    @CCD(label = "Decision actioned")
    DECISION_ACTIONED("decisionActioned", "Decision actioned"),
    @CCD(label = "Direction - action req'd")
    DIRECTION_ACTION_REQUIRED("directionActionRequired", "Direction - action req'd"),
    @CCD(label = "Direction - responded")
    DIRECTION_RESPONDED("directionResponded", "Direction - responded"),
    @CCD(label = "Exception")
    EXCEPTION("Exception", "Exception"),
    @CCD(label = "Extension requested")
    EXTENSION_REQUESTED("extensionRequested", "Extension requested"),
    @CCD(label = "FE Actioned - NA")
    FE_ACTIONED_NA("feActionedNA", "FE Actioned - NA"),
    @CCD(label = "FE Actioned - NR")
    FE_ACTIONED_NR("feActionedNR", "FE Actioned - NR"),
    @CCD(label = "FE received")
    FE_RECEIVED("feReceived", "FE received"),
    @CCD(label = "Final decision issued")
    FINAL_DECISION_ISSUED("finalDecisionIssued", "Final decision issued"),
    @CCD(label = "Hearing Date Issued")
    HEARING_DATE_ISSUED("hearingDateIssued", "Hearing date issued"),
    @CCD(label = "Postponed")
    HEARING_POSTPONED("hearingPostponed", "Postponed"),
    @CCD(label = "Hearing recording actioned")
    HEARING_RECORDING_ACTIONED("hearingRecordingActioned","Hearing recording actioned"),
    @CCD(label = "Hearing recording processed")
    HEARING_RECORDING_PROCESSED("hearingRecordingProcessed","Hearing recording processed"),
    @CCD(label = "In progress")
    IN_PROGRESS("inProgress", "In progress"),
    @CCD(label = "Lapsed")
    LAPSED("lapsed", "Lapsed"),
    @CCD(label = "Liberty to Apply Granted")
    LIBERTY_TO_APPLY_GRANTED("libertyToApplyGranted", "Liberty to apply granted"),
    @CCD(label = "Liberty to Apply Refused")
    LIBERTY_TO_APPLY_REFUSED("libertyToApplyRefused", "Liberty to apply refused"),
    @CCD(label = "Liberty to Apply requested")
    LIBERTY_TO_APPLY_REQUESTED("libertyToApplyRequested", "Liberty to apply requested"),
    @CCD(label = "No action")
    NO_ACTION("noAction", "No action"),
    @CCD(label = "Permission to Appeal Granted")
    PERMISSION_TO_APPEAL_GRANTED("permissionToAppealGranted", "Permission to Appeal Granted"),
    @CCD(label = "Permission to Appeal Refused")
    PERMISSION_TO_APPEAL_REFUSED("permissionToAppealRefused", "Permission to Appeal Refused"),
    @CCD(label = "Permission to Appeal Requested")
    PERMISSION_TO_APPEAL_REQUESTED("permissionToAppealRequested", "Permission to Appeal Requested"),
    PHE_GRANTED("phmeGranted", "PHE granted"),
    PHE_REFUSED("phmeRefused", "PHE refused"),
    @CCD(label = "Appeal registered (FTA)")
    REGISTERED("Registered", "Appeal registered (FTA)"),
    @CCD(label = "Reinstatement Granted")
    REINSTATEMENT_GRANTED("reinstatementGranted", "Reinstatement Granted"),
    @CCD(label = "Reinstatement Refused")
    REINSTATEMENT_REFUSED("reinstatementRefused", "Reinstatement Refused"),
    @CCD(label = "Rep added")
    REP_ADDED("repAdded", "Rep added"),
    @CCD(label = "Response submitted (FTA)")
    RESPONSE_SUBMITTED_DWP("responseSubmittedDwp", "Response submitted (FTA)"),
    @CCD(label = "Set Aside Refused")
    SET_ASIDE_REFUSED("setAsideRefused", "Set Aside Refused"),
    @CCD(label = "Set Aside Granted")
    SET_ASIDE_GRANTED("setAsideGranted", "Set Aside Granted"),
    @CCD(label = "Set Aside Requested")
    SET_ASIDE_REQUESTED("setAsideRequested", "Set Aside Requested"),
    @CCD(label = "SOR Granted")
    STATEMENT_OF_REASONS_GRANTED("statementOfReasonsGranted", "SOR Granted"),
    @CCD(label = "SOR Issued")
    STATEMENT_OF_REASONS_ISSUED("statementOfReasonsIssued", "SOR Issued"),
    @CCD(label = "SOR Refused")
    STATEMENT_OF_REASONS_REFUSED("statementOfReasonsRefused", "SOR Refused"),
    @CCD(label = "SOR Requested")
    STATEMENT_OF_REASONS_REQUESTED("statementOfReasonsRequested", "SOR Requested"),
    @CCD(label = "Strike-out actioned")
    STRIKE_OUT_ACTIONED("strikeOutActioned", "Strike-out actioned"),
    @CCD(label = "Struck out")
    STRUCK_OUT("struckOut", "Struck out"),
    @CCD(label = "Supplementary response")
    SUPPLEMENTARY_RESPONSE("supplementaryResponse", "Supplementary response"),
    UNREGISTERED("UnRegistered", "Appeal to-be registered"),
    @CCD(label = "Validity challenge")
    VALIDITY_CHALLENGE("validityChallenge", "Validity challenge"),
    @CCD(label = "Withdrawal received")
    WITHDRAWAL_RECEIVED("withdrawalReceived", "Withdrawal received"),
    @CCD(label = "Withdrawn")
    WITHDRAWN("Withdrawn", "Withdrawn");

    private final String ccdDefinition;
    private final String description;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public static DwpState fromValue(String ccdDefinition) {
        return stream(values())
                .filter(dwpState -> dwpState.getCcdDefinition().equals(ccdDefinition))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public static List<DwpState> getPostHearingDwpStates() {
        return List.of(CORRECTED_DECISION_NOTICE_ISSUED, CORRECTION_GRANTED, CORRECTION_REFUSED, CORRECTION_REQUESTED,
                DECISION_REMADE, LIBERTY_TO_APPLY_GRANTED, LIBERTY_TO_APPLY_REFUSED, LIBERTY_TO_APPLY_REQUESTED,
                PERMISSION_TO_APPEAL_GRANTED, PERMISSION_TO_APPEAL_REFUSED, PERMISSION_TO_APPEAL_REQUESTED,
                SET_ASIDE_REFUSED, SET_ASIDE_GRANTED, SET_ASIDE_REQUESTED, STATEMENT_OF_REASONS_GRANTED,
                STATEMENT_OF_REASONS_ISSUED, STATEMENT_OF_REASONS_REFUSED, STATEMENT_OF_REASONS_REQUESTED);
    }
}

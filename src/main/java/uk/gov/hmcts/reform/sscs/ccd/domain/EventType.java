package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;
import java.util.Arrays;

public enum EventType {

    APPEAL_RECEIVED("appealReceived", "appealReceived", 1, true),
    DWP_RESPOND("dwpRespond", "responseReceived", 2, true),
    HEARING_BOOKED("hearingBooked", "hearingBooked", 3, true),
    HEARING("hearing", "hearing", 4, false),
    ADJOURNED("adjourned", "hearingAdjourned", 5, true),
    LAPSED_REVISED("lapsedRevised", "appealLapsed", 6, true),
    WITHDRAWN("withdrawn", "appealWithdrawn", 7, true),
    POSTPONED("postponed", "hearingPostponed", 8, true),
    NEW_HEARING_BOOKED("newHearingBooked", "newHearingBooked", 9, true),
    PAST_HEARING_BOOKED("pastHearingBooked", "pastHearingBooked", 10, true),
    DORMANT("dormant", "appealDormant", 11, false),
    CLOSED("closed", "appealClosed", 12, false),
    DWP_RESPOND_OVERDUE("dwpRespondOverdue", "responseOverdue", 13, true),
    EVIDENCE_RECEIVED("evidenceReceived", "evidenceReceived", -1, true),
    CREATE_DRAFT("createDraft", "createDraft", 0, false),
    UPDATE_DRAFT("updateDraft", "updateDraft", 0, false),
    DRAFT_ARCHIVED("draftArchived", "draftArchived", 0, false),
    EVIDENCE_REMINDER("evidenceReminder", "evidenceReminder", -2, true),
    SYA_APPEAL_CREATED("appealCreated", 0, true),
    SUBSCRIPTION_CREATED("subscriptionCreated", 0, true),
    SUBSCRIPTION_UPDATED("subscriptionUpdated", 0, true),
    FIRST_HEARING_HOLDING_REMINDER("hearingHoldingReminder", 0, true),
    SECOND_HEARING_HOLDING_REMINDER("secondHearingHoldingReminder", 0, true),
    THIRD_HEARING_HOLDING_REMINDER("thirdHearingHoldingReminder", 0, true),
    FINAL_HEARING_HOLDING_REMINDER("finalHearingHoldingReminder", 0, true),
    HEARING_REMINDER("hearingReminder", 0, true),
    DWP_RESPONSE_LATE_REMINDER("dwpResponseLateReminder", 0, false),
    NON_COMPLIANT("nonCompliant", 0, true),
    INCOMPLETE_APPLICATION_RECEIVED("incompleteApplicationReceived", 0, true),
    UPDATE_HEARING_TYPE("updateHearingType", 0, false),
    FINAL_DECISION("corDecision", 0, false),
    CASE_UPDATED("caseUpdated", 0, false),
    COH_QUESTION_ROUND_ISSUED("cohQuestionRoundIssued", 0, false),
    COH_QUESTION_DEADLINE_ELAPSED("cohQuestionDeadlineElapsed", 0, false),
    COH_QUESTION_DEADLINE_EXTENDED("cohQuestionDeadlineExtended", 0, false),
    COH_QUESTION_DEADLINE_EXTENSION_DENIED("cohQuestionDeadlineExtensionDenied", 0, false),
    COH_QUESTION_DEADLINE_EXTENSION_GRANTED("cohQuestionDeadlineExtensionGranted", 0, false),
    COH_QUESTION_DEADLINE_REMINDER("cohQuestionDeadlineReminder", 0, false),
    COH_ANSWERS_SUBMITTED("cohAnswersSubmitted", 0, false),
    COH_DECISION_REJECTED("cohDecisionRejected", 0, false),
    COH_ONLINE_HEARING_RELISTED("cohContinuousOnlineHearingRelisted", 0, false),
    COH_DECISION_ISSUED("cohDecisionIssued", 0, false),
    STRUCK_OUT("struckOut", 0, false),
    DIRECTION_ISSUED("directionIssued", 0, false),
    CREATE_BUNDLE("createBundle", 0, false),
    SEND_TO_DWP("sendToDwp", 0, false),
    SENT_TO_DWP("sentToDwp", 0, false),
    SENT_TO_DWP_ERROR("sendToDwpError", 0, false),
    REQUEST_INFO_INCOMPLETE("requestInfoIncompleteApplication", 0, false),
    CREATE_APPEAL_PDF("createAppealPDF", 0, false),
    RESEND_CASE_TO_GAPS2("resendCaseToGAPS2", 0, false),
    VALID_APPEAL("validAppeal", 0, false),
    INTERLOC_VALID_APPEAL("interlocValidAppeal", 0, false),
    VALID_APPEAL_CREATED("validAppealCreated", 0, false),
    MOVE_TO_APPEAL_CREATED("moveToAppealCreated", 0, false),
    ASSIGN_TO_JUDGE("assignToJudge", 0, false),
    EVENTS_UPDATES("eventsUpdates", 0, false),
    ADD_SC_NUMBER("addSCnumber", 0, false),
    UPLOAD_DOCUMENT("uploadDocument", 0, false),
    PANEL_UPDATE("panelUpdate", 0, false),
    UNASSIGN_TO_JUDGE("unassignToJudge", 0, false),
    UPLOAD_COR_DOCUMENT("uploadCorDocument", 0, false),
    UPDATE_INCOMPLETE_APPLICATION("updateIncompleteApplication", 0, false),
    VOID_INCOMPLETE_APPLICATION("voidIncompleteApplication", 0, false),
    ADD_REPRESENTATIVE("addRepresentative", 0, false),
    NON_COMPLIANT_SEND_TO_INTERLOC("nonCompliantSendToInterloc", 0, false),
    VOID_CASE("voidCase", 0, false),
    REINSTATE_APPEAL("reinstateAppeal", 0, false),
    REINSTATE_VOID_APPEAL("reinstateVoidAppeal", 0, false),
    ADD_NOTE("addNote", 0, false),
    SENT_TO_JUDGE("sentToJudge", 0, false),
    RESEND_APPEAL_CREATED("resendAppealCreated", 0, false),
    ATTACH_ROBOTICS_JSON("attachRoboticsJson", 0, false),
    INTERLOC_VOID_APPEAL("interlocVoidAppeal", 0, false),
    INTERLOC_SEND_TO_TCW("interlocSendToTcw", 0, false),
    TCW_DIRECTION_ISSUED("tcwDirectionIssued", 0, false),
    INTERLOC_INFORMATION_RECEIVED("interlocInformationReceived", 0, false),
    TCW_DECISION_STRIKE_OUT("tcwDecisionStrikeOut", 0, false),
    JUDGE_DECISION_ADMIT_APPEAL("judgeDecisionAdmitAppeal", 0, false),
    TCW_DECISION_ADMIT_APPEAL("tcwDecisionAdmitAppeal", 0, false),
    JUDGE_DIRECTION_ISSUED("judgeDirectionIssued", 0, false),
    JUDGE_DECISION_STRIKEOUT("judgeDecisionStrikeout", 0, false),
    TCW_REFER_TO_JUDGE("tcwReferToJudge", 0, false),
    EDIT_BUNDLE("editBundle", 0, false),
    STITCH_BUNDLE("stitchBundle", 0, false),
    SEND_TO_DWP_OFFLINE("sendToDwpOffline", 0, false),
    UPLOAD_DRAFT_DOCUMENT("uploadDraftDocument", 0, false),
    UPLOAD_DRAFT_COR_DOCUMENT("uploadDraftCorDocument", 0, false),
    JUDGE_DECISION_APPEAL_TO_PROCEED("judgeDecisionAppealToProceed", 0, false),
    TCW_DECISION_APPEAL_TO_PROCEED("tcwDecisionAppealToProceed", 0, false),
    ATTACH_SCANNED_DOCS("attachScannedDocs", 0, false),
    HANDLE_EVIDENCE("handleEvidence", 0, false),
    ACTION_FURTHER_EVIDENCE("actionFurtherEvidence", 0, false),
    UPLOAD_FURTHER_EVIDENCE("uploadFurtherEvidence", 0, false),
    ASSOCIATE_CASE("associateCase", 0, false),
    LINK_A_CASE("linkACase", 0, false),
    LOG_DOCS_TO_DWP("logDocsToDwp", 0, false),
    MAKE_CASE_URGENT("makeCaseUrgent", 0, false),
    DWP_APPEAL_REGISTERED("dwpAppealRegistered", 0, false),
    DWP_RAISE_EXCEPTION("dwpRaiseException", 0, false),
    DWP_NOT_ABLE_TO_REGISTER("dwpNotAbleToRegister", 0, false),
    DWP_UPLOAD_RESPONSE("dwpUploadResponse", 0, false),
    DWP_SUPPLEMENTARY_RESPONSE("dwpSupplementaryResponse", 0, false),
    DWP_NO_ACTION("dwpNoAction", 0, false),
    DWP_EVIDENCE_IN_PROGRESS("dwpEvidenceInProgress", 0, false),
    DWP_LAPSE_CASE("dwpLapseCase", "dwpLapseCase", 0, false),
    DWP_CASE_IN_PROGRESS("dwpCaseInProgress", 0, false),
    HMCTS_LAPSE_CASE("hmctsLapseCase", 0, false),
    HMCTS_RESPONSE_REVIEWED("hmctsResponseReviewed", 0, false),
    UPDATE_CASE_ONLY("updateCaseOnly", 0, false),
    REISSUE_FURTHER_EVIDENCE("reissueFurtherEvidence", 0, false),
    ISSUE_FURTHER_EVIDENCE("issueFurtherEvidence", 0, false),
    ADMIN_APPEAL_WITHDRAWN("adminAppealWithdrawn", 0, false);

    private String type;
    private String ccdType;
    private final int order;
    private boolean notifiable;

    EventType(String ccdType, int order, boolean notifiable) {
        this.ccdType = ccdType;
        this.order = order;
        this.notifiable = notifiable;
    }

    EventType(String type, String ccdType, int order, boolean notifiable) {
        this.type = type;
        this.ccdType = ccdType;
        this.order = order;
        this.notifiable = notifiable;
    }

    public String getType() {
        return type;
    }

    public String getCcdType() {
        return ccdType;
    }

    public int getOrder() {
        return order;
    }

    public boolean isStatusEvent() {
        return order > 0;
    }

    public String getContentKey() {
        return "status." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public boolean isNotifiable() {
        return notifiable;
    }

    public static EventType getEventTypeByCcdType(String ccdType) {
        EventType e = null;
        for (EventType event : EventType.values()) {
            if (event.getCcdType().equals(ccdType)) {
                e = event;
            }
        }
        return e;
    }

    @JsonCreator
    static EventType findValue(@JsonProperty("ccdType") String ccdType) {
        return Arrays.stream(EventType.values()).filter(pt -> pt.ccdType.equals(ccdType)).findFirst().get();
    }
}


package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.CaseFormat;
import java.util.Arrays;

public enum EventType {
    ABATE_CASE("abateCase", 0, false),
    ACTION_FURTHER_EVIDENCE("actionFurtherEvidence", 0, false),
    ACTION_HEARING_RECORDING_REQUEST("actionHearingRecordingRequest", 0, false),
    ACTION_POSTPONEMENT_REQUEST("actionPostponementRequest", 0, false),
    ACTION_POSTPONEMENT_REQUEST_WELSH("actionPostponementRequestWelsh", 0, false),
    ACTION_STRIKE_OUT("actionStrikeOut", 0, false),
    ADD_HEARING("addHearing", 0, false),
    ADD_NOTE("addNote", 0, false),
    ADD_REPRESENTATIVE("addRepresentative", 0, false),
    ADD_SC_NUMBER("addSCnumber", 0, false),
    ADJOURNED("adjourned", "hearingAdjourned", 5, true),
    ADJOURN_CASE("adjournCase", 0, false),
    ADMIN_ACTION_CORRECTION("adminActionCorrection", 0, false),
    ADMIN_APPEAL_WITHDRAWN("adminAppealWithdrawn", 0, false),
    ADMIN_CORRECTION_BODY("adminCorrectionBody", 0, false),
    ADMIN_CORRECTION_HEADER("adminCorrectionHeader", 0, true),
    ADMIN_RESTORE_CASES("adminRestoreCases", 0, false),
    ADMIN_SEND_TOR_RSPONSE_RECEIVED("adminSendTorRsponseReceived", 0, false),
    ADMIN_SEND_TO_APPEAL_CREATED("adminSendToAppealCreated", 0, false),
    ADMIN_SEND_TO_DORMANT_APPEAL_STATE("adminSendToDormantAppealState", 0, false),
    ADMIN_SEND_TO_DRAFT("adminSendToDraft", 0, false),
    ADMIN_SEND_TO_DRAFT_ARCHIVED("adminSendToDraftArchived", 0, false),
    ADMIN_SEND_TO_HEARING("adminSendToHearing", 0, false),
    ADMIN_SEND_TO_INCOMPLETE_APPLICATION("adminSendToIncompleteApplication", 0, false),
    ADMIN_SEND_TO_INCOMPLETE_APPLICATION_INFORMATION_REQSTED("adminSendToIncompleteApplicationInformationReqsted", 0, false),
    ADMIN_SEND_TO_INTERLOCUTORY_REVIEW_STATE("adminSendToInterlocutoryReviewState", 0, false),
    ADMIN_SEND_TO_READY_TO_LIST("adminSendToReadyToList", 0, false),
    ADMIN_SEND_TO_VALID_APPEAL("adminSendToValidAppeal", 0, false),
    ADMIN_SEND_TO_VOID_STATE("adminSendToVoidState", 0, false),
    ADMIN_SEND_TO_WITH_DWP("adminSendToWithDwp", 0, false),
    ADMIN_UPDATE_EVENT("adminUpdateEvent", 0, false),
    AMEND_DUE_DATE("amendDueDate", 0, false),
    AMEND_ELEMENTS_ISSUES("amendElementsIssues", 0, false),
    APPEAL_RECEIVED("appealReceived", "appealReceived", 1, true),
    APPEAL_TO_PROCEED("appealToProceed", 0, false),
    ASSIGN_TO_JUDGE("assignToJudge", 0, false),
    ASSOCIATE_CASE("associateCase", 0, false),
    ASYNC_STITCHING_COMPLETE("asyncStitchingComplete", 0, false),
    ATTACH_ROBOTICS_JSON("attachRoboticsJson", 0, false),
    ATTACH_SCANNED_DOCS("attachScannedDocs", 0, false),
    CANCEL_TRANSLATIONS("cancelTranslations", 0, false),
    CASE_UPDATED("caseUpdated", 0, false),
    CCD_ADMIN("CCD_ADMIN", 0, false),
    CITIZEN_REQUEST_HEARING_RECORDING("citizenRequestHearingRecording", 0, false),
    CLONE_BUNDLE("cloneBundle", 0, false),
    CLOSED("closed", "appealClosed", 12, false),
    COH_ANSWERS_SUBMITTED("cohAnswersSubmitted", 0, false),
    COH_DECISION_ISSUED("cohDecisionIssued", 0, false),
    COH_DECISION_REJECTED("cohDecisionRejected", 0, false),
    COH_ONLINE_HEARING_RELISTED("cohContinuousOnlineHearingRelisted", 0, false),
    COH_QUESTION_DEADLINE_ELAPSED("cohQuestionDeadlineElapsed", 0, false),
    COH_QUESTION_DEADLINE_EXTENDED("cohQuestionDeadlineExtended", 0, false),
    COH_QUESTION_DEADLINE_EXTENSION_DENIED("cohQuestionDeadlineExtensionDenied", 0, false),
    COH_QUESTION_DEADLINE_EXTENSION_GRANTED("cohQuestionDeadlineExtensionGranted", 0, false),
    COH_QUESTION_DEADLINE_REMINDER("cohQuestionDeadlineReminder", 0, false),
    COH_QUESTION_ROUND_ISSUED("cohQuestionRoundIssued", 0, false),
    CONFIRM_LAPSED("confirmLapsed", 0, false),
    CONFIRM_PANEL_COMPOSITION("confirmPanelComposition", 0, false),
    CORRECTION_GRANTED("correctionGranted", 0, true),
    CORRECTION_REFUSED("correctionRefused", 0, true),
    CORRECTION_REQUEST("correctionRequest", 0, false),
    CREATE_APPEAL_PDF("createAppealPDF", 0, false),
    CREATE_BUNDLE("createBundle", 0, false),
    CREATE_DRAFT("createDraft", "createDraft", 0, false),
    CREATE_EDITED_BUNDLE("createEditedBundle", 0, false),
    CREATE_RESPONSE_RECEIVED_TEST_CASE("createResponseReceivedTestCase", 0, false),
    CREATE_TEST_CASE("createTestCase", 0, false),
    CREATE_WELSH_NOTICE("createWelshNotice", 0, false),
    CREATE_WITH_DWP_TEST_CASE("createWithDwpTestCase", 0, false),
    DEATH_OF_APPELLANT("deathOfAppellant", 0, false),
    DEATH_OF_APPELLANT_ACTIONED("deathOfAppellantActioned", 0, false),
    DECISION_ISSUED("decisionIssued", 0, false),
    DECISION_ISSUED_WELSH("decisionIssuedWelsh", 0, false),
    DIRECTION_ISSUED("directionIssued", 0, false),
    DIRECTION_ISSUED_WELSH("directionIssuedWelsh", 0, false),
    DORMANT("dormant", "appealDormant", 11, false),
    DRAFT_ARCHIVED("draftArchived", "draftArchived", 0, false),
    DRAFT_TO_INCOMPLETE_APPLICATION("draftToIncompleteApplication", 0, false),
    DRAFT_TO_NON_COMPLIANT("draftToNonCompliant", 0, false),
    DRAFT_TO_VALID_APPEAL_CREATED("draftToValidAppealCreated", 0, false),
    DWP_ACTION_DIRECTION("dwpActionDirection", 0, false),
    DWP_ACTION_REP_ADDED("dwpActionRepAdded", 0, false),
    DWP_ACTION_WITHDRAWAL("dwpActionWithdrawal", 0, false),
    DWP_APPEAL_REGISTERED("dwpAppealRegistered", 0, false),
    DWP_CASE_IN_PROGRESS("dwpCaseInProgress", 0, false),
    DWP_CHALLENGE_VALIDITY("dwpChallengeValidity", 0, false),
    DWP_DIRECTION_RESPONSE("dwpDirectionResponse", 0, false),
    DWP_EVIDENCE_IN_PROGRESS("dwpEvidenceInProgress", 0, false),
    DWP_LAPSE_CASE("dwpLapseCase", "dwpLapseCase", 0, false),
    DWP_NOT_ABLE_TO_REGISTER("dwpNotAbleToRegister", 0, false),
    DWP_NO_ACTION("dwpNoAction", 0, false),
    DWP_RAISE_EXCEPTION("dwpRaiseException", 0, false),
    DWP_REQUEST_HEARING_RECORDING("dwpRequestHearingRecording", 0, false),
    DWP_REQUEST_TIME_EXTENSION("dwpRequestTimeExtension", 0, false),
    DWP_RESPOND("dwpRespond", "responseReceived", 2, true),
    DWP_RESPOND_OVERDUE("dwpRespondOverdue", "responseOverdue", 13, true),
    DWP_RESPONSE_LATE_REMINDER("dwpResponseLateReminder", 0, false),
    DWP_SUPPLEMENTARY_RESPONSE("dwpSupplementaryResponse", 0, false),
    DWP_UPLOAD_RESPONSE("dwpUploadResponse", 0, false),
    EDIT_BUNDLE("editBundle", 0, false),
    EVENTS_UPDATES("eventsUpdates", 0, false),
    EVIDENCE_RECEIVED("evidenceReceived", "evidenceReceived", -1, true),
    EVIDENCE_REMINDER("evidenceReminder", "evidenceReminder", -2, true),
    FE_NO_ACTION("feNoAction", 0, false),
    FINAL_DECISION("corDecision", 0, false),
    FINAL_HEARING_HOLDING_REMINDER("finalHearingHoldingReminder", 0, true),
    FIRST_HEARING_HOLDING_REMINDER("hearingHoldingReminder", 0, true),
    FURTHER_EVIDENCE_HANDLED_OFFLINE("furtherEvidenceHandledOffline", 0, false),
    GENERATE_COVERSHEET("generateCoversheet", 0, false),
    HANDLE_EVIDENCE("handleEvidence", 0, false),
    HANDLING_ERROR("handlingError", 0, false),
    HEARING("hearing", "hearing", 4, false),
    HEARING_BOOKED("hearingBooked", "hearingBooked", 3, true),
    HEARING_REMINDER("hearingReminder", 0, true),
    HMCTS_LAPSE_CASE("hmctsLapseCase", 0, false),
    HMCTS_RESPONSE_REVIEWED("hmctsResponseReviewed", 0, false),
    INCOMPLETE_APPLICATION_RECEIVED("incompleteApplicationReceived", 0, true),
    INTERLOC_INFORMATION_RECEIVED("interlocInformationReceived", 0, false),
    INTERLOC_INFORMATION_RECEIVED_ACTION_FURTHER_EVIDENCE("interlocInformationReceivedActionFurtherEvidence", 0, false),
    INTERLOC_REVIEW_STATE_AMEND("interlocReviewStateAmend", 0, false),
    INTERLOC_SEND_TO_TCW("interlocSendToTcw", 0, false),
    INTERLOC_VALID_APPEAL("interlocValidAppeal", 0, false),
    INTERLOC_VOID_APPEAL("interlocVoidAppeal", 0, false),
    ISSUE_ADJOURNMENT_NOTICE("issueAdjournmentNotice", 0, false),
    ISSUE_ADJOURNMENT_NOTICE_WELSH("issueAdjournmentNoticeWelsh", 0, false),
    ISSUE_FINAL_DECISION("issueFinalDecision", 0, false),
    ISSUE_FINAL_DECISION_WELSH("issueFinalDecisionWelsh", 0, false),
    ISSUE_FURTHER_EVIDENCE("issueFurtherEvidence", 0, false),
    ISSUE_GENERIC_LETTER("issueGenericLetter", 0, false),
    JOINT_PARTY_ADDED("jointPartyAdded", 0, false),
    JUDGE_DECISION_ADMIT_APPEAL("judgeDecisionAdmitAppeal", 0, false),
    JUDGE_DECISION_APPEAL_TO_PROCEED("judgeDecisionAppealToProceed", 0, false),
    JUDGE_DECISION_STRIKEOUT("judgeDecisionStrikeout", 0, false),
    JUDGE_DIRECTION_ISSUED("judgeDirectionIssued", 0, false),
    LAPSED_REVISED("lapsedRevised", "appealLapsed", 6, true),
    LIBERTY_TO_APPLY_GRANTED("libertyToApplyGranted", 0, true),
    LIBERTY_TO_APPLY_REFUSED("libertyToApplyRefused", 0, true),
    LIBERTY_TO_APPLY_REQUEST("libertyToApplyRequest", 0, false),
    LINK_A_CASE("linkACase", 0, false),
    LISTING_ERROR("listingError", 0, false),
    LOG_DOCS_TO_DWP("logDocsToDwp", 0, false),
    MAKE_CASE_URGENT("makeCaseUrgent", 0, false),
    MANAGE_DWP_DOCUMENTS("manageDwpDocuments", 0, false),
    MANAGE_WELSH_DOCUMENTS("manageWelshDocuments", 0, false),
    MARK_DOCS_FOR_TRANSATION("markDocumentsForTranslation", 0, false),
    MOVE_TO_APPEAL_CREATED("moveToAppealCreated", 0, false),
    NEW_HEARING_BOOKED("newHearingBooked", "newHearingBooked", 9, true),
    NON_COMPLIANT("nonCompliant", 0, true),
    NON_COMPLIANT_SEND_TO_INTERLOC("nonCompliantSendToInterloc", 0, false),
    NOTIFICATION_SENT("notificationSent", 0, false),
    NOT_LISTABLE("notListable", 0, false),
    PANEL_UPDATE("panelUpdate", 0, false),
    PAST_HEARING_BOOKED("pastHearingBooked", "pastHearingBooked", 10, true),
    PERMISSION_TO_APPEAL_GRANTED("permissionToAppealGranted", 0, true),
    PERMISSION_TO_APPEAL_REFUSED("permissionToAppealRefused", 0, true),
    PERMISSION_TO_APPEAL_REQUEST("permissionToAppealRequest", 0, false),
    POSTPONED("postponed", "hearingPostponed", 8, true),
    POSTPONEMENT_REQUEST("postponementRequest", 0, false),
    POSTPONEMENT_GRANTED("postponementGranted", 0, false),
    POSTPONEMENT_REFUSED("postponementRefused", 0, false),
    POSTPONEMENT_SEND_TO_JUDGE("postponementSendToJudge", 0, false),
    POST_HEARING_APP_SOR_WRITTEN("postHearingAppSorWritten", 0, true),
    POST_HEARING_OTHER("postHearingOther", 0, false),
    POST_HEARING_REQUEST("postHearingRequest", 0, false),
    POST_HEARING_REVIEW("postHearingReview", 0, false),
    PROCESS_AUDIO_VIDEO("processAudioVideo", 0, false),
    PROCESS_AUDIO_VIDEO_WELSH("processAudioVideoWelsh", 0, false),
    PROCESS_REASONABLE_ADJUSTMENT("processReasonableAdjustment", 0, false),
    PROVIDE_APPOINTEE_DETAILS("provideAppointeeDetails", 0, false),
    READY_TO_LIST("readyToList", 0, false),
    REINSTATE_APPEAL("reinstateAppeal", 0, false),
    REINSTATE_VOID_APPEAL("reinstateVoidAppeal", 0, false),
    REISSUE_DOCUMENT("reissueDocument", 0, false),
    REISSUE_FURTHER_EVIDENCE("reissueFurtherEvidence", 0, false),
    REMOVE_LINK_FOR_CASE("removeLinkForCase", 0, false),
    REQUEST_FOR_INFORMATION("requestForInformation", 0, false),
    REQUEST_TRANSLATION_FROM_WLU("requestTranslationFromWLU", 0, false),
    RESEND_APPEAL_CREATED("resendAppealCreated", 0, false),
    RESEND_CASE_TO_GAPS2("resendCaseToGAPS2", 0, false),
    RESEND_TO_DWP("resendToDwp", 0, false),
    REVIEW_CONFIDENTIALITY_REQUEST("reviewConfidentialityRequest", 0, false),
    REVIEW_PHME_REQUEST("reviewPhmeRequest", 0, false),
    SECOND_HEARING_HOLDING_REMINDER("secondHearingHoldingReminder", 0, true),
    SEND_FURTHER_EVIDENCE_ERROR("sendFurtherEvidenceError", 0, false),
    SEND_TO_ADMIN("sendToAdmin", 0, false),
    SEND_TO_DWP("sendToDwp", 0, false),
    SEND_TO_DWP_OFFLINE("sendToDwpOffline", 0, false),
    SEND_TO_FIRST_TIER("sendToFirstTier", 0, false),
    SEND_TO_ROBOTICS_ERROR("sendToRoboticsError", 0, false),
    SEND_VALID_CASE_TO_INTERLOC("sendValidCaseToInterloc", 0, false),
    SENT_TO_DWP("sentToDwp", 0, false),
    SENT_TO_DWP_ERROR("sendToDwpError", 0, false),
    SENT_TO_JUDGE("sentToJudge", 0, false),
    SET_ASIDE_GRANTED("setAsideGranted", 0, true),
    SET_ASIDE_REFUSED("setAsideRefused", 0, true),
    SET_ASIDE_REFUSED_SOR("setAsideRefusedSOR", 0, true),
    SET_ASIDE_REQUEST("setAsideRequest", 0, false),
    REVIEW_AND_SET_ASIDE("reviewAndSetAside", 0, true),
    SOR_EXTEND_TIME("sORExtendTime", 0, true),
    SOR_REFUSED("sORRefused", 0, true),
    SOR_REQUEST("sORRequest", 0, false),
    SOR_WRITE("sORWrite", 0, true),
    STITCH_BUNDLE("stitchBundle", 0, false),
    STOP_BULK_PRINT_FOR_REASONABLE_ADJUSTMENT("stopBulkPrintForReasonableAdjustment", 0, false),
    STRUCK_OUT("struckOut", 0, false),
    SUBSCRIPTION_CREATED("subscriptionCreated", 0, true),
    SUBSCRIPTION_UPDATED("subscriptionUpdated", 0, true),
    SYA_APPEAL_CREATED("appealCreated", 0, true),
    SYSTEM_MAINTENANCE("SYSTEM_MAINTENANCE", 0, false),
    TCW_DECISION_ADMIT_APPEAL("tcwDecisionAdmitAppeal", 0, false),
    TCW_DECISION_APPEAL_TO_PROCEED("tcwDecisionAppealToProceed", 0, false),
    TCW_DECISION_STRIKE_OUT("tcwDecisionStrikeOut", 0, false),
    TCW_DIRECTION_ISSUED("tcwDirectionIssued", 0, false),
    TCW_REFER_TO_JUDGE("tcwReferToJudge", 0, false),
    THIRD_HEARING_HOLDING_REMINDER("thirdHearingHoldingReminder", 0, true),
    UNASSIGN_TO_JUDGE("unassignToJudge", 0, false),
    UPDATE_CASE_ONLY("updateCaseOnly", 0, false),
    UPDATE_DRAFT("updateDraft", "updateDraft", 0, false),
    UPDATE_HEARING_TYPE("updateHearingType", 0, false),
    UPDATE_INCOMPLETE_APPLICATION("updateIncompleteApplication", 0, false),
    UPDATE_LISTING_REQUIREMENTS("updateListingRequirements", 0, false),
    UPDATE_NOT_LISTABLE("updateNotListable", 0, false),
    UPDATE_OTHER_PARTY_DATA("updateOtherPartyData", 0, false),
    UPDATE_REASONABLE_ADJUSTMENT("updateReasonableAdjustment", 0, false),
    UPDATE_UCB("updateUCB", 0, false),
    UPDATE_WELSH_PREFERENCE("updateWelshPreference", 0, false),
    UPLOAD_COR_DOCUMENT("uploadCorDocument", 0, false),
    UPLOAD_DOCUMENT("uploadDocument", 0, false),
    UPLOAD_DOCUMENT_FURTHER_EVIDENCE("uploadDocumentFurtherEvidence", 0, false),
    UPLOAD_DRAFT_COR_DOCUMENT("uploadDraftCorDocument", 0, false),
    UPLOAD_DRAFT_DOCUMENT("uploadDraftDocument", 0, false),
    UPLOAD_FURTHER_EVIDENCE("uploadFurtherEvidence", 0, false),
    UPLOAD_HEARING_RECORDING("uploadHearingRecording", 0, false),
    UPLOAD_WELSH_DOCUMENT("uploadWelshDocument", 0, false),
    UPPER_TRIBUNAL_DECISION("upperTribunalDecision", 0, false),
    VALID_APPEAL("validAppeal", 0, false),
    VALID_APPEAL_CREATED("validAppealCreated", 0, false),
    VALID_SEND_TO_INTERLOC("validSendToInterloc", 0, false),
    VOID_CASE("voidCase", 0, false),
    VOID_INCOMPLETE_APPLICATION("voidIncompleteApplication", 0, false),
    WITHDRAWN("withdrawn", "appealWithdrawn", 7, true),
    WRITE_FINAL_DECISION("writeFinalDecision", 0, false),
    GET_FIRST_TIER_DOCUMENTS("getFirstTierDocuments", 0, false),
    BUNDLE_CREATED_FOR_UPPER_TRIBUNAL("bundleCreatedForUpperTribunal", 0, false),
    NEW_CASE_ROLES_ASSIGNED("newCaseRolesAssigned",0,false);

    private final String type;
    private final String ccdType;
    private final int order;
    private final boolean notifiable;

    EventType(String ccdType, int order, boolean notifiable) {
        this.type = ccdType;
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
    static EventType findValue(String ccdType) {
        return Arrays.stream(EventType.values()).filter(pt -> pt.ccdType.equals(ccdType)).findFirst().get();
    }

    public String getType() {
        return type;
    }

    @JsonValue
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
}


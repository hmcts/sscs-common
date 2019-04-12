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
    DIRECTION_ISSUED("directionIssued", 0, false);

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


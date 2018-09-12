package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.google.common.base.CaseFormat;

public enum EventType {

    APPEAL_RECEIVED("appealReceived", "appealReceived", 1, true),
    DWP_RESPOND("dwpRespond", "responseReceived", 2, true),
    HEARING_BOOKED("hearingBooked", "hearingBooked", 3, true),
    HEARING("hearing", "hearing", 4, false),
    ADJOURNED("adjourned", "hearingAdjourned", 5, true),
    LAPSED_REVISED("lapsedRevised", "appealLapsed", 6, true),
    WITHDRAWN("withdrawn", "appealWithdrawn", 7, true),
    POSTPONED("postponed", "hearingPostponed",8, true),
    NEW_HEARING_BOOKED("newHearingBooked", "newHearingBooked", 9, true),
    PAST_HEARING_BOOKED("pastHearingBooked", "pastHearingBooked", 10, true),
    DORMANT("dormant", "appealDormant", 11, false),
    CLOSED("closed", "appealClosed", 12, false),
    DWP_RESPOND_OVERDUE("dwpRespondOverdue", "responseOverdue", 13, true),
    EVIDENCE_RECEIVED("evidenceReceived", "evidenceReceived", -1, true),
    EVIDENCE_REMINDER("evidenceRemainder", "evidenceReminder", -2, true),
    SYA_APPEAL_CREATED("appealCreated", 0, true),
    SUBSCRIPTION_CREATED("subscriptionCreated", 0, true),
    SUBSCRIPTION_UPDATED("subscriptionUpdated", 0, true),
    FIRST_HEARING_HOLDING_REMINDER("hearingHoldingReminder", 0, true),
    SECOND_HEARING_HOLDING_REMINDER("secondHearingHoldingReminder", 0, true),
    THIRD_HEARING_HOLDING_REMINDER("thirdHearingHoldingReminder", 0, true),
    FINAL_HEARING_HOLDING_REMINDER("finalHearingHoldingReminder", 0, true),
    HEARING_REMINDER("hearingReminder", 0, true);

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
}


package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_audioVideoProcessedAction", generate = true)
public enum ProcessedAction {
    @CCD(label = "Sent To Admin")
    SENT_TO_ADMIN("sentToAdmin", "Sent To Admin"),
    @CCD(label = "Sent To Judge")
    SENT_TO_JUDGE("sentToJudge", "Sent To Judge"),
    @CCD(label = "Direction Notice Issued")
    DIRECTION_ISSUED("directionIssued", "Direction Notice Issued");


    private final String value;
    private final String label;

    ProcessedAction(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static ProcessedAction fromValue(String text) {

        for (ProcessedAction party : ProcessedAction.values()) {
            if (text != null && party.getValue() != null && party.getValue().equalsIgnoreCase(text)) {
                return party;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}

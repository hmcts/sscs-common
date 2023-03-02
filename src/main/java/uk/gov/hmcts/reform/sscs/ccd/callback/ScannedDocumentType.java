package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum ScannedDocumentType {

    CHERISHED("cherished", "Cherished"),
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
    CORRECTION_APPLICATION("correctionApplication", "Correction application"),
    COVERSHEET("coversheet", "Coversheet"),
    FORM("form", "Form"),
    OTHER("other", "Other"),
    POSTPONEMENT_REQUEST("postponementRequest", "Postponement request"),
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    SET_ASIDE_APPLICATION("setAsideApplication", "Set aside application"),
    URGENT_HEARING_REQUEST("urgentHearingRequest", "Urgent hearing request");

    private String value;
    private String label;

    ScannedDocumentType(String value) {
        this.value = value;
    }

    ScannedDocumentType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static ScannedDocumentType fromValue(String text) {
        for (ScannedDocumentType scannedDocumentType : ScannedDocumentType.values()) {
            if (scannedDocumentType.getValue() != null && scannedDocumentType.getValue().equalsIgnoreCase(text)) {
                return scannedDocumentType;
            }
        }
        return null;
    }

}
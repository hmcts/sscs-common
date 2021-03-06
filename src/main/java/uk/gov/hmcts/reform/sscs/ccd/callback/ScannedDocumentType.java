package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum ScannedDocumentType {

    CHERISHED("cherished", "Cherished"),
    OTHER("other", "Other"),
    FORM("form", "Form"),
    COVERSHEET("coversheet", "Coversheet"),
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
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

}
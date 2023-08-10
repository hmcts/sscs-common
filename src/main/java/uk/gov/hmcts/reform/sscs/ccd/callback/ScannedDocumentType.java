package uk.gov.hmcts.reform.sscs.ccd.callback;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScannedDocumentType {

    CHERISHED("cherished", "Cherished"),
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
    CORRECTION_APPLICATION("correctionApplication", "Correction Application"),
    COVERSHEET("coversheet", "Coversheet"),
    FORM("form", "Form"),
    LIBERTY_TO_APPLY_APPLICATION("libertyToApplyApplication", "Liberty to Apply Application"),
    OTHER("other", "Other"),
    PERMISSION_TO_APPEAL_APPLICATION("permissionToAppealApplication", "Permission to Appeal Application"),
    POSTPONEMENT_REQUEST("postponementRequest", "Postponement request"),
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    SET_ASIDE_APPLICATION("setAsideApplication", "Set aside Application"),
    STATEMENT_OF_REASONS_APPLICATION("statementOfReasonsApplication", "Statement of Reasons Application"),
    URGENT_HEARING_REQUEST("urgentHearingRequest", "Urgent hearing request");

    private final String value;
    private final String label;

    @SuppressWarnings("unused")
    public static ScannedDocumentType fromValue(String text) {
        return Stream.of(ScannedDocumentType.values())
            .filter(type -> type.getValue() != null && type.getValue().equalsIgnoreCase(text))
            .findFirst()
            .orElse(null);
    }

}
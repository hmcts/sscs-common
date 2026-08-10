package uk.gov.hmcts.reform.sscs.ccd.callback;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Getter
@AllArgsConstructor
public enum ScannedDocumentType {

    @CCD(label = "Cherished")
    CHERISHED("cherished", "Cherished"),
    @CCD(label = "Confidentiality request")
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
    @CCD(label = "Correction application")
    CORRECTION_APPLICATION("correctionApplication", "Correction Application"),
    @CCD(label = "Coversheet")
    COVERSHEET("coversheet", "Coversheet"),
    @CCD(label = "Form")
    FORM("form", "Form"),
    @CCD(label = "Liberty to Apply application")
    LIBERTY_TO_APPLY_APPLICATION("libertyToApplyApplication", "Liberty to Apply Application"),
    @CCD(label = "Other")
    OTHER("other", "Other"),
    @CCD(label = "Permission to Appeal application")
    PERMISSION_TO_APPEAL_APPLICATION("permissionToAppealApplication", "Permission to Appeal Application"),
    @CCD(label = "Postponement request")
    POSTPONEMENT_REQUEST("postponementRequest", "Postponement request"),
    @CCD(label = "Post hearing other")
    POST_HEARING_OTHER("postHearingOther", "Post hearing other"),
    @CCD(label = "Reinstatement request")
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    @CCD(label = "Set aside application")
    SET_ASIDE_APPLICATION("setAsideApplication", "Set aside Application"),
    @CCD(label = "SOR application")
    STATEMENT_OF_REASONS_APPLICATION("statementOfReasonsApplication", "Statement of Reasons Application"),
    @CCD(label = "Urgent hearing request")
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
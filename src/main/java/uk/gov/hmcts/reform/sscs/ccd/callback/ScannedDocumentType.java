package uk.gov.hmcts.reform.sscs.ccd.callback;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@AllArgsConstructor
public enum ScannedDocumentType {

    @JsonProperty("cherished")
    @CCD(label = "Cherished")
    CHERISHED("cherished", "Cherished"),
    @JsonProperty("confidentialityRequest")
    @CCD(label = "Confidentiality request")
    CONFIDENTIALITY_REQUEST("confidentialityRequest", "Confidentiality request"),
    @JsonProperty("correctionApplication")
    @CCD(label = "Correction application")
    CORRECTION_APPLICATION("correctionApplication", "Correction Application"),
    @JsonProperty("coversheet")
    @CCD(label = "Coversheet")
    COVERSHEET("coversheet", "Coversheet"),
    @JsonProperty("form")
    @CCD(label = "Form")
    FORM("form", "Form"),
    @JsonProperty("libertyToApplyApplication")
    @CCD(label = "Liberty to Apply application")
    LIBERTY_TO_APPLY_APPLICATION("libertyToApplyApplication", "Liberty to Apply Application"),
    @JsonProperty("other")
    @CCD(label = "Other")
    OTHER("other", "Other"),
    @JsonProperty("permissionToAppealApplication")
    @CCD(label = "Permission to Appeal application")
    PERMISSION_TO_APPEAL_APPLICATION("permissionToAppealApplication", "Permission to Appeal Application"),
    @JsonProperty("postponementRequest")
    @CCD(label = "Postponement request")
    POSTPONEMENT_REQUEST("postponementRequest", "Postponement request"),
    @JsonProperty("postHearingOther")
    @CCD(label = "Post hearing other")
    POST_HEARING_OTHER("postHearingOther", "Post hearing other"),
    @JsonProperty("reinstatementRequest")
    @CCD(label = "Reinstatement request")
    REINSTATEMENT_REQUEST("reinstatementRequest", "Reinstatement request"),
    @JsonProperty("setAsideApplication")
    @CCD(label = "Set aside application")
    SET_ASIDE_APPLICATION("setAsideApplication", "Set aside Application"),
    @JsonProperty("statementOfReasonsApplication")
    @CCD(label = "SOR application")
    STATEMENT_OF_REASONS_APPLICATION("statementOfReasonsApplication", "Statement of Reasons Application"),
    @JsonProperty("urgentHearingRequest")
    @CCD(label = "Urgent hearing request")
    URGENT_HEARING_REQUEST("urgentHearingRequest", "Urgent hearing request"),
    @JsonProperty("otherPartyHearingPreferences")
    @CCD(label = "Other party hearing preferences")
    OTHER_PARTY_HEARING_PREFERENCES("otherPartyHearingPreferences", "Other party hearing preferences");

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
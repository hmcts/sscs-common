package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ListingStatus {
    DRAFT("Draft"),
    PROVISIONAL("Provisional"),
    FIXED("Fixed");

    private final String label;

    @JsonValue
    public String getLabel() {
        return label;
    }
}

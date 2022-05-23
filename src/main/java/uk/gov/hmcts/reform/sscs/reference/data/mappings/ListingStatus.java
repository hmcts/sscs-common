package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ListingStatus {
    DRAFT("Draft"),
    PROVISIONAL("Provisional"),
    FIXED("Fixed");

    @JsonValue
    private final String label;

    public static ListingStatus getListingStatusByLabel(String value) {
        return Arrays.stream(ListingStatus.values())
                .filter(sl -> sl.getLabel().equals(value))
                .findFirst()
                .orElse(null);
    }

}

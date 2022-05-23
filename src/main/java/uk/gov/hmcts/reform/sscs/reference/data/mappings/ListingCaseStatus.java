package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ListingCaseStatus {
    CASE_CREATED("Case Created"),
    AWAITING_LISTING("Awaiting Listing"),
    LISTED("Listed"),
    PENDING_RELISTING("Pending Relisting"),
    HEARING_COMPLETED("Hearing Completed"),
    CASE_CLOSED("Case Closed");

    @JsonValue
    private final String label;

    public static ListingCaseStatus getListingCaseStatusByLabel(String value) {
        return Arrays.stream(ListingCaseStatus.values())
                .filter(sl -> sl.getLabel().equals(value))
                .findFirst()
                .orElse(null);
    }

}


package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import com.fasterxml.jackson.annotation.JsonValue;
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

    private final String label;

    @JsonValue
    public String getLabel() {
        return label;
    }
}


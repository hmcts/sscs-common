package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostponementRequest {
    private YesNo unprocessedPostponementRequest;
    private String postponementRequestHearingDateAndTime;
    private String postponementRequestHearingVenue;
    private String postponementRequestDetails;
    private DocumentLink postponementPreviewDocument;
    private YesNo showPostponementDetailsPage;
    private String actionPostponementRequestSelected;
    private String listingOption;
}

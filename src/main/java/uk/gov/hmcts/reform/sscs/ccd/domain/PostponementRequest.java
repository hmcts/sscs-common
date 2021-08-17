package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostponementRequest {
    String postponementRequestHearingDateAndTime;
    String postponementRequestHearingVenue;
    String postponementRequestDetails;
    DocumentLink postponementRequestPreviewDocument;
}

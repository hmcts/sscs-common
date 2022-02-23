package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class HmcHearing implements Comparable<HmcHearing> {

    private HmcHearingDetails hmcHearingDetails;
    private HearingRequest hearingRequest;
    private HearingResponse hearingResponse;

    @Override
    public int compareTo(HmcHearing o) {
        return hearingRequest.getInitialRequestTimestamp().compareTo(o.getHearingRequest().getInitialRequestTimestamp());
    }
}

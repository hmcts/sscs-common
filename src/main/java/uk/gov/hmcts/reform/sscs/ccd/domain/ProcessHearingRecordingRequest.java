package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class ProcessHearingRecordingRequest implements Comparable<ProcessHearingRecordingRequest>{

    ProcessHearingRecordingRequestDetails value;

    @JsonCreator
    public ProcessHearingRecordingRequest(@JsonProperty("value") ProcessHearingRecordingRequestDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(ProcessHearingRecordingRequest o) {
        return new CompareToBuilder()
                .append(this.getValue().getHearingId(),
                        o.getValue().getHearingId())
                .toComparison();
    }

}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
public class HearingRecordingDetails implements Comparable<HearingRecordingDetails> {
    private DocumentLink value;

    @JsonCreator
    public HearingRecordingDetails(@JsonProperty("value") DocumentLink value) {
        this.value = value;
    }

    @Override
    public int compareTo(HearingRecordingDetails o) {
        return new CompareToBuilder()
                .append(this.value.getDocumentBinaryUrl(),
                        o.getValue().getDocumentBinaryUrl())
                .toComparison();
    }
}

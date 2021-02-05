package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnprocessedAudioVideoEvidence implements Comparable<UnprocessedAudioVideoEvidence> {

    private UnprocessedAudioVideoEvidenceDetails value;

    @JsonCreator
    public UnprocessedAudioVideoEvidence(@JsonProperty("value") UnprocessedAudioVideoEvidenceDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(UnprocessedAudioVideoEvidence o) {
        return new CompareToBuilder()
                .append(this.value.getDateAdded(), o.getValue().getDateAdded())
                .toComparison();
    }

}
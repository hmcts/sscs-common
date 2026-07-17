package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudioVideoEvidence implements Comparable<AudioVideoEvidence> {

    @CCD(ignore = true)
    @Delegate
    AudioVideoEvidenceDetails value;

    @JsonCreator
    public AudioVideoEvidence(@JsonProperty("value") AudioVideoEvidenceDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(AudioVideoEvidence o) {
        return new CompareToBuilder()
                .append(this.value.getDateAdded(), o.getValue().getDateAdded())
                .toComparison();
    }

}
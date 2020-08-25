package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsWelshDocument implements Comparable<SscsWelshDocument> {

    private SscsWelshDocumentDetails value;

    @JsonCreator
    public SscsWelshDocument(@JsonProperty("value") SscsWelshDocumentDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(SscsWelshDocument doc2) {
        SscsWelshDocumentDetails nextDocumentDetails = doc2.getValue();

        if (doc2.getValue().getDocumentDateAdded() == null) {
            return -1;
        }

        if (value.getDocumentDateAdded() == null) {
            return 0;
        }

        if (value.getDocumentDateAdded().equals(nextDocumentDetails.getDocumentDateAdded())) {
            return -1;
        }
        return -1 * value.getDocumentDateAdded().compareTo(nextDocumentDetails.getDocumentDateAdded());
    }
}
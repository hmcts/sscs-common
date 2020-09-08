package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractDocument<D extends AbstractDocumentDetails> implements Comparable<AbstractDocument> {

    private D value;

    @JsonCreator
    public AbstractDocument(@JsonProperty("value") D value) {
        this.value = value;
    }

    public D getValue() {
        return value;
    }

    public void setValue(D value) {
        this.value = value;
    }

    @Override
    public int compareTo(AbstractDocument doc2) {
        AbstractDocumentDetails nextDocumentDetails = doc2.getValue();

        if (doc2.getValue().getDocumentDateAdded() == null) {
            return -1;
        }

        if (value.getDocumentDateAdded() == null) {
            return 0;
        }

        if (value.getBundleAddition() != null && nextDocumentDetails.getBundleAddition() != null) {
            return 1 * value.getBundleAddition().compareTo(nextDocumentDetails.getBundleAddition());
        }

        if (value.getDocumentDateAdded().equals(nextDocumentDetails.getDocumentDateAdded())) {
            return -1;
        }
        return -1 * value.getDocumentDateAdded().compareTo(nextDocumentDetails.getDocumentDateAdded());
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.CompareToBuilder;

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
        return new CompareToBuilder()
                .append(this.value.getBundleAddition(), doc2.getValue().getBundleAddition())
                .append(doc2.getValue().getDateTimeFormatted(), this.value.getDateTimeFormatted())
                .toComparison();

    }
}

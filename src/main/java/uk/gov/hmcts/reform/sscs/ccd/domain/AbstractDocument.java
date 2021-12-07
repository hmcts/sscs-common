package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.util.Comparator;
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

        Comparator<String> nullSafeStringComparator = Comparator
                .nullsFirst(String::compareToIgnoreCase);
        Comparator<Integer> nullSafeIntegerComparator = Comparator
                .nullsFirst(Integer::compareTo);
        Comparator<LocalDateTime> nullSafeDateTimeComparator = Comparator
                .nullsFirst(LocalDateTime::compareTo).reversed();

        return Comparator
                .comparing(AbstractDocumentDetails::getFirstHalfOfBundleAddition, nullSafeStringComparator)
                .thenComparing(AbstractDocumentDetails::getSecondHalfOfBundleAddition, nullSafeIntegerComparator)
                .thenComparing(AbstractDocumentDetails::getDateTimeFormatted, nullSafeDateTimeComparator)
                .compare(this.getValue(), doc2.getValue());
    }
}

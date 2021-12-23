package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Comparator;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScannedDocument implements Comparable<ScannedDocument> {

    private ScannedDocumentDetails value;

    @JsonCreator
    public ScannedDocument(@JsonProperty("value") ScannedDocumentDetails value) {
        this.value = value;
    }

    @Override
    public int compareTo(ScannedDocument doc2) {

        Comparator<Long> nullSafeLongComparator = Comparator
                .nullsFirst(Long::compareTo);
        Comparator<LocalDateTime> nullSafeDateTimeComparator = Comparator
                .nullsFirst(LocalDateTime::compareTo).reversed();

        return Comparator
                .comparing(ScannedDocumentDetails::getScanDateTimeFormatted, nullSafeDateTimeComparator)
                .thenComparing(ScannedDocumentDetails::getLongControlNumber, nullSafeLongComparator)
                .compare(this.value, doc2.getValue());
    }
}

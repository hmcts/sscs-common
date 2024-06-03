package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ScannedDocument implements Comparable<ScannedDocument>, TypedDocument {

    private String id;

    private ScannedDocumentDetails value;

    @JsonCreator
    public ScannedDocument(@JsonProperty("id") String id, @JsonProperty("value") ScannedDocumentDetails value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public int compareTo(ScannedDocument doc2) {

        Comparator<Long> nullSafeLongComparator = Comparator
                .nullsLast(Long::compareTo);
        Comparator<LocalDateTime> nullSafeDateTimeComparator = Comparator
                .nullsLast(LocalDateTime::compareTo);

        return Comparator
                .comparing(ScannedDocumentDetails::getScanDateTimeFormatted, nullSafeDateTimeComparator)
                .thenComparing(ScannedDocumentDetails::getLongControlNumber, nullSafeLongComparator)
                .compare(this.value, doc2.getValue());
    }

    @Override
    @JsonIgnore
    public String getDocumentType() {
        return getValue() != null ? getValue().getType() : null;
    }
}

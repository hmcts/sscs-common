package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Comparator.nullsLast;
import static java.util.Comparator.reverseOrder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Comparator;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SscsDocument extends AbstractDocument<SscsDocumentDetails> {

    public static final Comparator<SscsDocument> BY_DOCUMENT_DATE_ADDED_DESCENDING = Comparator
        .comparing((final SscsDocument document) -> document.getValue().getDateTimeFormatted(), nullsLast(reverseOrder()));

    public SscsDocument(@JsonProperty("value") SscsDocumentDetails value) {
        super(value);
    }

    @JsonCreator
    public SscsDocument(@JsonProperty("id") String id, @JsonProperty("value") SscsDocumentDetails value) {
        super(id, value);
    }
}
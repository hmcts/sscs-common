package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.util.Comparator;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SscsWelshDocument extends AbstractDocument<SscsWelshDocumentDetails> {

    public static final Comparator<SscsWelshDocument> BY_DOCUMENT_DATE_ADDED_DESCENDING = byDocumentDateAddedDescending();

    public SscsWelshDocument(@JsonProperty("value") SscsWelshDocumentDetails value) {
        super(value);
    }
    @JsonCreator
    public SscsWelshDocument(@JsonProperty("id") String id, @JsonProperty("value") SscsWelshDocumentDetails value) {
        super(id, value);
    }

}
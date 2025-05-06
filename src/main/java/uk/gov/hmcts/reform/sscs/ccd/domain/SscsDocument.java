package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SscsDocument extends AbstractDocument<SscsDocumentDetails> implements TypedDocument {

    public SscsDocument(@JsonProperty("value") SscsDocumentDetails value) {
        super(value);
    }

    @JsonCreator
    public SscsDocument(@JsonProperty("id") String id, @JsonProperty("value") SscsDocumentDetails value) {
        super(id, value);
    }

    @Override
    @JsonIgnore
    public String getDocumentType() {
        return getValue() != null ? getValue().getDocumentType() : null;
    }
}
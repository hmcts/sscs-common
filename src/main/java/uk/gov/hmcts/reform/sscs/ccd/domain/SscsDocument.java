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
public class SscsDocument extends AbstractDocument<SscsDocumentDetails> {

    @JsonCreator
    public SscsDocument(@JsonProperty("value") SscsDocumentDetails value) {
        super(value);
    }
}
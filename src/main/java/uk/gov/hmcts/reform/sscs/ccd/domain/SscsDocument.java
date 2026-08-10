package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "sscsDocument", generate = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SscsDocument extends AbstractDocument<SscsDocumentDetails> {

    public SscsDocument(@JsonProperty("value") SscsDocumentDetails value) {
        super(value);
    }

    @JsonCreator
    public SscsDocument(@JsonProperty("id") String id, @JsonProperty("value") SscsDocumentDetails value) {
        super(id, value);
    }
}
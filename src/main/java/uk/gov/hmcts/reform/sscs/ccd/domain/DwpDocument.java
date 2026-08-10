package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "dwpDocuments", generate = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class DwpDocument extends AbstractDocument<DwpDocumentDetails> {

    @JsonCreator
    public DwpDocument(@JsonProperty("value") DwpDocumentDetails value) {
        super(value);
    }

}

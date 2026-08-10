package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "evidence", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Evidence {
    @CCD(label = "Evidence Documentation", typeOverride = FieldType.Collection, typeParameterOverride = "doc")
    private List<Document> documents;

    @JsonCreator
    public Evidence(@JsonProperty("documents") List<Document> documents, @JsonProperty("gpConsent") uk.gov.hmcts.ccd.sdk.type.YesOrNo gpConsent) {
        this.documents = documents;
        this.gpConsent = gpConsent;
    }

    /** Retained so existing positional call sites still compile. */
    public Evidence(List<Document> documents) {
        this(documents, null);
    }

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "GP Consent Given")
  private uk.gov.hmcts.ccd.sdk.type.YesOrNo gpConsent;
  // ==== end synthesised definition-only fields ====
}

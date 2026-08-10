package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "dwpTimeExtension", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class DwpTimeExtensionDetails {
    @CCD(label = "Requested", typeOverride = FieldType.YesOrNo)
    private String requested;
    @CCD(label = "Granted", typeOverride = FieldType.YesOrNo)
    private String granted;

    @JsonCreator
    public DwpTimeExtensionDetails(@JsonProperty("requested") String requested,
                                   @JsonProperty("granted") String granted,
                                   @JsonProperty("requestDate") java.time.LocalDate requestDate) {
        this.requested = requested;
        this.granted = granted;
        this.requestDate = requestDate;
    }

    /** Retained so existing positional call sites still compile. */
    public DwpTimeExtensionDetails(String requested, String granted) {
        this(requested, granted, null);
    }

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "Date of request")
  private java.time.LocalDate requestDate;
  // ==== end synthesised definition-only fields ====
}

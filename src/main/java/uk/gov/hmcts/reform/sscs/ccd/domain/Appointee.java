package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude
@EqualsAndHashCode(callSuper = true)
public class Appointee extends Entity {

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "PCQ ID")
  private String pcqId;
  // ==== end synthesised definition-only fields ====
}

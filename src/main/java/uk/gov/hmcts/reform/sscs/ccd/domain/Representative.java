package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude
@EqualsAndHashCode(callSuper = true)
public class Representative extends Entity {
    @CCD(label = "Has Representative", typeOverride = FieldType.YesOrNo)
    private String hasRepresentative;

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "PCQ ID")
  private String pcqId;
  // ==== end synthesised definition-only fields ====
}

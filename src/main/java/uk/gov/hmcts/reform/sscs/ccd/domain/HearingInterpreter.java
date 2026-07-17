package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class HearingInterpreter {
    @CCD(label = "Is an interpreter wanted?", typeOverride = FieldType.YesOrNo)
    private YesNo isInterpreterWanted;
    @CCD(label = "Interpreter Language", typeOverride = FieldType.DynamicList)
    private DynamicList interpreterLanguage;
}

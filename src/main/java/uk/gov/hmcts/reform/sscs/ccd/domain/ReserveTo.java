package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class ReserveTo {
    @CCD(label = "Reserved to Judge", typeOverride = FieldType.JudicialUser)
    private JudicialUserBase reservedJudge;
    @CCD(label = "Is this reserved to a District Tribunal Judge?", typeOverride = FieldType.YesOrNo)
    private YesNo reservedDistrictTribunalJudge;
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsIndustrialInjuriesData {
    @CCD(
            label = "Panel doctor specialism",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_panelDoctorSpecialism",
            access = {SscsCrudAccess.class}
    )
    private String panelDoctorSpecialism;
    @CCD(
            label = "Second Panel doctor specialism",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_panelDoctorSpecialism",
            access = {SscsCrudAccess.class}
    )
    private String secondPanelDoctorSpecialism;
}

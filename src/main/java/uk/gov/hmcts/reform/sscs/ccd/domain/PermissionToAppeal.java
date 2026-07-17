package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PermissionToAppeal {
    @CCD(
            label = "Action Permission to Appeal Application",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_actionPermissionToAppeal"
    )
    private PermissionToAppealActions action;
    @CCD(label = "Upload or enter details of your post hearing request")
    private RequestFormat requestFormat;
}

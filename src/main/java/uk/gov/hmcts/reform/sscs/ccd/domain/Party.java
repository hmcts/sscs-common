package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
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
public abstract class Party extends Entity {
    @CCD(label = "Appointee", typeOverride = FieldType.YesOrNo)
    private String isAppointee;
    @CCD(label = "Appointee details")
    private Appointee appointee;

    @CCD(label = "Appellant Role")
    private Role role;

    @CCD(label = "Role", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_ibcRoles")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ibcRole;

    @CCD(label = "Confidentiality Status", typeOverride = FieldType.YesOrNo)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo confidentialityRequired;

    @CCD(ignore = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNoUndetermined confidentialityRequirement;

    @CCD(label = "Confidentiality Confirmed")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("confidentialityRequiredConfirmedDate")
    private LocalDateTime confidentialityRequiredChangedDate;

}

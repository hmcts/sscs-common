package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.sscs.utility.TrimStringFields;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRoleMapping extends CaseWorkerDomain {

    @MappingField(columnName = "Service ID", position = 1)
    @NotEmpty
    @JsonProperty(value = "service_code")
    @JsonDeserialize(using = TrimStringFields.class)
    String serviceId;

    @MappingField(columnName = "Role")
    @NotNull
    @JsonProperty(value = "role")
    Integer roleId;

    @MappingField(columnName = "IDAM Roles")
    @NotEmpty
    @JsonProperty(value = "idam_roles")
    @JsonDeserialize(using = TrimStringFields.class)
    String idamRoles;
}

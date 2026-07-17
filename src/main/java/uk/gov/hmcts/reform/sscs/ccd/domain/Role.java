package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role {

    @CCD(label = "Role", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_roles")
    private String name;
    @CCD(label = "Description", showCondition = "name=\"Other\"")
    private String description;

    @JsonCreator
    public Role(@JsonProperty("name") String name,
                @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
    }
}

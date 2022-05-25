package uk.gov.hmcts.reform.sscs.model.dwp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OfficeMapping {
    private String code;
    @JsonProperty("isDefault")
    private boolean isDefault;
    private Mapping mapping;
    private OfficeAddress address;

}

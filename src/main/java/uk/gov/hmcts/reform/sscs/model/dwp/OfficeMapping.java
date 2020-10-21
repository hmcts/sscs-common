package uk.gov.hmcts.reform.sscs.model.dwp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class OfficeMapping {
    private String code;
    private boolean isDefault;
    private Mapping mapping;
    private OfficeAddress address;

}

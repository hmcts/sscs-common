package uk.gov.hmcts.reform.sscs.model.dwp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Mapping {
    private String ccd;
    private String gaps;
    private String dwpRegionCentre;
}

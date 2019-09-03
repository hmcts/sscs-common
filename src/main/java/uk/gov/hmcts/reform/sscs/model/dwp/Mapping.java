package uk.gov.hmcts.reform.sscs.model.dwp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Mapping {
    private String ccd;
    private String gaps;
    private String dwpRegionCentre;
}
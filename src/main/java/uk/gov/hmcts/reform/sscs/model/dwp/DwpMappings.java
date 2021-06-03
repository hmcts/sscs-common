package uk.gov.hmcts.reform.sscs.model.dwp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class DwpMappings {

    private OfficeMapping[] pip;
    private OfficeMapping[] esa;
    private OfficeMapping uc;
    private OfficeMapping[] dla;
    private OfficeMapping carersAllowance;
    private OfficeMapping[] attendanceAllowance;
    private OfficeMapping[] jsa;
    private OfficeMapping bereavementBenefit;
    private OfficeMapping[] iidb;
    private OfficeMapping testHmctsAddress;
}


package uk.gov.hmcts.reform.sscs.model.dwp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class OfficeAddress {
    private String line1;
    private String line2;
    private String line3;
    private String postCode;
}
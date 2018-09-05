package uk.gov.hmcts.reform.sscs.ccd.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode
public class CcdRequestDetails {
    private String jurisdictionId;
    private String caseTypeId;
}

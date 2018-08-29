package uk.gov.hmcts.reform.sscscorbackend.service.ccd;

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

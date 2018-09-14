package uk.gov.hmcts.reform.sscs.ccd.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@EqualsAndHashCode
@NonFinal
public class CcdRequestDetails {
    private String jurisdictionId;
    private String caseTypeId;
}

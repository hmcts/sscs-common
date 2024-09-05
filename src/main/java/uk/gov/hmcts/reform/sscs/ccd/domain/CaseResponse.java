package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata.ValidationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CaseResponse {
    private List<String> warnings;
    private Map<String, Object> transformedCase;
    private List<String> errors;
    private ValidationStatus status;

}

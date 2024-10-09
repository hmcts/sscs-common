package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.sscs.ccd.validation.appeal.ValidationStatus;

@Data
@Builder
public class CaseResponse {
    private List<String> warnings;
    private Map<String, Object> transformedCase;
    private List<String> errors;
    private ValidationStatus status;

}

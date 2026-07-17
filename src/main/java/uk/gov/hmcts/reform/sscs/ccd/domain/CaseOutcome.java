package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseOutcome {
    @CCD(
            label = "Case Outcome",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_caseOutcome",
            access = {SscsSuperuserCrudAccess.class, ClerkCrudAccess.class}
    )
    private String caseOutcome;
    @CCD(
            label = "Did PO Attend?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsSuperuserCrudAccess.class, ClerkCrudAccess.class}
    )
    private YesNo didPoAttend;
}

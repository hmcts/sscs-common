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
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Postponement {
    @CCD(
            label = "Unprocessed Postponement",
            typeOverride = FieldType.YesOrNo,
            access = {SscsSuperuserCrudAccess.class, SystemupdateCrudAccess.class}
    )
    private YesNo unprocessedPostponement;
    @CCD(
            label = "Postponement Event Type",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "eventType",
            access = {SscsSuperuserCrudAccess.class, SystemupdateCrudAccess.class}
    )
    private EventType postponementEvent;
}

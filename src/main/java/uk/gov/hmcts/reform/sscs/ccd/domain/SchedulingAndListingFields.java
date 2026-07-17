package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCudSuperuserCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CitizenRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCudSystemupdateCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulingAndListingFields {
    @CCD(
            label = "Hearing Route",
            access = {DefaultAccess.class, SscsCitizenCrudAccess.class, JudgeRegistrarCrudAccess.class, DwpresponsewriterCrudAccess.class}
    )
    private HearingRoute hearingRoute;
    @CCD(
            label = "Hearing State",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_hearingState",
            access = {SscsCitizenCrudAccess.class, ClerkCudSuperuserCudSystemupdateCrudAccess.class, JudgeRegistrarCudAccess.class, DwpresponsewriterCrudAccess.class}
    )
    private HearingState hearingState;
    @CCD(
            label = "Panel member exclusions/reserve to",
            access = {SscsSuperuserCrudAccess.class, JudgeCudAccess.class, SystemupdateCrudAccess.class}
    )
    @JsonProperty("panelMemberExclusions")
    private PanelMemberExclusions panelMemberExclusions;
    @CCD(label = "Reserve to", access = {DefaultAccess.class, JudgeRegistrarRAccess.class})
    private ReserveTo reserveTo;
    @CCD(label = "Overrides Listing Values", access = {DefaultAccess.class, CitizenRAccess.class})
    private OverrideFields overrideFields;
    @CCD(label = "Default Listing Values", access = {SuperuserCudSystemupdateCrudAccess.class})
    private OverrideFields defaultListingValues;
    @CCD(
            label = "Amend Reason",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_amendReason",
            access = {DefaultAccess.class}
    )
    private List<AmendReason> amendReasons;
}

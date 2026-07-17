package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarTeamleaderCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRDwpresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedSscsCaseData {
    @CCD(
            label = "Do you want to select next hearing type i.e. Substantive or Directions hearing",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo selectNextHmcHearingType;

    @CCD(
            label = "Are you aware of any additional Other Parties?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsRDwpresponsewriterCrudAccess.class, SuperuserCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo awareOfAnyAdditionalOtherParties;

    @CCD(
            label = "Selected Party",
            typeOverride = FieldType.DynamicList,
            access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DynamicList selectedConfidentialityParty;

    @CCD(label = "Show confidentiality tab", typeOverride = FieldType.YesOrNo, access = {SscsCudAccess.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo showConfidentialityTab;

    @CCD(
            label = "Is this a severe conditions criteria only appeal?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo writeFinalDecisionSevereYesNo;

    @CCD(
            label = "Do the severe conditions criteria apply?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo writeFinalDecisionSevereCriteriaApply;

    @CCD(
            label = "Do the severe conditions criteria apply?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo esaWriteFinalDecisionSevereCriteriaApply;

}

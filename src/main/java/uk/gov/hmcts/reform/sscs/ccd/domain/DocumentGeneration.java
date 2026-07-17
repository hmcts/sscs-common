package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkRSuperuserCrudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarTeamleaderCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRDwpresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentGeneration {
    @CCD(
            label = "Generate notice",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class}
    )
    @JsonProperty("generateNotice")
    private YesNo generateNotice;
    @CCD(label = "Generate notice?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    @JsonProperty("correctionGenerateNotice")
    private YesNo correctionGenerateNotice;
    @CCD(label = "Generate notice?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    @JsonProperty("statementOfReasonsGenerateNotice")
    private YesNo statementOfReasonsGenerateNotice;
    @CCD(label = "Generate notice?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    @JsonProperty("libertyToApplyGenerateNotice")
    private YesNo libertyToApplyGenerateNotice;
    @CCD(label = "Generate notice?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    @JsonProperty("permissionToAppealGenerateNotice")
    private YesNo permissionToAppealGenerateNotice;
    @CCD(
            label = "Body content",
            typeOverride = FieldType.TextArea,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class}
    )
    @JsonProperty("bodyContent")
    private String bodyContent;
    @CCD(label = "Body content", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    @JsonProperty("correctionBodyContent")
    private String correctionBodyContent;
    @CCD(label = "Body content", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    @JsonProperty("statementOfReasonsBodyContent")
    private String statementOfReasonsBodyContent;
    @CCD(label = "Body content", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    @JsonProperty("libertyToApplyBodyContent")
    private String libertyToApplyBodyContent;
    @CCD(label = "Body content", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    @JsonProperty("permissionToAppealBodyContent")
    private String permissionToAppealBodyContent;
    @CCD(
            label = "Directions notice content",
            typeOverride = FieldType.TextArea,
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRAccess.class}
    )
    @JsonProperty("directionNoticeContent")
    private String directionNoticeContent;
    @CCD(
            label = "Signed by",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class}
    )
    @JsonProperty("signedBy")
    private String signedBy;
    @CCD(
            label = "Signed role",
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class}
    )
    @JsonProperty("signedRole")
    private String signedRole;
    @CCD(label = "Signed by", access = {SscsCrudAccess.class})
    @JsonProperty("correctionSignedBy")
    private String correctionSignedBy;
    @CCD(label = "Signed role", access = {SscsCrudAccess.class})
    @JsonProperty("correctionSignedRole")
    private String correctionSignedRole;
    @CCD(label = "Signed by", access = {SscsCrudAccess.class})
    @JsonProperty("statementOfReasonsSignedBy")
    private String statementOfReasonsSignedBy;
    @CCD(label = "Signed role", access = {SscsCrudAccess.class})
    @JsonProperty("statementOfReasonsSignedRole")
    private String statementOfReasonsSignedRole;
    @CCD(label = "Signed by", access = {SscsCrudAccess.class})
    @JsonProperty("libertyToApplySignedBy")
    private String libertyToApplySignedBy;
    @CCD(label = "Signed role", access = {SscsCrudAccess.class})
    @JsonProperty("libertyToApplySignedRole")
    private String libertyToApplySignedRole;
    @CCD(label = "Signed by", access = {SscsCrudAccess.class})
    @JsonProperty("permissionToAppealSignedBy")
    private String permissionToAppealSignedBy;
    @CCD(label = "Signed role", access = {SscsCrudAccess.class})
    @JsonProperty("permissionToAppealSignedRole")
    private String permissionToAppealSignedRole;
}

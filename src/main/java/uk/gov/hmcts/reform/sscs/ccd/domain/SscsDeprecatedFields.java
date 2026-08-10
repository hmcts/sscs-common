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
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterHmrcresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CallagentCruRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CallagentCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.RegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkRSuperuserCrudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.TeamleaderRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsDeprecatedFields {

    @CCD(
            label = "Appellant NINO",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, CallagentCruRegistrarCrudAccess.class}
    )
    private String generatedNino;
    @CCD(
            label = "Appellant Surname",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, CallagentCruRegistrarCrudAccess.class}
    )
    private String generatedSurname;
    @CCD(
            label = "Appellant Email Address",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, CallagentCruAccess.class}
    )
    private String generatedEmail;
    @CCD(
            label = "Appellant Mobile Number",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, CallagentCruAccess.class}
    )
    private String generatedMobile;
    @CCD(
            label = "Appellant Date of Birth",
            typeOverride = FieldType.Date,
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class, RegistrarCrudAccess.class}
    )
    @JsonProperty("generatedDOB")
    private String generatedDob;
    @CCD(
            label = "Historic interloc directions",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsInterlocDirectionDocument",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarCrudAccess.class, TeamleaderRAccess.class}
    )
    private List<SscsInterlocDirectionDocuments> historicSscsInterlocDirectionDocs;
    @CCD(
            label = "Cor Case documents",
            regex = "No",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "corDocument",
            access = {SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class, SuperuserCrudAccess.class}
    )
    private List<CorDocument> corDocument;
    @CCD(
            label = "Hidden document collection",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "corDocument",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class}
    )
    private List<CorDocument> draftCorDocument;
    @CCD(
            label = "Assign to Judge",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    private String assignedToJudge;
    @CCD(
            label = "Assign to disability member",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    private String assignedToDisabilityMember;
    @CCD(
            label = "Assign to medical member",
            access = {DefaultAccess.class, SscsCrudJudgeCrudPanelmemberRCitizenCrudAccess.class}
    )
    private String assignedToMedicalMember;
}

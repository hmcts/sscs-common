package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkSystemupdateCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRClerkCruSuperuserCruSystemupdateCruAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkAllocationFields {

    @CCD(label = "Added documents", access = {SscsCitizenCrudAccess.class})
    @JsonInclude
    private String addedDocuments;

    @CCD(
            label = "Scanned Document Types",
            typeOverride = FieldType.DynamicList,
            access = {SscsSuperuserCrudAccess.class, ClerkSystemupdateCruAccess.class}
    )
    private List<String> scannedDocumentTypes;

    @CCD(
            label = "Uploaded Welsh Document Types",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "documentTypeWelsh",
            access = {SscsCrudAccess.class}
    )
    private List<String> uploadedWelshDocumentTypes;

    @CCD(
            label = "Assigned Case Roles",
            typeOverride = FieldType.DynamicList,
            access = {SscsRClerkCruSuperuserCruSystemupdateCruAccess.class}
    )
    private List<String> assignedCaseRoles;

    @CCD(
            label = "Assigned Case Roles",
            typeOverride = FieldType.DynamicList,
            access = {SscsRClerkCruSuperuserCruSystemupdateCruAccess.class}
    )
    private List<String> previouslyAssignedCaseRoles;

    @CCD(label = "Days to hearing", access = {JudgeRegistrarCrudAccess.class, SuperuserCrudAccess.class})
    private Integer daysToHearing;

    @CCD(
            label = "FTA response review required",
            typeOverride = FieldType.YesOrNo,
            access = {SscsSuperuserCrudAccess.class, ClerkSystemupdateCruAccess.class}
    )
    private YesNo ftaResponseReviewRequired;
}

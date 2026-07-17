package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.RegistrarCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternalCaseDocumentData {
    @CCD(
            label = "Tribunal Internal documents",
            regex = "No",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsDocument",
            access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class, SscsCrudAccess.class}
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SscsDocument> sscsInternalDocument;
    @CCD(label = " ", access = {DefaultAccess.class, RegistrarCrudAccess.class})
    @Getter
    private DocumentTabChoice moveDocumentTo;
    @CCD(
            label = " ",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_uploadRemoveDocumentType",
            access = {DefaultAccess.class, RegistrarCrudAccess.class}
    )
    private String uploadRemoveDocumentType;
    @CCD(
            label = " ",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_uploadRemoveOrMoveDocument",
            access = {DefaultAccess.class, RegistrarCrudAccess.class}
    )
    private String uploadRemoveOrMoveDocument;
    @CCD(
            label = " ",
            typeOverride = FieldType.DynamicMultiSelectList,
            access = {DefaultAccess.class, RegistrarCrudAccess.class}
    )
    private DynamicMixedChoiceList moveDocumentToInternalDocumentsTabDL;
    @CCD(
            label = " ",
            typeOverride = FieldType.DynamicMultiSelectList,
            access = {DefaultAccess.class, RegistrarCrudAccess.class}
    )
    private DynamicMixedChoiceList moveDocumentToDocumentsTabDL;
    @CCD(
            label = "Should this document be issued out to parties?",
            typeOverride = FieldType.YesOrNo,
            access = {DefaultAccess.class, JudgeRegistrarCrudAccess.class}
    )
    private YesNo shouldBeIssued;
}

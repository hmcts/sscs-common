package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarTeamleaderCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsRDwpresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkRSuperuserCrudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.TeamleaderRAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentStaging {
    @CCD(
            label = "Preview Document",
            typeOverride = FieldType.Document,
            access = {DefaultAccess.class, JudgeRegistrarTeamleaderCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class}
    )
    @JsonProperty("previewDocument")
    private DocumentLink previewDocument;
    @CCD(
            label = "Preview Document",
            hint = "All documents must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("postHearingPreviewDocument")
    private DocumentLink postHearingPreviewDocument;

    @CCD(
            label = "Date added",
            access = {ClerkRSuperuserCrudSystemupdateCrudAccess.class, JudgeRegistrarCrudAccess.class, SscsRDwpresponsewriterCrudAccess.class, TeamleaderRAccess.class}
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("dateAdded")
    private LocalDate dateAdded;
}

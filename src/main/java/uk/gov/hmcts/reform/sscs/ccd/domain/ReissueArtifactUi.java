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
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReissueArtifactUi {
    @CCD(label = "Select the document to resend", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList reissueFurtherEvidenceDocument;
    @CCD(
            label = "Resend to the appellant or appointee",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo resendToAppellant;
    @CCD(label = "Resend to the representative", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo resendToRepresentative;
    @CCD(label = "Resend to the FTA", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo resendToDwp;
    @CCD(
            label = "Show reissue to other party ui section",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo showReissueToOtherPartyUiSection;
    @CCD(
            label = "Other party",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "OtherPartyOption",
            access = {SscsCrudAccess.class}
    )
    private List<OtherPartyOption> otherPartyOptions;
}

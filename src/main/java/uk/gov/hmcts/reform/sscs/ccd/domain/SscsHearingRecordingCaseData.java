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
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DefaultAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterHmrcresponsewriterCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCudSuperuserCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SystemupdateCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsHearingRecordingCaseData {
    @CCD(label = "Select hearing", typeOverride = FieldType.DynamicList, access = {ClerkSuperuserCrudAccess.class})
    private DynamicList selectHearingDetails;
    @CCD(
            label = "Request for hearing recording",
            typeOverride = FieldType.DynamicList,
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class}
    )
    private DynamicList requestableHearingDetails;
    @CCD(
            label = "Requested hearing recordings",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingRecordingRequest",
            access = {DwpresponsewriterHmrcresponsewriterCrudAccess.class, ClerkCudSuperuserCudSystemupdateCrudAccess.class}
    )
    private List<HearingRecordingRequest> requestedHearings;
    @CCD(
            label = "Released hearing recordings",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingRecordingRequest",
            access = {DefaultAccess.class, CitizenCrudAccess.class}
    )
    private List<HearingRecordingRequest> citizenReleasedHearings;
    @CCD(
            label = "Released hearing recordings",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingRecordingRequest",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class}
    )
    private List<HearingRecordingRequest> dwpReleasedHearings;
    @CCD(
            label = "Refused hearing recordings",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingRecordingRequest",
            access = {DefaultAccess.class, DwpresponsewriterHmrcresponsewriterCrudAccess.class}
    )
    private List<HearingRecordingRequest> refusedHearings;
    @CCD(label = "Outstanding hearing recording requests", access = {SscsCrudAccess.class})
    private String requestedHearingsTextList;
    @CCD(label = "Released hearing recordings", access = {SscsCrudAccess.class})
    private String releasedHearingsTextList;
    @CCD(
            label = "Hearing recording request outstanding?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo hearingRecordingRequestOutstanding;
    @CCD(label = "Requesting party", typeOverride = FieldType.DynamicList, access = {SscsCrudAccess.class})
    private DynamicList requestingParty;
    @CCD(label = "Show the requesting party page?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo showRequestingPartyPage;
    @CCD(label = " ", access = {SscsCrudAccess.class})
    private ProcessHearingRecordingRequest processHearingRecordingRequest;
    @CCD(
            label = "Hearing recording",
            hint = "You can only upload MP3 and MP4 files up to 500MB",
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("hearingRecording")
    private HearingRecording hearingRecording;
    @CCD(
            label = "Hearing recordings",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "sscsHearingRecordingDetails",
            access = {SuperuserSystemupdateCrudAccess.class, ClerkCudAccess.class, JudgeRAccess.class}
    )
    @JsonProperty("sscsHearingRecordings")
    private List<SscsHearingRecording> sscsHearingRecordings;
    @CCD(label = "Hearing recordings", access = {SystemupdateCrudAccess.class, SscsCrudAccess.class})
    @JsonProperty("existingHearingRecordings")
    private SscsHearingRecordingDetails existingHearingRecordings;
    @CCD(
            label = "Hearing recording exists?",
            typeOverride = FieldType.YesOrNo,
            access = {SystemupdateCrudAccess.class, SscsCrudAccess.class}
    )
    private YesNo hearingRecordingExist;
    @CCD(
            label = "Other party hearing recording requests",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "otherPartyHearingRecordingReqUi",
            access = {SscsCrudAccess.class}
    )
    private List<OtherPartyHearingRecordingReqUi> otherPartyHearingRecordingReqUi;

}

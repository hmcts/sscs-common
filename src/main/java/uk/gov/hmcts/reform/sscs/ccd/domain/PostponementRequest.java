package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkSuperuserCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeRegistrarCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostponementRequest {
    @CCD(label = "Unprocessed postponement request", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo unprocessedPostponementRequest;
    @CCD(label = "Date & Time", access = {SscsCrudAccess.class})
    private String postponementRequestHearingDateAndTime;
    @CCD(label = "Venue", access = {SscsCrudAccess.class})
    private String postponementRequestHearingVenue;
    @CCD(label = "Postponement details", typeOverride = FieldType.TextArea, access = {SscsCrudAccess.class})
    private String postponementRequestDetails;
    @CCD(
            label = "Postponement preview",
            hint = "All documents must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document,
            access = {SscsCrudAccess.class}
    )
    private DocumentLink postponementPreviewDocument;
    @CCD(
            label = "Show the Postponement Details page?",
            typeOverride = FieldType.YesOrNo,
            access = {ClerkSuperuserCrudAccess.class}
    )
    private YesNo showPostponementDetailsPage;
    @CCD(
            label = "Action postponement request",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_actionPostponementRequest",
            access = {JudgeRegistrarCrudAccess.class, SuperuserCrudAccess.class}
    )
    private String actionPostponementRequestSelected;
    @CCD(
            label = "Listing option",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_actionPostponementRequestListing",
            access = {JudgeRegistrarCrudAccess.class, ClerkSuperuserCrudAccess.class}
    )
    private String listingOption;
}

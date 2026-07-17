package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingOutcomeDetails {
    @CCD(label = "Hearing id")
    private String completedHearingId;
    @CCD(label = "Hearing Start Date and Time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime hearingStartDateTime;
    @CCD(label = "Hearing End Date and Time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime hearingEndDateTime;
    @CCD(label = "Hearing Outcome", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_caseOutcome")
    private String hearingOutcomeId;
    @CCD(label = "Did PO Attend?", typeOverride = FieldType.YesOrNo)
    private YesNo didPoAttendHearing;
    @CCD(label = "Hearing Channel", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_hearingChannel")
    private HearingChannel hearingChannelId;
    @CCD(label = "Venue")
    private Venue venue;
    @CCD(label = "Epims Id")
    private String epimsId;
    @CCD(label = "Hearings", typeOverride = FieldType.DynamicList)
    private DynamicList completedHearings;
}

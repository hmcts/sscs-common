package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingDetails {
    @CCD(label = "Hearing id")
    private String hearingId;
    @CCD(label = "Date", showCondition = "start!=\"*\"", typeOverride = FieldType.Date)
    private String hearingDate;
    @CCD(label = "Time", hint = "(24hr e.g. 13:25)", showCondition = "start!=\"*\"")
    private String time;
    @CCD(label = "Adjourned", typeOverride = FieldType.YesOrNo)
    private String adjourned;
    @CCD(label = "Date hearing set", typeOverride = FieldType.Date)
    private String eventDate;
    @CCD(label = "Venue")
    private Venue venue;
    @CCD(label = "Venue id")
    private String venueId;
    @CCD(label = "Panel")
    @JsonProperty("panel")
    private JudicialUserPanel panel;

    @CCD(label = "Hearing Requested")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime hearingRequested;
    @CCD(label = "Version Number")
    private Long versionNumber;
    @CCD(label = "Hearing Status", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_hearingStatus")
    private HearingStatus hearingStatus;
    @CCD(label = "Hearing Start Date and Time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime start;
    @CCD(label = "Hearing End Date and Time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime end;
    @CCD(label = "Epims Id")
    private String epimsId;
    @CCD(label = "Hearing Channel", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_hearingChannel")
    private HearingChannel hearingChannel;

    @JsonIgnore
    public LocalDateTime getHearingDateTime() {
        if (isNotBlank(hearingDate) && isNotBlank(time)) {
            return LocalDateTime.of(LocalDate.parse(hearingDate), LocalTime.parse(time));
        }
        return null;
    }

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "Postponement date")
  private java.time.LocalDate postponedDate;
  @CCD(label = "Date hearing adjourned")
  private java.time.LocalDate adjournedDate;
  // ==== end synthesised definition-only fields ====
}

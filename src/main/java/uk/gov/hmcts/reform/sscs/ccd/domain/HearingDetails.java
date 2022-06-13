package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class HearingDetails {
    private String hearingId;
    private String hearingDate;
    private String time;
    private String adjourned;
    private String eventDate;
    private Venue venue;
    private String venueId;

    private LocalDateTime hearingRequested;
    private Long hmcHearingId;
    private Long hmcVersionNumber;
    private LocalDateTime start;
    private LocalDateTime end;
    private String epimsId;

    @JsonIgnore
    public LocalDateTime getHearingDateTime() {
        if (nonNull(start)) {
            return start;
        }
        return LocalDateTime.of(LocalDate.parse(hearingDate), LocalTime.parse(time));
    }
}

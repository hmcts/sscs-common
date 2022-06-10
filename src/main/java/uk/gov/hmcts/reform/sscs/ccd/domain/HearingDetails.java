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
    private Venue venue;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String hearingDate;
    private String time;
    private String adjourned;
    private String eventDate;
    private String hearingId;
    private String venueId;

    @JsonIgnore
    public LocalDateTime getHearingDateTime() {
        if (nonNull(startDateTime)) {
            return startDateTime;
        }
        return LocalDateTime.of(LocalDate.parse(hearingDate), LocalTime.parse(time));
    }
}

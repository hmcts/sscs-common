package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class HearingDetails {
    private Venue venue;
    private String hearingDate;
    private String time;
    private String adjourned;
    private String eventDate;
    private String hearingId;

    @JsonCreator
    public HearingDetails(@JsonProperty("venue") Venue venue,
                          @JsonProperty("hearingDate") String hearingDate,
                          @JsonProperty("time") String time,
                          @JsonProperty("adjourned") String adjourned,
                          @JsonProperty("eventDate") String eventDate,
                          @JsonProperty("hearingId") String hearingId) {
        this.venue = venue;
        this.hearingDate = hearingDate;
        this.time = time;
        this.adjourned = adjourned;
        this.eventDate = eventDate;
        this.hearingId = hearingId;
    }

    @JsonIgnore
    public LocalDateTime getHearingDateTime() {
        return LocalDateTime.of(LocalDate.parse(hearingDate), LocalTime.parse(time));
    }
}

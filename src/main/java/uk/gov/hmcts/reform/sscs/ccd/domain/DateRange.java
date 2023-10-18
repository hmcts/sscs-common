package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class DateRange {
    String start;
    String end;

    @JsonCreator
    public DateRange(@JsonProperty("start") String start,
                     @JsonProperty("end") String end) {
        this.start = start;
        this.end = end;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public LocalDate getStartDate() {
        return getLocalDate(start, end);
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public LocalDate getEndDate() {
        return getLocalDate(end, start);
    }

    @JsonIgnore
    private LocalDate getLocalDate(String date1, String date2) {
        if (isNull(date1)) {
            if (isNull(date2)) {
                return null;
            }

            LocalDate parsedDate = parseLocalDate(date2);

            if (nonNull(parsedDate)) {
                return LocalDate.parse(date2);
            }
        }

        return parseLocalDate(date1);
    }

    @JsonIgnore
    private LocalDate parseLocalDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}

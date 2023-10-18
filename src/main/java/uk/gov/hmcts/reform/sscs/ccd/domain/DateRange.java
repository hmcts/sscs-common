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
        LocalDate parsedDate1 = parseLocalDate(date1);

        if (isNull(parsedDate1)) {
            LocalDate parsedDate2 = parseLocalDate(date2);

            if (isNull(parsedDate2)) {
                return null;
            }
        }

        return parsedDate1;
    }

    @JsonIgnore
    private LocalDate parseLocalDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }
}

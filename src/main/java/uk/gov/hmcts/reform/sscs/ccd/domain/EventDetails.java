package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.config.SscsConstants.ZONE_ID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "event", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class EventDetails {
    @CCD(label = "Date")
    String date;
    @CCD(
            label = "Event Type",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "eventType",
            typeParameterClass = EventType2.class
    )
    String type;
    @CCD(label = "Description")
    String description;

    @JsonCreator
    public EventDetails(@JsonProperty("date") String date,
                        @JsonProperty("type") String type,
                        @JsonProperty("description") String description) {
        this.date = date;
        this.type = type;
        this.description = description;
    }

    @JsonIgnore
    public EventType getEventType() {
        return EventType.getEventTypeByCcdType(type);
    }

    @JsonIgnore
    public ZonedDateTime getDateTime() {
        return ZonedDateTime.parse(date + "Z").toInstant().atZone(ZoneId.of(ZONE_ID));
    }
}

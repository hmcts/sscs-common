package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReasAdjCorrespondenceDetails {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm");
    private final String sentOn;
    private final String to;
    private final DocumentLink documentLink;
    private final CorrespondenceType correspondenceType;
    private final String eventType;
    private final String reasonableAdjustmentStatus;

    @JsonCreator
    public ReasAdjCorrespondenceDetails(@JsonProperty("sentOn") String sentOn,
                                        @JsonProperty("to") String to,
                                        @JsonProperty("documentLink") DocumentLink documentLink,
                                        @JsonProperty("correspondenceType") CorrespondenceType correspondenceType,
                                        @JsonProperty("eventType") String eventType,
                                        @JsonProperty("reasonableAdjustmentStatus") String reasonableAdjustmentStatus) {
        this.sentOn = sentOn;
        this.to = to;
        this.documentLink = documentLink;
        this.correspondenceType = correspondenceType;
        this.eventType = eventType;
        this.reasonableAdjustmentStatus = reasonableAdjustmentStatus;
    }

    @JsonIgnore
    public LocalDateTime getSentOnDateTime() {
        return LocalDateTime.parse(sentOn, DATE_TIME_FORMATTER);
    }
}

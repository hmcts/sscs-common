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
public class CorrespondenceDetails {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm");
    private final String sentOn;
    private final String from;
    private final String to;
    private final String subject;
    private final String body;
    private final DocumentLink documentLink;
    private final CorrespondenceType correspondenceType;
    private final String eventType;
    private final String reasonableAdjustmentStatus;

    @JsonCreator
    public CorrespondenceDetails(@JsonProperty("sentOn") String sentOn,
                                 @JsonProperty("from") String from,
                                 @JsonProperty("to") String to,
                                 @JsonProperty("subject") String subject,
                                 @JsonProperty("body") String body,
                                 @JsonProperty("documentLink") DocumentLink documentLink,
                                 @JsonProperty("correspondenceType") CorrespondenceType correspondenceType,
                                 @JsonProperty("eventType") String eventType,
                                 @JsonProperty("reasonableAdjustmentStatus") String reasonableAdjustmentStatus) {
        this.sentOn = sentOn;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
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

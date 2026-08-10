package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "correspondence", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CorrespondenceDetails {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm");
    @CCD(label = "Sent on")
    private final String sentOn;
    @CCD(label = "From")
    private final String from;
    @CCD(label = "To")
    private final String to;
    @CCD(ignore = true)
    private final String subject;
    @CCD(ignore = true)
    private final String body;
    @CCD(label = "Document url", typeOverride = FieldType.Document)
    private final DocumentLink documentLink;
    @CCD(
            label = "Correspondence type",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "correspondenceType"
    )
    private final CorrespondenceType correspondenceType;
    @CCD(ignore = true)
    private final String eventType;
    @CCD(
            label = "Reasonable adjustment status",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_reasonableAdjustmentStatus"
    )
    private final ReasonableAdjustmentStatus reasonableAdjustmentStatus;

    @JsonCreator
    public CorrespondenceDetails(@JsonProperty("sentOn") String sentOn,
                                 @JsonProperty("from") String from,
                                 @JsonProperty("to") String to,
                                 @JsonProperty("subject") String subject,
                                 @JsonProperty("body") String body,
                                 @JsonProperty("documentLink") DocumentLink documentLink,
                                 @JsonProperty("correspondenceType") CorrespondenceType correspondenceType,
                                 @JsonProperty("eventType") String eventType,
                                 @JsonProperty("reasonableAdjustmentStatus") ReasonableAdjustmentStatus reasonableAdjustmentStatus) {
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

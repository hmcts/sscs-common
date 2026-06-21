package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunicationRequestDetails {

    private LocalDateTime requestDateTime;
    private LocalDate requestResponseDueDate;
    private String requestUserName;
    private String requestUserRole;
    private CommunicationRequestTopic requestTopic;
    private String requestMessage;
    private CommunicationRequestReply requestReply;
    private String taskCreatedForRequest;

    @Override
    public String toString() {
        return MessageFormat.format("{0} - {1} - {2} - {3}",
            this.getRequestTopic().getValue(),
            this.getRequestDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm")),
            this.getRequestUserName(),
            this.getRequestUserRole());
    }
}


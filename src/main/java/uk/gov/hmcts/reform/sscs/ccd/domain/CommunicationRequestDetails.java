package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private CommunicationRequestTopic requestTopic;
    private String requestMessage;
    private CommunicationRequestReply requestReply;
    private YesNo requestActioned;
}


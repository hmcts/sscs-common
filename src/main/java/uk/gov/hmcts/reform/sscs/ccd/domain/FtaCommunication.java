package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class FtaCommunication {

    private LocalDateTime requestDateTime;
    private LocalDateTime requestDueDate;
    private String requestTopic;
    private String requestText;
    private String requestUserName;

}


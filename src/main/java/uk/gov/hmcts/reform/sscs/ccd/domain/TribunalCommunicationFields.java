package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.List;

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
public class TribunalCommunicationFields {

    private List<TribunalCommunication> tribunalCommunications;
    private String tribunalRequestTopic;
    private String tribunalRequestQuestion;
    private FtaCommunicationFilter ftaCommunicationFilter;
    private TribunalCommunicationFilter tribunalCommunicationFilter;
    private TribunalRequestType tribunalRequestType;
}

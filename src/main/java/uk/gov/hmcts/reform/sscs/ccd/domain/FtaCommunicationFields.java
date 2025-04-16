package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.time.LocalDate;
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
public class FtaCommunicationFields {

    private List<CommunicationRequest> ftaCommunications;
    private CommunicationRequestTopic ftaRequestTopic;
    private String ftaRequestQuestion;
    private FtaRequestType ftaRequestType;
    private DynamicList ftaRequestNoResponseRadioDl;
    private String ftaRequestNoResponseQuery;
    private String ftaRequestNoResponseTextArea;
    private List<String> ftaRequestNoResponseNoAction;

    private DynamicList ftaResponseNoActionedRadioDl;
    private String ftaResponseNoActionedQuery;
    private YesNo ftaResponseActioned;
    private String ftaResponseNoActionedReply;

    private List<CommunicationRequest> tribunalCommunications;
    private CommunicationRequestTopic tribunalRequestTopic;
    private String tribunalRequestQuestion;
    private TribunalRequestType tribunalRequestType;

    private DynamicList tribunalRequestRespondedDl;
    private String tribunalRequestRespondedQuery;
    private String tribunalRequestRespondedReply;
    private YesNo tribunalRequestRespondedActioned;

    private DynamicList tribunalRequestNoResponseRadioDl;
    private String tribunalRequestNoResponseQuery;
    private String tribunalRequestNoResponseTextArea;
    private List<String> tribunalRequestNoResponseNoAction;

    private DynamicList deleteCommRequestRadioDl;
    private CommunicationRequestDetails deleteCommRequestReadOnly;
    private String deleteCommRequestTextArea;
    private CommunicationRequestDetails deleteCommRequestReadOnlyStored;
    private String deleteCommRequestTextAreaStored;

    private LocalDate tribunalResponseDueDate;
    private LocalDate ftaResponseDueDate;
    private LocalDate tribunalResponseProvidedDate;
    private LocalDate ftaResponseProvidedDate;

    private YesNo awaitingInfoFromFta;
    private YesNo infoProvidedByFta;
    private YesNo infoRequestFromFta;
    private YesNo infoRequestFromTribunal;
    private YesNo awaitingInfoFromTribunal;
    private YesNo infoProvidedByTribunal;
}


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
    private CommunicationRequestTopic commRequestTopic;
    private String commRequestQuestion;
    private FtaRequestType ftaRequestType;
    private DynamicList ftaRequestsDl;
    private String ftaRequestNoResponseQuery;
    private String commRequestResponseTextArea;
    private List<String> commRequestResponseNoAction;

    private DynamicList tribunalRequestsDl;
    private String ftaRequestRespondedQuery;
    private String ftaRequestRespondedReply;
    private YesNo commRequestActioned;

    private DynamicList ftaResponseNoActionedRadioDl;
    private String ftaResponseNoActionedQuery;
    private YesNo ftaResponseActioned;
    private String ftaResponseNoActionedReply;

    private List<CommunicationRequest> tribunalCommunications;
    private TribunalRequestType tribunalRequestType;

    private String tribunalRequestRespondedQuery;
    private String tribunalRequestRespondedReply;

    private String tribunalRequestNoResponseQuery;

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


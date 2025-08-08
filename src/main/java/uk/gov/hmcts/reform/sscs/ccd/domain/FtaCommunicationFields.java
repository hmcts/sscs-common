package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;
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
    private FtaRequestType ftaRequestType;
    private DynamicList ftaRequestsDl;
    private String ftaRequestNoResponseQuery;

    private List<CommunicationRequest> tribunalCommunications;
    private TribunalRequestType tribunalRequestType;
    private DynamicList tribunalRequestsDl;
    private String tribunalRequestNoResponseQuery;

    private CommunicationRequestTopic commRequestTopic;
    private String commRequestQuestion;
    private String commRequestResponseTextArea;
    private List<String> commRequestResponseNoAction;

    private DynamicList deleteCommRequestRadioDl;
    private CommunicationRequestDetails deleteCommRequestReadOnly;
    private String deleteCommRequestTextArea;
    private CommunicationRequestDetails deleteCommRequestReadOnlyStored;
    private String deleteCommRequestTextAreaStored;

    private DynamicMixedChoiceList tribunalRequestsToReviewDl;
    private DynamicMixedChoiceList ftaRequestsToReviewDl;

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


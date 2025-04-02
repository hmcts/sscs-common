package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties (ignoreUnknown = true)
@Value
@Builder (toBuilder = true)
@JsonInclude (JsonInclude.Include.NON_NULL)
public class TribunalCommunication {
    TribunalCommunicationRequestDetails value;

    @JsonCreator
    public TribunalCommunication(@JsonProperty("value") TribunalCommunicationRequestDetails value){
        this.value = value;
    }
}

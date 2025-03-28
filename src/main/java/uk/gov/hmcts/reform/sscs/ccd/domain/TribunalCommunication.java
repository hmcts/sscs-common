package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@JsonIgnoreProperties (ignoreUnknown = true)
@Value
@Builder (toBuilder = true)
@JsonInclude (JsonInclude.Include.NON_NULL)
public class TribunalCommunication {
    TribunalCommunicationFields value;

    @JsonCreator
    public TribunalCommunication(@JsonProperty("value") TribunalCommunicationFields value){
        this.value = value;
    }
}

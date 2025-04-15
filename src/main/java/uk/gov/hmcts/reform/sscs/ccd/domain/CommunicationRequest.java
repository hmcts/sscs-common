package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunicationRequest {

    private String id;
    private CommunicationRequestDetails value;

    @JsonCreator
    public CommunicationRequest(@JsonProperty("id") String id,
                                @JsonProperty("value") CommunicationRequestDetails value) {
        this.id = id;
        if (isBlank(this.id)) {
            this.id = UUID.randomUUID().toString();
        }
        this.value = value;
    }
}

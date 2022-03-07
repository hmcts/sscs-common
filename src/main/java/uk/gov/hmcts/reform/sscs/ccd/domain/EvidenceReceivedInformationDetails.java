package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class EvidenceReceivedInformationDetails {

    private YesNo evidenceReceivedBoolean;
    private String evidenceReceivedDate;

    @JsonCreator
    public EvidenceReceivedInformationDetails(@JsonProperty("evidenceReceivedBoolean") YesNo evidenceReceivedBoolean,
                       @JsonProperty("evidenceReceivedDate") String evidenceReceivedDate) {
        this.evidenceReceivedBoolean = evidenceReceivedBoolean;
        this.evidenceReceivedDate = evidenceReceivedDate;
    }
}

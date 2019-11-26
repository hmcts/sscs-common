package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EvidenceReceivedInformation {

    private EvidenceReceivedInformationDetails value;

    @JsonCreator
    public EvidenceReceivedInformation(@JsonProperty("value") EvidenceReceivedInformationDetails value) {
        this.value = value;
    }
}

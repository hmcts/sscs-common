package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EvidenceReceivedInformationDetails {

    String evidenceReceivedBoolean;
    String evidenceReceivedDate;

    @JsonCreator
    public EvidenceReceivedInformationDetails(@JsonProperty("evidenceReceivedBoolean") String evidenceReceivedBoolean,
                       @JsonProperty("evidenceReceivedDate") String evidenceReceivedDate) {
        this.evidenceReceivedBoolean = evidenceReceivedBoolean;
        this.evidenceReceivedDate = evidenceReceivedDate;
    }
}

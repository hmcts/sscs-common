package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class OtherPartySelection {

    private OtherPartySelectionDetails value;

    @JsonCreator
    public OtherPartySelection(OtherPartySelectionDetails value) {
        this.value = value;
    }
}

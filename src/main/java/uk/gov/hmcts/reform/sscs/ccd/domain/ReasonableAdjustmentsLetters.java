package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class ReasonableAdjustmentsLetters {

    private List<Correspondence> appellant;
    private List<Correspondence> appointee;
    private List<Correspondence> representative;
    private List<Correspondence> jointParty;

    public ReasonableAdjustmentsLetters(@JsonProperty("appellant") List<Correspondence> appellant,
                                 @JsonProperty("appointee") List<Correspondence> appointee,
                                 @JsonProperty("representative") List<Correspondence> representative,
                                 @JsonProperty("jointParty") List<Correspondence> jointParty) {
        this.appellant = appellant;
        this.appointee = appointee;
        this.representative = representative;
        this.jointParty = jointParty;
    }
}

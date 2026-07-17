package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class ReasonableAdjustmentsLetters {

    @CCD(label = "Appellant", typeOverride = FieldType.Collection, typeParameterOverride = "correspondence")
    private List<Correspondence> appellant;
    @CCD(label = "Appointee", typeOverride = FieldType.Collection, typeParameterOverride = "correspondence")
    private List<Correspondence> appointee;
    @CCD(label = "Representative", typeOverride = FieldType.Collection, typeParameterOverride = "correspondence")
    private List<Correspondence> representative;
    @CCD(label = "Joint Party", typeOverride = FieldType.Collection, typeParameterOverride = "correspondence")
    private List<Correspondence> jointParty;
    @CCD(label = "Other Party", typeOverride = FieldType.Collection, typeParameterOverride = "correspondence")
    private List<Correspondence> otherParty;

    public ReasonableAdjustmentsLetters(@JsonProperty("appellant") List<Correspondence> appellant,
                                 @JsonProperty("appointee") List<Correspondence> appointee,
                                 @JsonProperty("representative") List<Correspondence> representative,
                                 @JsonProperty("jointParty") List<Correspondence> jointParty,
                                 @JsonProperty("otherParty") List<Correspondence> otherParty) {
        this.appellant = appellant;
        this.appointee = appointee;
        this.representative = representative;
        this.jointParty = jointParty;
        this.otherParty = otherParty;
                
    }
}

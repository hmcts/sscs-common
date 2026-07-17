package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;
import uk.gov.hmcts.reform.sscs.ccd.validation.localdate.LocalDateYearMustBeInPast;
import uk.gov.hmcts.reform.sscs.ccd.validation.nino.NationalInsuranceNumber;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Identity {

    @CCD(label = "Date of birth", typeOverride = FieldType.Date)
    @LocalDateYearMustBeInPast(message = "You’ve entered an invalid date of birth", groups = UniversalCreditValidationGroup.class)
    private String dob;
    @CCD(label = "National insurance number")
    @NationalInsuranceNumber(groups = UniversalCreditValidationGroup.class)
    private String nino;
    @CCD(label = "IBCA Reference Number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ibcaReference;

    @JsonCreator
    public Identity(@JsonProperty("dob") String dob,
                    @JsonProperty("nino") String nino,
                    @JsonProperty("ibcaReference") String ibcaReference) {
        this.dob = dob;
        this.nino = nino;
        this.ibcaReference = ibcaReference;
    }
    
    public Identity(@JsonProperty("dob") String dob,
                    @JsonProperty("nino") String nino) {
        this.dob = dob;
        this.nino = nino;
    }
}

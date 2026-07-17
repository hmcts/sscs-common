package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Appeal {
    @CCD(label = "MRN/Review Decision Notice Details")
    private MrnDetails mrnDetails;
    @CCD(label = "Appellant Details")
    private Appellant appellant;
    @CCD(label = "Benefit Type")
    private BenefitType benefitType;
    @CCD(label = "Hearing Options")
    private HearingOptions hearingOptions;
    @CCD(label = "Appeal Reasons")
    private AppealReasons appealReasons;
    @CCD(label = "Supporter Details")
    private Supporter supporter;
    @CCD(label = "Representative Details")
    private Representative rep;
    @CCD(label = "Signer")
    private String signer;
    @CCD(label = "Hearing Type", typeOverride = FieldType.FixedList, typeParameterOverride = "hearingType")
    private String hearingType;
    @CCD(label = "Hearing Subtype")
    private HearingSubtype hearingSubtype;
    @CCD(label = "Channel of receipt", typeOverride = FieldType.FixedList, typeParameterOverride = "receivedVia")
    private String receivedVia;

    @CCD(label = "Show Appellant Confidentiality Required Option", typeOverride = FieldType.YesOrNo)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo isOtherPartyAddedForChildMaintUCCase;

    public Appeal(@JsonProperty("mrnDetails") MrnDetails mrnDetails,
                  @JsonProperty("appellant") Appellant appellant,
                  @JsonProperty("benefitType") BenefitType benefitType,
                  @JsonProperty("hearingOptions") HearingOptions hearingOptions,
                  @JsonProperty("appealReasons") AppealReasons appealReasons,
                  @JsonProperty("supporter") Supporter supporter,
                  @JsonProperty("rep") Representative rep,
                  @JsonProperty("signer") String signer,
                  @JsonProperty("hearingType") String hearingType,
                  @JsonProperty("hearingSubtype") HearingSubtype hearingSubtype,
                  @JsonProperty("receivedVia") String receivedVia,
                  @JsonProperty("isOtherPartyAddedForChildMaintUCCase") YesNo isOtherPartyAddedForChildMaintUCCase) {
        this.mrnDetails = mrnDetails;
        this.appellant = appellant;
        this.benefitType = benefitType;
        this.hearingOptions = hearingOptions;
        this.appealReasons = appealReasons;
        this.supporter = supporter;
        this.rep = rep;
        this.signer = signer;
        this.hearingType = hearingType;
        this.hearingSubtype = hearingSubtype;
        this.receivedVia = receivedVia;
        this.isOtherPartyAddedForChildMaintUCCase = isOtherPartyAddedForChildMaintUCCase;
    }
}

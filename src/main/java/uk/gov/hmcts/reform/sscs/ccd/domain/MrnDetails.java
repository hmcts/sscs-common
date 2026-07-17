package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class MrnDetails {
    @CCD(label = "FTA Issuing Office")
    private String dwpIssuingOffice;
    @CCD(label = "MRN/Review Decision Notice Date", typeOverride = FieldType.Date)
    private String mrnDate;
    @CCD(label = "MRN/Review Decision Notice Late Reason")
    private String mrnLateReason;
    @CCD(label = "MRN/Review Decision Notice Missing Reason")
    private String mrnMissingReason;

    @JsonCreator
    public MrnDetails(@JsonProperty("dwpIssuingOffice") String dwpIssuingOffice,
                      @JsonProperty("mrnDate") String mrnDate,
                      @JsonProperty("mrnLateReason") String mrnLateReason,
                      @JsonProperty("mrnMissingReason") String mrnMissingReason) {
        this.dwpIssuingOffice = dwpIssuingOffice;
        this.mrnDate = mrnDate;
        this.mrnLateReason = mrnLateReason;
        this.mrnMissingReason = mrnMissingReason;
    }
}

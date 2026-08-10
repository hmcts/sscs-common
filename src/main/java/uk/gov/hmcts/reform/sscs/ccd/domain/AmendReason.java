package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "FL_amendReason", generate = true)
@Getter
@AllArgsConstructor
public enum AmendReason {
    @CCD(label = "Party requested change")
    PARTY_REQUEST("partyreq","Party requested change"),
    @CCD(label = "Judge requested change")
    JUDGE_REQUEST("judgereq","Judge requested change"),
    @CCD(label = "Admin requested change")
    ADMIN_REQUEST("adminreq", "Admin requested change"),
    @CCD(label = "Admin error")
    ADMIN_ERROR("adminerr", "Admin error");

    private final String ccdDefinition;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return ccdDefinition;
    }
}

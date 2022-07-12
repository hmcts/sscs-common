package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AmendReason {
    PARTY_REQUEST("partyreq","Party requested change"),
    JUDGE_REQUEST("judgereq","Judge requested change"),
    ADMIN_REQUEST("adminreq", "Admin requested change"),
    ADMIN_ERROR("adminerr", "Admin error");

    @JsonValue
    private final String ccdDefinition;
    private final String descriptionEn;
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherParty extends Party {

    private HearingOptions hearingOptions;
    private HearingSubtype hearingSubtype;

    private YesNo unacceptableCustomerBehaviour;

    private Representative rep;

    private String isAppointee;

    private Appointee appointee;

    private YesNo showRole;

    @JsonIgnore
    public boolean hasAppointee() {
        return nonNull(appointee) && isYes(isAppointee);
    }

    @JsonIgnore
    public boolean hasRepresentative() {
        return nonNull(rep) && isYes(rep.getHasRepresentative());
    }
}

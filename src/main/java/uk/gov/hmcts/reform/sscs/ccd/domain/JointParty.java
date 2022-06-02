package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class JointParty extends Party {

    @JsonProperty("jointPartyId")
    private String id;

    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    @JsonProperty("jointPartyIdentity")
    private Identity identity;

    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    @JsonProperty("jointPartyAddress")
    private Address address;

    @JsonProperty("jointPartyName")
    private Name name;

    @JsonProperty("jointParty")
    private YesNo hasJointParty;

    private YesNo jointPartyAddressSameAsAppellant;
}

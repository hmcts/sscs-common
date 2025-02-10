package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class JointParty extends Party {

    @Builder.Default
    @JsonProperty("jointPartyId")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String id = UUID.randomUUID().toString();

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

    @JsonProperty("jointPartyContact")
    private Contact contact;

    private YesNo jointPartyAddressSameAsAppellant;

    @Override
    @JsonGetter
    @JsonProperty("jointPartyId")
    public String getId() {
        if (isBlank(id)) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    @Override
    @JsonSetter
    @JsonProperty("jointPartyId")
    public void setId(String id) {
        this.id = id;
        if (isBlank(this.id)) {
            this.id = UUID.randomUUID().toString();
        }
    }
}

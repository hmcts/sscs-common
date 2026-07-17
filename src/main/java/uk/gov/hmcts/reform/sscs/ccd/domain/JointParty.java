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
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class JointParty extends Party {

    @CCD(label = "Joint party id", access = {SscsCitizenCrudAccess.class, SuperuserSystemupdateCrudAccess.class})
    @Builder.Default
    @JsonProperty("jointPartyId")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String id = UUID.randomUUID().toString();

    @CCD(label = "Joint party identity", access = {SscsCrudAccess.class})
    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    @JsonProperty("jointPartyIdentity")
    private Identity identity;

    @CCD(label = "Joint party address", access = {SscsCrudAccess.class})
    @Valid
    @ConvertGroup(to = UniversalCreditValidationGroup.class)
    @JsonProperty("jointPartyAddress")
    private Address address;

    @CCD(label = "Joint party name", access = {SscsCrudAccess.class})
    @JsonProperty("jointPartyName")
    private Name name;

    @CCD(
            label = "Is there a joint party on this case?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("jointParty")
    private YesNo hasJointParty;

    @CCD(label = "Joint party contact", access = {SscsCitizenCrudAccess.class, SuperuserSystemupdateCrudAccess.class})
    @JsonProperty("jointPartyContact")
    private Contact contact;

    @CCD(
            label = "Is their address the same as the appellant's?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
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

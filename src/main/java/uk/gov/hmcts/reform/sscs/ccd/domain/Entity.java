package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude
public abstract class Entity {

    @CCD(label = "Id", showCondition = "name=\"AnyValueToFailThisCondition\"")
    @Builder.Default
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String id = UUID.randomUUID().toString();
    @CCD(label = "Identity", showCondition = "hasRepresentative=\"Yes\"")
    private Identity identity;

    @CCD(label = "Name", showCondition = "hasRepresentative=\"Yes\"")
    private Name name;
    @CCD(label = "Address Details", showCondition = "hasRepresentative=\"Yes\"")
    private Address address;
    @CCD(label = "Contact Details", showCondition = "hasRepresentative=\"Yes\"")
    private Contact contact;
    @CCD(label = "Organisation", showCondition = "hasRepresentative=\"Yes\"")
    private String organisation;

    public String getId() {
        if (isBlank(id)) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (isBlank(this.id)) {
            this.id = UUID.randomUUID().toString();
        }
    }
}

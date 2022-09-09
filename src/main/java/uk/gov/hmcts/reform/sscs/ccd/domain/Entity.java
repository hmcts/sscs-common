package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude
public abstract class Entity {
    @Getter(AccessLevel.NONE)
    private String id;
    private Identity identity;

    private Name name;
    private Address address;
    private Contact contact;
    private String organisation;

    public String getId() {
        if (isBlank(id)) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }
}

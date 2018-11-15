package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class Appointee {

    private Name name;
    private Address address;
    private Contact contact;
    private Identity identity;

    @JsonCreator
    public Appointee(@JsonProperty("name") Name name,
                     @JsonProperty("address") Address address,
                     @JsonProperty("contact") Contact contact,
                     @JsonProperty("identity") Identity identity) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.identity = identity;
    }
}

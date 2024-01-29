package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.ccd.domain.Contact;
import uk.gov.hmcts.reform.sscs.ccd.domain.Name;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class PoDetails {
    @JsonProperty("name")
    private Name name;
    @JsonProperty("contact")
    private Contact contact;

    @SuppressWarnings("unused")
    @JsonIgnore
    public Name getName() {
        if (name == null) {
            this.name = new Name();
        }
        return name;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public Contact getContact() {
        if (contact == null) {
            this.contact = new Contact();
        }
        return contact;
    }
}

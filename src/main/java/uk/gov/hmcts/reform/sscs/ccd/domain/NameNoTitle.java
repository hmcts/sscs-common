package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class NameNoTitle {
    private String firstName;
    private String lastName;

    @JsonCreator
    public NameNoTitle(
                @JsonProperty("firstName") String firstName,
                @JsonProperty("lastName") String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getFullName(String title) {
        return title + " " + firstName + " " + lastName;
    }

    @JsonIgnore
    public String getFullNameNoTitle() {
        return firstName + " " + lastName;
    }

    @JsonIgnore
    public String getAbbreviatedFullName(String title) {
        return title + " " + StringUtils.defaultIfBlank(StringUtils. substring(firstName, 0, 1), StringUtils.EMPTY) + " " + lastName;
    }
}

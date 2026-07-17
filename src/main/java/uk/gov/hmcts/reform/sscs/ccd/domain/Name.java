package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class Name {
    @CCD(label = "Title")
    private String title;
    @CCD(label = "First Name")
    private String firstName;
    @CCD(label = "Middle Name")
    private String middleName;
    @CCD(label = "Last Name")
    private String lastName;

    @JsonCreator
    public Name(@JsonProperty("title") String title,
                @JsonProperty("firstName") String firstName,
                @JsonProperty("middleName") String middleName,
                @JsonProperty("lastName") String lastName) {
        this.title = title;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getFullName() {
        return title + " " + firstName + " " + lastName;
    }

    @JsonIgnore
    public String getFullNameNoTitle() {
        return firstName + " " + lastName;
    }

    @JsonIgnore
    public String getAbbreviatedFullName() {
        return title + " " + StringUtils.defaultIfBlank(StringUtils. substring(firstName, 0, 1), StringUtils.EMPTY) + " " + lastName;
    }
}

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
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "name", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class Name {
    @CCD(
            label = "Title",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_titles",
            typeParameterClass = Titles.class
    )
    private String title;
    @CCD(label = "First Name")
    private String firstName;
    @CCD(label = "Last Name")
    private String lastName;

    @JsonCreator
    public Name(@JsonProperty("title") String title,
                @JsonProperty("firstName") String firstName,
                @JsonProperty("lastName") String lastName,
                @JsonProperty("middleName") String middleName) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    /** Retained so existing positional call sites still compile. */
    public Name(String title, String firstName, String lastName) {
        this(title, firstName, lastName, null);
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

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "Middle Name")
  private String middleName;
  // ==== end synthesised definition-only fields ====
}

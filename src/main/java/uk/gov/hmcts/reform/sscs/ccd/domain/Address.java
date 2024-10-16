package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.sscs.ccd.validation.address.Postcode;
import uk.gov.hmcts.reform.sscs.ccd.validation.groups.UniversalCreditValidationGroup;
import uk.gov.hmcts.reform.sscs.ccd.validation.string.StringNoSpecialCharacters;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @StringNoSpecialCharacters(fieldName = "Line 1", groups = {UniversalCreditValidationGroup.class})
    private String line1;
    @StringNoSpecialCharacters(fieldName = "Line 2", groups = {UniversalCreditValidationGroup.class})
    private String line2;
    @StringNoSpecialCharacters(fieldName = "Town", groups = {UniversalCreditValidationGroup.class})
    private String town;
    @StringNoSpecialCharacters(fieldName = "County", groups = {UniversalCreditValidationGroup.class})
    private String county;
    @Postcode(groups = {UniversalCreditValidationGroup.class})
    private String postcode;
    private String postcodeLookup;
    private String postcodeAddress;
    private String portOfEntry;
    private String country;
    private YesNo isInUk;
    private DynamicList ukPortOfEntryList;

    @JsonCreator
    public Address(@JsonProperty("line1") String line1,
                   @JsonProperty("line2") String line2,
                   @JsonProperty("town") String town,
                   @JsonProperty("county") String county,
                   @JsonProperty("postcode") String postcode,
                   @JsonProperty("postcodeLookup") String postcodeLookup,
                   @JsonProperty("postcodeAddress") String postcodeAddress,
                   @JsonProperty("ukPortOfEntry") String portOfEntry,
                   @JsonProperty("country") String country,
                   @JsonProperty("inUk") YesNo isInUk,
                   @JsonProperty("ukPortOfEntryList") DynamicList ukPortOfEntryList) {
        this.line1 = line1;
        this.line2 = line2;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
        this.postcodeLookup = postcodeLookup;
        this.postcodeAddress = postcodeAddress;
        this.portOfEntry = portOfEntry;
        this.country = country;
        this.isInUk = isInUk;
        this.ukPortOfEntryList = ukPortOfEntryList;
    }
    
    public Address(@JsonProperty("line1") String line1,
                   @JsonProperty("line2") String line2,
                   @JsonProperty("town") String town,
                   @JsonProperty("county") String county,
                   @JsonProperty("postcode") String postcode,
                   @JsonProperty("postcodeLookup") String postcodeLookup,
                   @JsonProperty("postcodeAddress") String postcodeAddress) {
        this.line1 = line1;
        this.line2 = line2;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
        this.postcodeLookup = postcodeLookup;
        this.postcodeAddress = postcodeAddress;
    }

    @JsonIgnore
    public String getFullAddress() {
        if (isYes(isInUk) || isInUk == null) {
            return Stream.of(
                    line1,
                    line2,
                    town,
                    county,
                    postcode)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(", "));
        } else {
            return Stream.of(
                    line1,
                    line2,
                    town,
                    postcode,
                    country)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(", "));
        }
    }

    @JsonIgnore
    public boolean isAddressEmpty() {
        if (isYes(isInUk) || isInUk == null) {
            return Stream.of(
                    line1,
                    line2,
                    town,
                    county,
                    postcode).allMatch(StringUtils::isEmpty);
        } else {
            return Stream.of(
                    line1,
                    line2,
                    town,
                    country,
                    portOfEntry).allMatch(StringUtils::isEmpty);
        }
    }

    @JsonIgnore
    public UkPortOfEntry getPortOfEntry() {
        return Arrays.stream(UkPortOfEntry.values())
            .filter(port -> port.getLocationCode().equalsIgnoreCase(this.portOfEntry))
            .findFirst()
            .orElse(null);
    }

}

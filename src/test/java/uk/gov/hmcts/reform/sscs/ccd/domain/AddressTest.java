package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class AddressTest {

    @Test
    public void givenAnAddressWithNoValuesEntered_thenReturnTrueForEmptyAddress() {
        Address address = Address.builder().build();

        assertTrue(address.isAddressEmpty());
    }

    @Test
    public void givenAddressIsPartiallyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().line1("My road").build();

        assertFalse(address.isAddressEmpty());
    }

    @Test
    public void givenAddressIsFullyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().line1("My road").line2("My road")
            .town("Brentwood").county("Essex").build();

        assertFalse(address.isAddressEmpty());
    }

    @Test
    public void givenAnNonUKAddressWithNoValuesEntered_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().portOfEntry("GB000461").inMainlandUk(NO).build();

        assertFalse(address.isAddressEmpty());
    }

    @Test
    public void givenNonAddressIsPartiallyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().country("Sweden").portOfEntry("GB000170").inMainlandUk(NO).build();

        assertFalse(address.isAddressEmpty());
    }

    @Test
    public void givenNonAddressIsFullyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder()
            .line1("123 Outside London")
            .line2("Ikea")
            .town("Away")
            .country("Oslo")
            .portOfEntry("GB000130")
            .inMainlandUk(NO)
            .build();

        assertFalse(address.isAddressEmpty());
    }


    @Test
    public void givenIsLivingInTheUk_thenIsLivingInTheUkTrue() {
        Address address = Address.builder()
            .line1("My road")
            .line2("My road")
            .town("Brentwood")
            .county("Essex")
            .inMainlandUk(YES)
            .build();

        assertTrue(isYes(address.getInMainlandUk()));
    }

    @Test
    public void givenIsNotLivingInTheUk_thenIsLivingInTheUkFalse() {
        Address address = Address.builder()
            .line1("123 New Forest Drive")
            .line2("My road")
            .town("Altanta")
            .portOfEntry("GB000170")
            .inMainlandUk(NO)
            .build();

        assertTrue(YesNo.isNoOrNull(address.getInMainlandUk()));
    }

    @Test
    public void givenIsLivingInTheUk_thenFullAddressHasPostcode() {
        String expectedAddress = "1a Carnival Road, Ladbroke Grove, Notting Hill, W10 5QA";

        Address address = Address.builder()
            .line1("1a Carnival Road")
            .line2("Ladbroke Grove")
            .town("Notting Hill")
            .postcode("W10 5QA")
            .inMainlandUk(YES)
            .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }

    @Test
    public void givenIsLivingInNI_thenFullAddressHasPostcode() {
        String expectedAddress = "Stranmillis Road, Botanic Gardens, Belfast, BT9 5AB";

        Address address = Address.builder()
                .line1("Stranmillis Road")
                .line2("Botanic Gardens")
                .town("Belfast")
                .postcode("BT9 5AB")
                .inMainlandUk(YES)
                .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }

    @Test
    public void givenIsLivingInNI_thenIsLivingInTheUkTrue() {
        Address address = Address.builder()
                .line1("Stranmillis Road")
                .line2("Botanic Gardens")
                .town("Belfast")
                .postcode("BT9 5AB")
                .inMainlandUk(YES)
                .build();

        assertTrue(isYes(address.getInMainlandUk()));
    }

    @Test
    public void givenIsNotLivingInTheUk_thenFullAddressHasCountryAndNoPostcode() {
        String expectedAddress = "123 New Forest Drive, My road, Washington, USA";

        Address address = Address.builder()
            .line1("123 New Forest Drive")
            .line2("My road")
            .town("Washington")
            .country("USA")
            .inMainlandUk(NO)
            .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }

    @Test
    public void givenIsNotLivingInTheUk_thenFullAddressHasCountryAndPostcode() {
        String expectedAddress = "123 New Forest Drive, My road, Washington, 98101, USA";

        Address address = Address.builder()
            .line1("123 New Forest Drive")
            .line2("My road")
            .town("Washington")
            .postcode("98101")
            .country("USA")
            .inMainlandUk(NO)
            .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }

    @ParameterizedTest
    @EnumSource(value = UkPortOfEntry.class)
    public void getPortOfEntry_returns_correct_portOfEntry_for_valid_locationCode(UkPortOfEntry port) {
        Address address = Address.builder()
            .line1("123 New Forest Drive")
            .line2("My road")
            .town("Washington")
            .portOfEntry(port.getLocationCode())
            .country("USA")
            .inMainlandUk(NO)
            .build();
        assertEquals(port, address.getUkPortOfEntry());
    }

    @Test
    public void getPortOfEntry_returns_null_for_invalid_locationCode() {
        Address address = Address.builder()
            .line1("123 New Forest Drive")
            .line2("My road")
            .town("Washington")
            .portOfEntry("some-invalid-code")
            .country("USA")
            .inMainlandUk(NO)
            .build();
        assertNull(address.getUkPortOfEntry());
    }
}

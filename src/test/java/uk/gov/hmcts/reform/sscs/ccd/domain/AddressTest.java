package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.*;

import org.junit.Test;

public class AddressTest {

    @Test
    public void givenAnAddressWithNoValuesEntered_thenReturnTrueForEmptyAddress() {
        Address address = Address.builder().build();

        assertEquals(true, address.isAddressEmpty());
    }

    @Test
    public void givenAddressIsPartiallyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().line1("My road").build();

        assertEquals(false, address.isAddressEmpty());
    }

    @Test
    public void givenAddressIsFullyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().line1("My road").line2("My road")
                .town("Brentwood").county("Essex").build();

        assertEquals(false, address.isAddressEmpty());
    }

    @Test
    public void givenAnNonUKAddressWithNoValuesEntered_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().portOfEntry(UkPortOfEntry.ST_PETER_PORT_GUERNSEY).isInUk(NO).build();

        assertEquals(false, address.isAddressEmpty());
    }

    @Test
    public void givenNonAddressIsPartiallyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().country("Sweden").portOfEntry(UkPortOfEntry.LONDON_GATEWAY_PORT).isInUk(NO).build();

        assertEquals(false, address.isAddressEmpty());
    }

    @Test
    public void givenNonAddressIsFullyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder()
                .line1("123 Outside London")
                .line2("Ikea")
                .town("Away")
                .country("Oslo")
                .portOfEntry(UkPortOfEntry.LONDON_CITY_AIRPORT)
                .isInUk(NO)
                .build();

        assertEquals(false, address.isAddressEmpty());
    }


    @Test
    public void givenIsLivingInTheUk_thenIsLivingInTheUkTrue() {
        Address address = Address.builder()
                .line1("My road")
                .line2("My road")
                .town("Brentwood")
                .county("Essex")
                .isInUk(YES)
                .build();

        assertTrue(isYes(address.getIsInUk()));
    }

    @Test
    public void givenIsLivingInTheUk_thenIsLivingInTheUkTrueByDefault() {
        Address address = Address.builder()
                .line1("My road")
                .line2("My road")
                .town("Brentwood")
                .county("Essex")
                .build();

        assertTrue(isYes(address.getIsInUk()));
    }

    @Test
    public void givenIsNotLivingInTheUk_thenIsLivingInTheUkFalse() {
        Address address = Address.builder()
                .line1("123 New Forest Drive")
                .line2("My road")
                .town("Altanta")
                .portOfEntry(UkPortOfEntry.LONDON_GATEWAY_PORT)
                .isInUk(NO)
                .build();

        assertTrue(YesNo.isNoOrNull(address.getIsInUk()));
    }

    @Test
    public void givenIsLivingInTheUk_thenFullAddressHasPostcode() {
        String expectedAddress = "1a Carnival Road, Ladbroke Grove, Notting Hill, W10 5QA";

        Address address = Address.builder()
                .line1("1a Carnival Road")
                .line2("Ladbroke Grove")
                .town("Notting Hill")
                .postcode("W10 5QA")
                .isInUk(YES)
                .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }

    @Test
    public void givenIsNotLivingInTheUk_thenFullAddressHasCountryAndNoPostcode() {
        String expectedAddress = "123 New Forest Drive, My road, Washington, USA, GB000461";

        Address address = Address.builder()
                .line1("123 New Forest Drive")
                .line2("My road")
                .town("Washington")
                .country("USA")
                .portOfEntry(UkPortOfEntry.ST_PETER_PORT_GUERNSEY)
                .isInUk(NO)
                .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }
}

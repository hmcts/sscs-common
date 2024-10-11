package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

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
    public void givenAnNonUKAddressWithNoValuesEntered_thenReturnTrueForEmptyAddress() {
        Address address = Address.builder().isLivingInUk(NO).build();

        assertEquals(true, address.isAddressEmpty());
    }

    @Test
    public void givenNonAddressIsPartiallyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder().country("Sweden").isLivingInUk(NO).build();

        assertEquals(false, address.isAddressEmpty());
    }

    @Test
    public void givenNonAddressIsFullyPopulated_thenReturnFalseForEmptyAddress() {
        Address address = Address.builder()
                .line1("123 Outside London")
                .line2("Ikea")
                .town("Away")
                .country("Oslo")
                .isLivingInUk(NO)
                .build();

        assertEquals(false, address.isAddressEmpty());
    }



    @Test
    public void givenIsLivingInTheUk_thenIsAppellantLivingInTheUkTrue() {
        Address address = Address.builder()
                .line1("My road")
                .line2("My road")
                .town("Brentwood")
                .county("Essex")
                .isLivingInUk(YES)
                .build();

        assertTrue(YesNo.isYes(address.getIsLivingInUk()));
    }

    @Test
    public void givenIsNotLivingInTheUk_thenIsAppellantLivingInTheUkFalse() {
        Address address = Address.builder()
                .line1("123 New Forest Drive")
                .line2("My road")
                .town("Altanta")
                .isLivingInUk(NO)
                .build();

        assertTrue(YesNo.isNoOrNull(address.getIsLivingInUk()));
    }

    @Test
    public void givenIsLivingInTheUk_thenFullAddressHasPostcode() {
        String expectedAddress = "1a Carnival Road, Ladbroke Grove, Notting Hill, W10 5QA";

        Address address = Address.builder()
                .line1("1a Carnival Road")
                .line2("Ladbroke Grove")
                .town("Notting Hill")
                .postcode("W10 5QA")
                .isLivingInUk(YES)
                .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }

    @Test
    public void givenIsNotLivingInTheUk_thenFullAddressHasCountryAndNoPostcode() {
        String expectedAddress = "123 New Forest Drive, My road, Washington, USA";

        Address address = Address.builder()
                .line1("123 New Forest Drive")
                .line2("My road")
                .town("Washington")
                .country("USA")
                .isLivingInUk(NO)
                .build();

        assertEquals(expectedAddress, address.getFullAddress());
    }

    @Test
    public void givenIsLivingInTheUk_thenIsAppellantHasPortOfEntry() {
        Address address = Address.builder()
                .line1("My road")
                .line2("My road")
                .town("Brentwood")
                .county("Essex")
                .portOfEntry(UkPortOfEntry.LONDON_GATEWAY_PORT)
                .isLivingInUk(YES)
                .build();

        assertEquals(UkPortOfEntry.LONDON_GATEWAY_PORT, address.getPortOfEntry());
    }
}

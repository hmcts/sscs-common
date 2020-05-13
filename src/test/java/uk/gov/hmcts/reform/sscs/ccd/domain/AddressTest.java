package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

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
}

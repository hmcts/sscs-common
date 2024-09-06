package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.validation.address.PostcodeValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppealPostcodeHelperTest {

    @Mock
    private PostcodeValidator postcodeValidator;

    @InjectMocks
    private AppealPostcodeHelper appealPostcodeHelper;

    @Test
    public void shouldReturnAppointeePostcode_givenAppointeeAddressExists_andAppointeePostcodeIsValid() {
        when(postcodeValidator.isValidPostcodeFormat("CR2 8YY")).thenReturn(true);
        when(postcodeValidator.isValid("CR2 8YY", null)).thenReturn(true);

        Appellant testAppellant = Appellant.builder()
            .address(Address.builder()
                .postcode("TS3 6NM").build())
            .appointee(Appointee.builder()
                .address(Address.builder()
                    .postcode("CR2 8YY")
                    .build())
                .build())
            .build();

        String actualPostcode = appealPostcodeHelper.resolvePostcode(testAppellant);

        assertThat(actualPostcode).isEqualTo("CR2 8YY");
    }

    @Test
    public void shouldReturnAppellantPostcode_givenInvalidAppointeePostcode_andAppellantPostcodeIsValid() {
        when(postcodeValidator.isValidPostcodeFormat("TS3 6NM")).thenReturn(true);
        when(postcodeValidator.isValid("TS3 6NM", null)).thenReturn(true);

        Appellant testAppellant = Appellant.builder()
            .address(Address.builder()
                .postcode("TS3 6NM").build())
            .appointee(Appointee.builder()
                .address(Address.builder()
                    .postcode("CR2 8YY")
                    .build())
                .build())
            .build();

        String actualPostcode = appealPostcodeHelper.resolvePostcode(testAppellant);

        assertThat(actualPostcode).isEqualTo("TS3 6NM");
    }

    @Test
    public void shouldReturnAppellantPostcode_givenAppointeeAddressDoesNotExist_andAppellantPostcodeIsValid() {
        when(postcodeValidator.isValidPostcodeFormat("TS3 6NM")).thenReturn(true);
        when(postcodeValidator.isValid("TS3 6NM", null)).thenReturn(true);

        Appellant testAppellant = Appellant.builder()
            .address(Address.builder()
                .postcode("TS3 6NM").build())
            .appointee(Appointee.builder().build())
            .build();

        String actualPostcode = appealPostcodeHelper.resolvePostcode(testAppellant);

        assertThat(actualPostcode).isEqualTo("TS3 6NM");
    }

    @Test
    public void shouldReturnBlankPostcode_givenAppointeeAndAppellantPostcodeAreNotValid() {
        Appellant testAppellant = Appellant.builder()
            .address(Address.builder()
                .postcode("TS3 6NM").build())
            .appointee(Appointee.builder()
                .address(Address.builder()
                    .postcode("CR2 8YY")
                    .build())
                .build())
            .build();

        String actualPostcode = appealPostcodeHelper.resolvePostcode(testAppellant);

        assertThat(actualPostcode).isEmpty();
    }

    @Test
    public void shouldReturnBlankPostcode_givenAppointeeAndAppellantPostcodeDoNotExist() {
        Appellant testAppellant = Appellant.builder()
            .address(Address.builder().build())
            .appointee(Appointee.builder()
                .address(Address.builder().build())
                .build())
            .build();

        String actualPostcode = appealPostcodeHelper.resolvePostcode(testAppellant);

        assertThat(actualPostcode).isEmpty();
    }

    @Test
    public void shouldReturnBlankPostcode_givenAppointeeAndAppellantAddressDoNotExist() {
        Appellant testAppellant = Appellant.builder().build();

        String actualPostcode = appealPostcodeHelper.resolvePostcode(testAppellant);

        assertThat(actualPostcode).isEmpty();
    }

}
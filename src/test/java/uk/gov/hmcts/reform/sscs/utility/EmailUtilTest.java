package uk.gov.hmcts.reform.sscs.utility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmailUtilTest {

    @ParameterizedTest
    @MethodSource("generateValidAndInvalidEmails")
    public void givenAListOfEmails_ThenVerifyIfEmailIsValidOrNot(String email, boolean isValid) {
        assertThat(EmailUtil.isEmailValid(email)).isEqualTo(isValid);
    }

    private Object[] generateValidAndInvalidEmails() {
        return new Object[]{
            new Object[]{"email@example.com", "true"},
            new Object[]{"firstname.lastname@example.com", "true"},
            new Object[]{"email@subdomain.example.com", "true"},
            new Object[]{"firstname+lastname@example.com", "true"},
            new Object[]{"email@123.123.123.123", "true"},
            new Object[]{"1234567890@example.com", "true"},
            new Object[]{"email@example-one.com", "true"},
            new Object[]{"_______@example.com", "true"},
            new Object[]{"email@example.name", "true"},
            new Object[]{"email@example.museum", "true"},
            new Object[]{"email@example.co.jp", "true"},
            new Object[]{"firstname-lastname@example.com", "true"},
            new Object[]{"plainaddress", "false"},
            new Object[]{"#@%^%#$@#$@#.com", "false"},
            new Object[]{"@example.com", "false"},
            new Object[]{"Joe Smith <email@example.com>", "false"},
            new Object[]{"email.example.com", "false"},
            new Object[]{"email@example@example.com", "false"},
            new Object[]{".email@example.com", "false"},
            new Object[]{"email.@example.com", "false"},
            new Object[]{"email..email@example.com", "false"},
            new Object[]{"あいうえお@example.com", "false"},
            new Object[]{"email@example.com (Joe Smith)", "false"},
            new Object[]{"email@example..com", "false"},
            new Object[]{"Abc..123@example.com", "false"},
            new Object[]{"null", "false"}
        };
    }
}
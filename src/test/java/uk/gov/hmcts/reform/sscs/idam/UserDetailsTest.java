package uk.gov.hmcts.reform.sscs.idam;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UserDetailsTest {

    final UserDetails userDetails = new UserDetails("id", "email", new ArrayList<>());

    @Test
    @Parameters({
            "caseworker-sscs,caseworker-sscs-judge",
            "caseworker-sscs-judge",
    })
    public void userWithJudgeRoles_shouldReturnTrue(String... roleWithJudge) {
        userDetails.getRoles().addAll(asList(roleWithJudge));
        assertThat(userDetails.hasJudgeRole(), is(true));
    }

    @Test
    @Parameters({
            "caseworker-sscs,caseworker-sscs-panelmember",
            "caseworker-sscs,caseworker-sscs-dwpresponsewriter",
            "caseworker-sscs,caseworker-sscs-registrar"
    })
    public void userWithNoJudgeRoles_shouldReturnFalse(String... roles) {
        userDetails.getRoles().addAll(asList(roles));
        assertThat(userDetails.hasJudgeRole(), is(false));
    }

    @Test
    public void userWithRoles_shouldHandleNullRoles() {
        UserDetails userDetails = new UserDetails("id", "email", null);
        assertThat(userDetails.hasJudgeRole(), is(false));
    }
}

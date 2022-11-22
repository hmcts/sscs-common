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
        "caseworker-sscs-judge"
    })
    public void userWithJudgeRoles_shouldReturnTrue(String... roleWithJudge) {
        userDetails.getRoles().addAll(asList(roleWithJudge));
        assertThat(userDetails.hasRole(UserRole.JUDGE), is(true));
    }

    @Test
    @Parameters({
        "caseworker-sscs,caseworker-sscs-panelmember",
        "caseworker-sscs,caseworker-sscs-dwpresponsewriter",
        "caseworker-sscs,caseworker-sscs-registrar",
        "caseworker-sscs,caseworker-sscs-superuser"
    })
    public void userWithNoJudgeRoles_shouldReturnFalse(String... roles) {
        userDetails.getRoles().addAll(asList(roles));
        assertThat(userDetails.hasRole(UserRole.JUDGE), is(false));
    }

    @Test
    @Parameters({
        "caseworker-sscs,caseworker-sscs-panelmember",
        "caseworker-sscs,caseworker-sscs-dwpresponsewriter",
        "caseworker-sscs,caseworker-sscs-registrar",
        "caseworker-sscs,caseworker-sscs-judge"
    })
    public void userWithNoSuperUserRoles_shouldReturnFalse(String... roles) {
        userDetails.getRoles().addAll(asList(roles));
        assertThat(userDetails.hasRole(UserRole.SUPER_USER), is(false));
    }

    @Test
    @Parameters({
        "caseworker-sscs,caseworker-sscs-superuser",
        "caseworker-sscs-superuser"
    })
    public void userWithSuperUserRoles_shouldReturnTrue(String... userWithSuperUserRole) {
        userDetails.getRoles().addAll(asList(userWithSuperUserRole));
        assertThat(userDetails.hasRole(UserRole.SUPER_USER), is(true));
    }

    @Test
    @Parameters({
        "caseworker-sscs,caseworker-sscs-panelmember",
        "caseworker-sscs,caseworker-sscs-registrar",
        "caseworker-sscs,caseworker-sscs-judge"
    })
    public void userWithNoDwpRoles_shouldReturnFalse(String... roles) {
        userDetails.getRoles().addAll(asList(roles));
        assertThat(userDetails.hasRole(UserRole.DWP), is(false));
    }

    @Test
    @Parameters({
        "caseworker-sscs,caseworker-sscs-dwpresponsewriter",
        "caseworker-sscs-dwpresponsewriter"
    })
    public void userWithDwpRoles_shouldReturnTrue(String... userWithSuperUserRole) {
        userDetails.getRoles().addAll(asList(userWithSuperUserRole));
        assertThat(userDetails.hasRole(UserRole.DWP), is(true));
    }

    @Test
    public void userWithRoles_shouldHandleNullRoles() {
        UserDetails userDetails = new UserDetails("id", "email", null);
        assertThat(userDetails.hasRole(UserRole.TCW), is(false));
        assertThat(userDetails.hasRole(UserRole.DWP), is(false));
    }
}

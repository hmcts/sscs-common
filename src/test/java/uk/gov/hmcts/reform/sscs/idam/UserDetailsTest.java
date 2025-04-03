package uk.gov.hmcts.reform.sscs.idam;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserDetailsTest {

    final UserDetails userDetails = new UserDetails("id", "email", new ArrayList<>());

    @ParameterizedTest
    @ValueSource(strings = {
        "caseworker-sscs,caseworker-sscs-judge",
        "caseworker-sscs-judge"
    })
    public void userWithJudgeRoles_shouldReturnTrue(String roleWithJudge) {
        userDetails.getRoles().addAll(asList(roleWithJudge.split(",")));
        assertThat(userDetails.hasRole(UserRole.JUDGE), is(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "caseworker-sscs,caseworker-sscs-panelmember",
        "caseworker-sscs,caseworker-sscs-dwpresponsewriter",
        "caseworker-sscs,caseworker-sscs-registrar",
        "caseworker-sscs,caseworker-sscs-superuser"
    })
    public void userWithNoJudgeRoles_shouldReturnFalse(String roles) {
        userDetails.getRoles().addAll(asList(roles.split(",")));
        assertThat(userDetails.hasRole(UserRole.JUDGE), is(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "caseworker-sscs,caseworker-sscs-panelmember",
        "caseworker-sscs,caseworker-sscs-dwpresponsewriter",
        "caseworker-sscs,caseworker-sscs-registrar",
        "caseworker-sscs,caseworker-sscs-judge"
    })
    public void userWithNoSuperUserRoles_shouldReturnFalse(String roles) {
        userDetails.getRoles().addAll(asList(roles.split(",")));
        assertThat(userDetails.hasRole(UserRole.SUPER_USER), is(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "caseworker-sscs,caseworker-sscs-superuser",
        "caseworker-sscs-superuser"
    })
    public void userWithSuperUserRoles_shouldReturnTrue(String userWithSuperUserRole) {
        userDetails.getRoles().addAll(asList(userWithSuperUserRole.split(",")));
        assertThat(userDetails.hasRole(UserRole.SUPER_USER), is(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "caseworker-sscs,caseworker-sscs-panelmember",
        "caseworker-sscs,caseworker-sscs-registrar",
        "caseworker-sscs,caseworker-sscs-judge"
    })
    public void userWithNoDwpRoles_shouldReturnFalse(String roles) {
        userDetails.getRoles().addAll(asList(roles.split(",")));
        assertThat(userDetails.hasRole(UserRole.DWP), is(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "caseworker-sscs,caseworker-sscs-dwpresponsewriter",
        "caseworker-sscs-dwpresponsewriter"
    })
    public void userWithDwpRoles_shouldReturnTrue(String userWithSuperUserRole) {
        userDetails.getRoles().addAll(asList(userWithSuperUserRole.split(",")));
        assertThat(userDetails.hasRole(UserRole.DWP), is(true));
    }

    @Test
    public void userWithRoles_shouldHandleNullRoles() {
        UserDetails userDetails = new UserDetails("id", "email", null);
        assertThat(userDetails.hasRole(UserRole.TCW), is(false));
        assertThat(userDetails.hasRole(UserRole.DWP), is(false));
    }
}

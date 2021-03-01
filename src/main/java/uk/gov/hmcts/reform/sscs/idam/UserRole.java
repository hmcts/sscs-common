package uk.gov.hmcts.reform.sscs.idam;

public enum UserRole {
    TCW("caseworker-sscs-registrar"),
    CTSC_CLERK("caseworker-sscs-clerk"),
    DWP("caseworker-sscs-dwpresponsewriter"),
    JUDGE("caseworker-sscs-judge"),
    SUPER_USER("caseworker-sscs-superuser"),
    CITIZEN("citizen");

    private String value;

    UserRole(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}

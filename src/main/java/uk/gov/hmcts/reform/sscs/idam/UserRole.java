package uk.gov.hmcts.reform.sscs.idam;

public enum UserRole {
    TCW("caseworker-sscs-registrar", "Registrar"),
    CTSC_CLERK("caseworker-sscs-clerk", "Clerk"),
    DWP("caseworker-sscs-dwpresponsewriter", "FTA"),
    JUDGE("caseworker-sscs-judge", "Judge"),
    SUPER_USER("caseworker-sscs-superuser", "Super User"),
    SYSTEM_USER("caseworker-sscs-systemupdate", "System User"),
    CITIZEN("citizen", "citizen");

    private final String value;
    private final String label;

    UserRole(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}

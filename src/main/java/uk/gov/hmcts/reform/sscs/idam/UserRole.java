package uk.gov.hmcts.reform.sscs.idam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    SUPER_USER("caseworker-sscs-superuser", "Super User"),
    SYSTEM_USER("caseworker-sscs-systemupdate", "System User"),
    FEE_PAID_JUDGE("caseworker-sscs-judge-feepaid", "Judge"),
    JUDGE("caseworker-sscs-judge", "Judge"),
    TCW("caseworker-sscs-registrar", "Registrar"),
    CTSC_CLERK("caseworker-sscs-clerk", "Clerk"),
    DWP("caseworker-sscs-dwpresponsewriter", "FTA"),
    CITIZEN("citizen", "Citizen");

    private final String value;
    private final String label;
}

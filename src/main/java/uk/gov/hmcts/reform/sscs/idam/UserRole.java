package uk.gov.hmcts.reform.sscs.idam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    SUPER_USER("caseworker-sscs-superuser", "Super User"),
    SYSTEM_USER("caseworker-sscs-systemupdate", "System User"),
    JUDGE("caseworker-sscs-judge", "Judge"),
    SALARIED_JUDGE("caseworker-sscs-judge-salaried", "Judge"),
    TCW("caseworker-sscs-registrar", "Registrar"),
    CTSC_CLERK("caseworker-sscs-clerk", "Clerk"),
    DWP("caseworker-sscs-dwpresponsewriter", "FTA"),
    HMRC("caseworker-sscs-hmrcresponsewriter", "FTA"),
    IBCA("caseworker-sscs-ibcaresponsewriter", "FTA"),
    CITIZEN("citizen", "Citizen");

    private final String value;
    private final String label;
}

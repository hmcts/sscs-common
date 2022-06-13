package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityRoleCode {

    APPELLANT("APEL", "Applicant", "Appellant", ""),
    APPOINTEE("APIN", "Appointee", "Appointee", ""),
    JOINT_PARTY("JOPA", "Applicant", "Joint Party", ""),
    OTHER_PARTY("OTPA", "Respondent", "Other Party", ""),
    RESPONDENT("RESP", "Respondent", "Respondent", ""),
    WELFARE_REPRESENTATIVE("WERP", "Representative", "Welfare Representative", ""),
    LEGAL_REPRESENTATIVE("LGRP", "Representative", "Legal Representative", ""),
    BARRISTER("BARR", "Representative", "Barrister", ""),
    INTERPRETER("INTP", "Interpreter", "Interpreter", ""),
    REPRESENTATIVE("RPTT", "Representative", "Barrister", ""),
    SUPPORT("SUPP", "Support", "Support", ""),
    APPLICANT("APPL", "Applicant", "Applicant", "");

    private final String hmcReference;
    private final String parentRole;
    private final String valueEn;
    private final String valueCy;

}

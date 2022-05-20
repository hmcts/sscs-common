package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityRoleCode {

    APPELLANT("BBA3-appellant", "Applicant", "Appellant", ""),
    APPOINTEE("BBA3-appointee", "Appointee", "Appointee", ""),
    JOINT_PARTY("BBA3-jointParty", "Applicant", "Joint Party", ""),
    OTHER_PARTY("BBA3-otherParty", "Respondent", "Other Party", ""),
    RESPONDENT("BBA3-respondent", "Respondent", "Respondent", ""),
    WELFARE_REPRESENTATIVE("BBA3-welfareRepresentative", "Representative", "Welfare Representative", ""),
    LEGAL_REPRESENTATIVE("BBA3-legalRepresentative", "Representative", "Legal Representative", ""),
    BARRISTER("BBA3-barrister", "Representative", "Barrister", ""),
    INTERPRETER("BBA3-interpreter", "Interpreter", "Interpreter", ""),
    REPRESENTATIVE("BBA3-Representative", "Representative", "Barrister", "");

    private final String hmcReference;
    private final String parentRole;
    private final String valueEn;
    private final String valueCy;

}

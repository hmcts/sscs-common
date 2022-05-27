package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityRoleCode {

    APPELLANT("BBA3-a", "Applicant", "Appellant", ""),
    APPOINTEE("BBA3-b", "Appointee", "Appointee", ""),
    JOINT_PARTY("BBA3-c", "Applicant", "Joint Party", ""),
    OTHER_PARTY("BBA3-d", "Respondent", "Other Party", ""),
    RESPONDENT("BBA3-e", "Respondent", "Respondent", ""),
    WELFARE_REPRESENTATIVE("BBA3-f", "Representative", "Welfare Representative", ""),
    LEGAL_REPRESENTATIVE("BBA3-g", "Representative", "Legal Representative", ""),
    BARRISTER("BBA3-h", "Representative", "Barrister", ""),
    INTERPRETER("BBA3-i", "Interpreter", "Interpreter", ""),
    REPRESENTATIVE("BBA3-j", "Representative", "Barrister", "");

    private final String hmcReference;
    private final String parentRole;
    private final String valueEn;
    private final String valueCy;

}
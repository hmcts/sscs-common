package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityRoleCode {
    APPELLANT("BBA3-appellant", "Appellant"),
    APPOINTEE("BBA3-appointee", "Appointee"),
    JOINT_PARTY("BBA3-jointParty", "Joint Party"),
    OTHER_PARTY("BBA3-otherParty", "Other Party"),
    RESPONDENT("BBA3-respondent", "Respondent"),
    WELFARE_REPRESENTATIVE("BBA3-welfareRepresentative", "Welfare Representative"),
    LEGAL_REPRESENTATIVE("BBA3-legalRepresentative", "Legal Representative"),
    BARRISTER("BBA3-barrister", "Barrister"),
    INTERPRETER("BBA3-interpreter", "Interpreter");

    private final String key;
    private final String value;

    public static EntityRoleCode getEntityRoleCode(String value) {
        for (EntityRoleCode erc : EntityRoleCode.values()) {
            if (erc.getValue().equals(value)) {
                return erc;
            }
        }
        return null;
    }
}

package uk.gov.hmcts.reform.sscs.model;

import static uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType.*;

import lombok.Getter;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;

@Getter
public enum PartyItemList {
    APPELLANT("appellant", "Appellant (or Appointee)", "Appellant evidence", APPELLANT_EVIDENCE),
    REPRESENTATIVE("representative", "Representative", "Representative evidence", REPRESENTATIVE_EVIDENCE),
    DWP("dwp", "DWP", "DWP evidence", DWP_EVIDENCE),
    JOINT_PARTY("jointParty", "Joint party", "Joint party evidence", JOINT_PARTY_EVIDENCE),
    HMCTS("hmcts", "HMCTS", "HMCTS evidence", HMCTS_EVIDENCE);

    private final String code;
    private final String label;
    private final String documentFooter;
    private final DocumentType documentType;

    PartyItemList(String code, String label, String documentFooter, DocumentType documentType) {
        this.code = code;
        this.label = label;
        this.documentFooter = documentFooter;
        this.documentType = documentType;
    }
}
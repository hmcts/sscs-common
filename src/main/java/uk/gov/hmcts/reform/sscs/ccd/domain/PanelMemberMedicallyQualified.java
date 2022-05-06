package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMemberMedicallyQualified {
    // TODO These are based on mock data and subject to change.
    CARDIOLOGIST("001","Cardiologist"),
    CARER("002", "Carer"),
    EYE_SURGEON("003", "Eye Surgeon"),
    GENERAL_PRACTITIONER("004", "General Practitioner");

    private final String reference;
    private final String description;
}

package uk.gov.hmcts.reform.sscs.reference.data.mappings;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PanelMemberSpecialism {
    // Note: these are based on mock data and subject to change.
    CARDIOLOGIST_1("BBA3-MQPM1-001", "Cardiologist"),
    CARER_1("BBA3-MQPM1-002", "Carer"),
    EYE_SURGEON_1("BBA3-MQPM1-003", "Eye Surgeon"),
    GENERAL_PRACTITIONER_1("BBA3-MQPM1-004", "General Practitioner"),
    CARDIOLOGIST_2("BBA3-MQPM2-001", "Cardiologist"),
    CARER_2("BBA3-MQPM2-002", "Carer"),
    EYE_SURGEON_2("BBA3-MQPM2-003", "Eye Surgeon"),
    GENERAL_PRACTITIONER_2("BBA3-MQPM2-004", "General Practitioner");

    private final String key;
    private final String description;
}

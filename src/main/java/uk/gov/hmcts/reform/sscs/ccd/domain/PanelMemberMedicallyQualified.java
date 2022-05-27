package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMemberMedicallyQualified {
    // TODO These are based on mock data, are subject to change and has missing data.
    CARDIOLOGIST("cardiologist","001","Cardiologist"),
    CARER("carer","002","Carer"),
    EYE_SURGEON("eyeSurgeon","003","Eye Surgeon"),
    GENERAL_PRACTITIONER("generalPractitioner","004","General Practitioner"),
    UROLOGIST("urologist","?","Urologist"),
    RHEUMATIC_PHYSICIAN("rheumaticPhysician","?","Rheumatic Physician"),
    RENAL_PHYSICIAN("renalPhysician","?","Renal Physician"),
    PSYCHIATRIST("psychiatrist","?","Psychiatrist"),
    PLASTIC_SURGEON("plasticSurgeon","?","Plastic Surgeon"),
    ORTHOPAEDIC_SURGEON("orthopaedicSurgeon","?","Orthopaedic Surgeon"),
    NEUROLOGIST("neurologist","?","Neurologist"),
    GENERAL_SURGEON("generalSurgeon","?","General Surgeon"),
    GENERAL_PHYSICIAN("generalPhysician","?","General Physician"),
    ENT_SURGEON("entSurgeon","?","ENT Surgeon"),
    DERMATOLOGIST("dermatologist","?","Dermatologist"),
    CHEST_SPECIALIST("chestSpecialist","?","Chest Specialist"),
    CHEST_PHYSICIAN("chestPhysician","?","Chest Physician");

    private final String ccdReference;
    private final String hmcReference;
    private final String description;

    public static PanelMemberMedicallyQualified getPanelMemberMedicallyQualified(String ccdReference) {
        return Arrays.stream(values())
                .filter(member -> member.ccdReference.equalsIgnoreCase(ccdReference))
                .findFirst()
                .orElse(null);
    }
}

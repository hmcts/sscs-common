package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PanelMemberMedicallyQualified {
    CARDIOLOGIST("cardiologist","1","Cardiologist"),
    CARER("carer","2","Carer"),
    EYE_SURGEON("eyeSurgeon","3","Eye Surgeon"),
    GENERAL_PRACTITIONER("generalPractitioner","4","General Practitioner"),
    DERMATOLOGIST("dermatologist","5","Dermatologist"),
    ENT_SURGEON("entSurgeon","6","ENT Surgeon"),
    PSYCHIATRIST("psychiatrist","7","Psychiatrist"),
    GENERAL_PHYSICIAN("generalPhysician","8","General Physician"),
    NEUROLOGIST("neurologist","9","Neurologist"),
    GENERAL_SURGEON("generalSurgeon","10","General Surgeon"),
    ORTHOPAEDIC_SURGEON("orthopaedicSurgeon","11","Orthopaedic Surgeon"),
    PLASTIC_SURGEON("plasticSurgeon","12","Plastic Surgeon"),
    CHEST_PHYSICIAN("chestPhysician","13","Chest Physician"),
    UROLOGIST("urologist","14","Urologist"),
    CHEST_SPECIALIST("chestSpecialist","15","Chest Specialist"),
    PAEDIATRICIAN("paediatrician", "16", "Paediatrician"),
    RHEUMATIC_PHYSICIAN("rheumaticPhysician","17","Rheumatic Physician"),
    RENAL_PHYSICIAN("renalPhysician","18","Renal Physician");

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

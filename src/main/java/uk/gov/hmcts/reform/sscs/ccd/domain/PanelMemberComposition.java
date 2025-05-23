package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanelMemberComposition {
    private static final String FQPM_REF = PanelMemberType.TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef();
    private static final String TRIBUNAL_MEDICAL_MEMBER_REF = PanelMemberType.TRIBUNAL_MEMBER_MEDICAL.toRef();
    private static final String REGIONAL_MEDICAL_MEMBER_REF = PanelMemberType.REGIONAL_MEDICAL_MEMBER.toRef();

    private String panelCompositionJudge;
    private String panelCompositionMemberMedical1;
    private String panelCompositionMemberMedical2;
    private List<String> panelCompositionDisabilityAndFqMember;

    @JsonIgnore
    public boolean isEmpty() {
        return isNull(panelCompositionJudge) &&
                isNull(panelCompositionMemberMedical1) &&
                isNull(panelCompositionMemberMedical2) &&
                ObjectUtils.isEmpty(panelCompositionDisabilityAndFqMember);
    }

    @JsonIgnore
    public boolean hasFqpm() {
        return nonNull(panelCompositionDisabilityAndFqMember)
            && panelCompositionDisabilityAndFqMember.contains(FQPM_REF);
    }

    @JsonIgnore
    public void addFqpm() {
        if (isNull(panelCompositionDisabilityAndFqMember)) {
            panelCompositionDisabilityAndFqMember = new ArrayList<>(List.of(FQPM_REF));
        } else if (!panelCompositionDisabilityAndFqMember.contains(FQPM_REF)) {
            panelCompositionDisabilityAndFqMember.add(FQPM_REF);
        }
    }

    @JsonIgnore
    public void removeFqpm() {
        if (nonNull(panelCompositionDisabilityAndFqMember) && panelCompositionDisabilityAndFqMember.contains(FQPM_REF)) {
            panelCompositionDisabilityAndFqMember.remove(FQPM_REF);
        }
    }

    @JsonIgnore
    public boolean hasMedicalMember() {
        return TRIBUNAL_MEDICAL_MEMBER_REF.equals(panelCompositionMemberMedical1)
            || TRIBUNAL_MEDICAL_MEMBER_REF.equals(panelCompositionMemberMedical2)
            || REGIONAL_MEDICAL_MEMBER_REF.equals(panelCompositionMemberMedical1)
            || REGIONAL_MEDICAL_MEMBER_REF.equals(panelCompositionMemberMedical2);
    }

    @JsonIgnore
    public void clearMedicalMembers() {
        panelCompositionMemberMedical1 = null;
        panelCompositionMemberMedical2 = null;
    }
}

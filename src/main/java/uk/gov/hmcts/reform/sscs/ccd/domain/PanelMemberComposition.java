package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.addIgnoreNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.getPanelMemberType;

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

    protected static final String FQPM_REF = PanelMemberType.TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED.toRef();

    private String districtTribunalJudge;
    private String panelCompositionJudge;
    private String panelCompositionMemberMedical1;
    private String panelCompositionMemberMedical2;
    private List<String> panelCompositionDisabilityAndFqMember = new ArrayList<>();


    public PanelMemberComposition(List<String> johTiers) {
        for (String johTier : johTiers) {
            switch (getPanelMemberType(johTier)) {
                case TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED:
                case TRIBUNAL_MEMBER_DISABILITY:
                    this.panelCompositionDisabilityAndFqMember.add(johTier);
                    break;
                case TRIBUNAL_MEMBER_MEDICAL:
                    if(nonNull(this.panelCompositionMemberMedical1)) {
                        this.panelCompositionMemberMedical2 = johTier;
                    } else {
                        this.panelCompositionMemberMedical1 = johTier;
                    }
                break;
                case REGIONAL_MEDICAL_MEMBER:
                    if (nonNull(this.panelCompositionMemberMedical1)) {
                        this.panelCompositionMemberMedical2 = TRIBUNAL_MEMBER_MEDICAL.toRef();
                    }
                    this.panelCompositionMemberMedical1 = johTier;
                    break;
                case TRIBUNAL_JUDGE:
                case REGIONAL_TRIBUNAL_JUDGE:
                    this.panelCompositionJudge = johTier;
                    break;
                case DISTRICT_TRIBUNAL_JUDGE:
                    this.districtTribunalJudge = johTier;
                    break;
            }
        }
    }

    @JsonIgnore
    public List<String> getJohTiers() {
        List<String> roleTypes = new ArrayList<>();
        addIgnoreNull(roleTypes, this.districtTribunalJudge);
        addIgnoreNull(roleTypes, this.panelCompositionJudge);
        addIgnoreNull(roleTypes, this.panelCompositionMemberMedical1);
        addIgnoreNull(roleTypes, this.panelCompositionMemberMedical2);
        if(nonNull(this.panelCompositionDisabilityAndFqMember)) {
            roleTypes.addAll(this.panelCompositionDisabilityAndFqMember);
        }
        return roleTypes;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return isNull(panelCompositionJudge) &&
                isNull(districtTribunalJudge) &&
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
        if (nonNull(panelCompositionDisabilityAndFqMember)) {
            panelCompositionDisabilityAndFqMember.remove(FQPM_REF);
        }
    }

    @JsonIgnore
    public boolean hasMedicalMember() {
        return nonNull(panelCompositionMemberMedical1) || nonNull(panelCompositionMemberMedical2);
    }

    @JsonIgnore
    public void clearMedicalMembers() {
        panelCompositionMemberMedical1 = null;
        panelCompositionMemberMedical2 = null;
    }
}

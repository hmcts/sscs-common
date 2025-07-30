package uk.gov.hmcts.reform.sscs.reference.data.model;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Slf4j
public class DefaultPanelComposition {

    private String benefitIssueCode;
    private String category;
    private String specialismCount;
    private String fqpm;
    private String medicalMember;
    private List<String> johTiers;

    public DefaultPanelComposition(String issueCode, SscsCaseData caseData) {
        this.benefitIssueCode = caseData.getBenefitCode() + issueCode;
        this.specialismCount = getSpecialismCount(caseData);
        this.fqpm = isYes(caseData.getIsFqpmRequired()) ? caseData.getIsFqpmRequired().getValue().toLowerCase() : null;;
        this.medicalMember = isYes(caseData.getIsMedicalMemberRequired())
                ? caseData.getIsMedicalMemberRequired().getValue().toLowerCase() : null;
        this.johTiers = new ArrayList<>();
    }

    public DefaultPanelComposition() {
        this.johTiers = new ArrayList<>();
    }

    public static String getSpecialismCount(SscsCaseData caseData) {
        return caseData.getSscsIndustrialInjuriesData().getPanelDoctorSpecialism() != null
                ? caseData.getSscsIndustrialInjuriesData().getSecondPanelDoctorSpecialism() != null
                ? "2" : "1" : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultPanelComposition that = (DefaultPanelComposition) o;
        return Objects.equals(benefitIssueCode, that.benefitIssueCode)
            && Objects.equals(specialismCount, that.specialismCount)
            && Objects.equals(fqpm, that.fqpm)
            && Objects.equals(medicalMember, that.medicalMember);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benefitIssueCode, specialismCount, fqpm);
    }

    public boolean containsAnyPanelMembers(List<PanelMemberType> panelMembers) {
        return panelMembers.stream().anyMatch(panelMember -> johTiers.contains(panelMember.getReference()));
    }

}

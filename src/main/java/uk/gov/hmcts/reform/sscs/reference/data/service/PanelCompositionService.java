package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.reference.data.model.DefaultPanelComposition;

@Getter
@Setter
@Slf4j
@Component
public class PanelCompositionService {

    private static final String JSON_DATA_LOCATION = "reference-data/panel-category-map.json";

    private List<DefaultPanelComposition> defaultPanelCompositions;

    public PanelCompositionService() {
        defaultPanelCompositions = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
    }

    public List<String> getRoleTypes(SscsCaseData caseData) {
        if (nonNull(caseData.getPanelMemberComposition()) && !caseData.getPanelMemberComposition().isEmpty()) {
            return caseData.getPanelMemberComposition().getJohTiers();
        } else {
            return getDefaultJohTiers(caseData);
        }
    }

    public List<String> getDefaultJohTiers(SscsCaseData caseData) {
        return defaultPanelCompositions.stream()
                .filter(new DefaultPanelComposition(caseData)::equals)
                .findFirst().orElse(new DefaultPanelComposition())
                .getJohTiers();
    }

    public boolean isBenefitIssueCodeValid(String benefitCode, String issueCode) {
        return defaultPanelCompositions.stream().anyMatch(panelComposition -> panelComposition.getBenefitIssueCode().equals(benefitCode + issueCode));
    }
}

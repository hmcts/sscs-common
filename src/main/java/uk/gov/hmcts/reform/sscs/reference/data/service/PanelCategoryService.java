package uk.gov.hmcts.reform.sscs.reference.data.service;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.reference.data.model.BenefitRoleRelationType;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;

@Getter
@Setter
@Slf4j
@Component
public class PanelCategoryService {
    private static final String JSON_DATA_LOCATION = "reference-data/panel-category-map.json";
    private List<PanelCategory> panelCategories;
    @Value("${feature.default-panel-comp.enabled}")
    private boolean defaultPanelCompEnabled;

    public PanelCategoryService() {
        panelCategories = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
    }

    public PanelCategory getPanelCategory(String benefitIssueCode, String specialism, String fqpm) {
        PanelCategory panelCategorySelection = new PanelCategory(benefitIssueCode, specialism, fqpm);
        return panelCategories.stream()
                .filter(panelCategorySelection::equals)
                .findFirst().orElse(null);
    }

    public List<String> getRoleTypes(SscsCaseData caseData) {
        if (defaultPanelCompEnabled) {
            String benefitIssueCode = caseData.getBenefitCode() + caseData.getIssueCode();
            String specialismCount = caseData.getSscsIndustrialInjuriesData().getPanelDoctorSpecialism() != null
                    ? caseData.getSscsIndustrialInjuriesData().getSecondPanelDoctorSpecialism() != null
                    ? "2" : "1" : null;
            String isFqpm =  isYes(caseData.getIsFqpmRequired()) ? "true" : null;
            PanelCategory panelComp = getPanelCategory(benefitIssueCode, specialismCount, isFqpm);
            log.info("Panel Category Map for Case {}: {}", caseData.getCcdCaseId(), panelComp);
            return panelComp != null ? panelComp.getJohTiers() : Collections.emptyList();
        } else {
            return BenefitRoleRelationType.findRoleTypesByBenefitCode(caseData.getBenefitCode());
        }
    }
}

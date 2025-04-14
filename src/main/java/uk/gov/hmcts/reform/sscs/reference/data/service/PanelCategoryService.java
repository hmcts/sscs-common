package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;

@Getter
@Setter
@Slf4j
@Component
public class PanelCategoryService {
    private static final String JSON_DATA_LOCATION = "reference-data/panel-category-map.json";
    private List<PanelCategory> panelCategories;

    public PanelCategoryService() {
        panelCategories = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
    }

    public PanelCategory getPanelCategory(String benefitIssueCode, String specialism, String fqpm) {
        PanelCategory panelCategorySelection = new PanelCategory(benefitIssueCode, specialism, fqpm);
        return panelCategories.stream()
                .filter(panelCategorySelection::equals)
                .findFirst().orElse(null);
    }

    public List<String> getRoleTypes(SscsCaseData caseData, boolean savePanelComp) {
        if (caseData.getPanelMemberComposition() != null) {
            return mapPanelMemberCompositionToRoleTypes(caseData.getPanelMemberComposition());
        }
        String benefitIssueCode = caseData.getBenefitCode() + caseData.getIssueCode();
        String specialismCount = caseData.getSscsIndustrialInjuriesData().getPanelDoctorSpecialism() != null
                ? caseData.getSscsIndustrialInjuriesData().getSecondPanelDoctorSpecialism() != null
                ? "2" : "1" : null;
        String isFqpm = isYes(caseData.getIsFqpmRequired()) ? "true" : null;
        PanelCategory panelComp = getPanelCategory(benefitIssueCode, specialismCount, isFqpm);
        if (panelComp != null) {
            if (savePanelComp) {
                log.info("Panel Category Map for Case {}: {}", caseData.getCcdCaseId(), panelComp);
                setPanelMemberComposition(caseData, panelComp.getJohTiers());
            }
            return panelComp.getJohTiers();
        } else {
            return Collections.emptyList();
        }
    }


    public static List<String> mapPanelMemberCompositionToRoleTypes(PanelMemberComposition panelMemberComposition) {
        ArrayList<String> roleTypes = new ArrayList<>();
        if (nonNull(panelMemberComposition.getPanelCompositionJudge())) {
            roleTypes.add(panelMemberComposition.getPanelCompositionJudge());
        }
        if (nonNull(panelMemberComposition.getPanelCompositionMemberMedical1())) {
            roleTypes.add(panelMemberComposition.getPanelCompositionMemberMedical1());
        }
        if (nonNull(panelMemberComposition.getPanelCompositionMemberMedical2())) {
            roleTypes.add(panelMemberComposition.getPanelCompositionMemberMedical2());
        }
        if (nonNull(panelMemberComposition.getPanelCompositionDisabilityAndFqMember())) {
            roleTypes.addAll(panelMemberComposition.getPanelCompositionDisabilityAndFqMember());
        }
        return roleTypes;
    }

    public static void setPanelMemberComposition(SscsCaseData caseData, List<String> johTiers) {
        PanelMemberComposition panelMemberComposition = new PanelMemberComposition();
        panelMemberComposition.setPanelCompositionDisabilityAndFqMember(new ArrayList<>());
        for (String johTier : johTiers) {
            switch (johTier) {
                case "50", "44":
                    panelMemberComposition.getPanelCompositionDisabilityAndFqMember().add(johTier);
                    break;
                case "58", "69":
                    if (panelMemberComposition.getPanelCompositionMemberMedical1() != null) {
                        panelMemberComposition.setPanelCompositionMemberMedical2(johTier);
                    } else {
                        panelMemberComposition.setPanelCompositionMemberMedical1(johTier);
                    }
                    break;
                case "84", "74":
                    panelMemberComposition.setPanelCompositionJudge(johTier);
                    break;
                default:
            }
        }
        caseData.setPanelMemberComposition(panelMemberComposition);
    }
}

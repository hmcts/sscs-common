package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;

@Getter
@Setter
@Slf4j
@Component
public class PanelCategoryService {
    private static final String JSON_DATA_LOCATION = "reference-data/panel-category-map.json";
    private List<PanelCategory> panelCategories;
    private static final String TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED = "50";
    private static final String TRIBUNAL_MEMBER_DISABILITY = "44";
    private static final String TRIBUNAL_MEMBER_MEDICAL = "58";
    private static final String TRIBUNAL_JUDGE = "84";
    private static final String REGIONAL_JUDGE = "74";
    private static final String REGIONAL_MEMBER_MEDICAL = "69";
    private static final String DISTRICT_TRIBUNAL_JUDGE = "90000";

    public PanelCategoryService() {
        panelCategories = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
    }

    public PanelCategory getPanelCategory(String benefitIssueCode, String specialism, String fqpm) {
        PanelCategory panelCategorySelection = new PanelCategory(benefitIssueCode, specialism, fqpm);
        return panelCategories.stream()
                .filter(panelCategorySelection::equals)
                .findFirst().orElse(null);
    }

    public List<String> getRoleTypes(SscsCaseData caseData, boolean savePanelComposition) {
        if (caseData.getPanelMemberComposition() != null) {
            YesNo reservedToDistrictTribunalJudge = NO;
            if (caseData.getSchedulingAndListingFields().getReserveTo() != null
                    && caseData.getSchedulingAndListingFields().getReserveTo().getReservedDistrictTribunalJudge() != null) {
                reservedToDistrictTribunalJudge = caseData.getSchedulingAndListingFields()
                        .getReserveTo().getReservedDistrictTribunalJudge();
            }
            return mapPanelMemberCompositionToRoleTypes(caseData.getPanelMemberComposition(), reservedToDistrictTribunalJudge);
        }
        String benefitIssueCode = caseData.getBenefitCode() + caseData.getIssueCode();
        String specialismCount = caseData.getSscsIndustrialInjuriesData().getPanelDoctorSpecialism() != null
                ? caseData.getSscsIndustrialInjuriesData().getSecondPanelDoctorSpecialism() != null
                ? "2" : "1" : null;
        String isFqpm = isYes(caseData.getIsFqpmRequired()) ? "true" : null;
        PanelCategory panelComp = getPanelCategory(benefitIssueCode, specialismCount, isFqpm);
        if (panelComp != null && !CollectionUtils.isEmpty(panelComp.getJohTiers())) {
            if (savePanelComposition) {
                log.info("Panel Category Map for Case {}: {}", caseData.getCcdCaseId(), panelComp);
                setPanelMemberComposition(caseData, panelComp.getJohTiers());
            }
            return panelComp.getJohTiers();
        } else {
            return Collections.emptyList();
        }
    }


    public static List<String> mapPanelMemberCompositionToRoleTypes(PanelMemberComposition panelMemberComposition, YesNo reservedToDistrictTribunalJudge) {
        ArrayList<String> roleTypes = new ArrayList<>();
        if (reservedToDistrictTribunalJudge == YES) {
            roleTypes.add(DISTRICT_TRIBUNAL_JUDGE);
        } else if (nonNull(panelMemberComposition.getPanelCompositionJudge())) {
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
                case TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED, TRIBUNAL_MEMBER_DISABILITY:
                    panelMemberComposition.getPanelCompositionDisabilityAndFqMember().add(johTier);
                    break;
                case TRIBUNAL_MEMBER_MEDICAL, REGIONAL_MEMBER_MEDICAL:
                    if (panelMemberComposition.getPanelCompositionMemberMedical1() != null) {
                        panelMemberComposition.setPanelCompositionMemberMedical2(johTier);
                    } else {
                        panelMemberComposition.setPanelCompositionMemberMedical1(johTier);
                    }
                    break;
                case TRIBUNAL_JUDGE, REGIONAL_JUDGE:
                    panelMemberComposition.setPanelCompositionJudge(johTier);
                    break;
                default:
            }
        }
        caseData.setPanelMemberComposition(panelMemberComposition);
    }
}

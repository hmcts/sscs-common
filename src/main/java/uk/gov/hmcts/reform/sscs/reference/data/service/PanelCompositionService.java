package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.reference.data.model.DefaultPanelComposition;

@Getter
@Setter
@Slf4j
@Component
public class PanelCompositionService {

    private static final String JSON_DATA_LOCATION = "reference-data/panel-category-map.json";
    static final String TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED = "50";
    static final String TRIBUNAL_MEMBER_DISABILITY = "44";
    static final String TRIBUNAL_MEMBER_MEDICAL = "58";
    static final String TRIBUNAL_JUDGE = "84";
    static final String REGIONAL_JUDGE = "74";
    static final String REGIONAL_MEMBER_MEDICAL = "69";
    static final String DISTRICT_TRIBUNAL_JUDGE = "90000";

    private List<DefaultPanelComposition> defaultPanelCompositions;

    public PanelCompositionService() {
        defaultPanelCompositions = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
    }

    public List<String> getRoleTypes(SscsCaseData caseData) {
        if (nonNull(caseData.getPanelMemberComposition())) {
            return getRoleTypesFromPanelComposition(caseData.getPanelMemberComposition());
        } else {
            DefaultPanelComposition defaultPanelComposition = getDefaultPanelComposition(caseData);
            return nonNull(defaultPanelComposition) && !isEmpty(defaultPanelComposition.getJohTiers())
                    ? defaultPanelComposition.getJohTiers() : List.of();
        }
    }

    public DefaultPanelComposition getDefaultPanelComposition(SscsCaseData caseData) {
        String benefitIssueCode = caseData.getBenefitCode() + caseData.getIssueCode();
        String specialismCount = caseData.getSscsIndustrialInjuriesData().getPanelDoctorSpecialism() != null
                ? caseData.getSscsIndustrialInjuriesData().getSecondPanelDoctorSpecialism() != null
                ? "2" : "1" : null;
        String isFqpm = caseData.getIsFqpmRequired().getValue().toLowerCase();
        return defaultPanelCompositions.stream()
                .filter(new DefaultPanelComposition(benefitIssueCode, specialismCount, isFqpm)::equals)
                .findFirst().orElse(null);
    }

    public static List<String> getRoleTypesFromPanelComposition(PanelMemberComposition panelMemberComposition) {
        List<String> roleTypes = new ArrayList<>();
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

    public PanelMemberComposition getPanelCompositionFromRoleTypes(List<String> johTiers) {
        PanelMemberComposition panelMemberComposition = new PanelMemberComposition();
        panelMemberComposition.setPanelCompositionDisabilityAndFqMember(new ArrayList<>());
        for (String johTier : johTiers) {
            switch (johTier) {
                case TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED:
                case TRIBUNAL_MEMBER_DISABILITY:
                    panelMemberComposition.getPanelCompositionDisabilityAndFqMember().add(johTier);
                    break;
                case TRIBUNAL_MEMBER_MEDICAL:
                case REGIONAL_MEMBER_MEDICAL:
                    if (isNull(panelMemberComposition.getPanelCompositionMemberMedical1())) {
                        panelMemberComposition.setPanelCompositionMemberMedical1(johTier);
                    } else {
                        panelMemberComposition.setPanelCompositionMemberMedical2(johTier);
                    }
                    break;
                case TRIBUNAL_JUDGE:
                case REGIONAL_JUDGE:
                    panelMemberComposition.setPanelCompositionJudge(johTier);
                    break;
            }
        }
        return panelMemberComposition;
    }
}

package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Collections.frequency;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.addIgnoreNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.DISTRICT_TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.REGIONAL_MEDICAL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.getPanelMemberType;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
import uk.gov.hmcts.reform.sscs.ccd.domain.ReserveTo;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;
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
        if (nonNull(caseData.getPanelMemberComposition()) &&
                (!caseData.getPanelMemberComposition().isEmpty() || isDtjSelected(caseData))) {
            updatePanelCompositionFromSpecialismCount(caseData);
            return getJohTiersFromPanelComposition(caseData.getPanelMemberComposition(), caseData);
        } else {
            DefaultPanelComposition defaultPanelComposition = getDefaultPanelComposition(caseData);
            return nonNull(defaultPanelComposition) && !isEmpty(defaultPanelComposition.getJohTiers())
                    ? defaultPanelComposition.getJohTiers() : new ArrayList<>();
        }
    }

    private DefaultPanelComposition getDefaultPanelComposition(SscsCaseData caseData) {
        String benefitIssueCode = caseData.getBenefitCode() + caseData.getIssueCode();
        String specialismCount = getSpecialismCount(caseData);
        String isFqpm =
                isYes(caseData.getIsFqpmRequired()) ? caseData.getIsFqpmRequired().getValue().toLowerCase() : null;
        String isMedicalMember = isYes(caseData.getIsMedicalMemberRequired())
                ? caseData.getIsMedicalMemberRequired().getValue().toLowerCase() : null;
        return defaultPanelCompositions.stream()
                .filter(new DefaultPanelComposition(benefitIssueCode, specialismCount, isFqpm, isMedicalMember)::equals)
                .findFirst().orElse(null);
    }

    private static List<String> getJohTiersFromPanelComposition(PanelMemberComposition panelMemberComposition, SscsCaseData caseData) {
        List<String> roleTypes = new ArrayList<>();
        ReserveTo reserveTo = caseData.getSchedulingAndListingFields().getReserveTo();
        if (reserveTo != null && YesNo.YES.equals(reserveTo.getReservedDistrictTribunalJudge())) {
            roleTypes.add(DISTRICT_TRIBUNAL_JUDGE.toRef());
        } else {
            addIgnoreNull(roleTypes, panelMemberComposition.getPanelCompositionJudge());
        }
        addIgnoreNull(roleTypes, panelMemberComposition.getPanelCompositionMemberMedical1());
        addIgnoreNull(roleTypes, panelMemberComposition.getPanelCompositionMemberMedical2());

        if(nonNull(panelMemberComposition.getPanelCompositionDisabilityAndFqMember())) {
            roleTypes.addAll(panelMemberComposition.getPanelCompositionDisabilityAndFqMember());
        }
        return roleTypes;
    }

    public PanelMemberComposition createPanelCompositionFromJohTiers(List<String> johTiers) {
        PanelMemberComposition panelMemberComposition = new PanelMemberComposition();
        panelMemberComposition.setPanelCompositionDisabilityAndFqMember(new ArrayList<>());
        for (String johTier : johTiers) {
            switch (getPanelMemberType(johTier)) {
                case TRIBUNAL_MEMBER_FINANCIALLY_QUALIFIED:
                case TRIBUNAL_MEMBER_DISABILITY:
                    panelMemberComposition.getPanelCompositionDisabilityAndFqMember().add(johTier);
                    break;
                case TRIBUNAL_MEMBER_MEDICAL:
                    if((frequency(johTiers, TRIBUNAL_MEMBER_MEDICAL.toRef()) > 1
                            && nonNull(panelMemberComposition.getPanelCompositionMemberMedical1()))
                            || johTiers.contains(REGIONAL_MEDICAL_MEMBER.toRef())) {
                        panelMemberComposition.setPanelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef());
                    } else {
                        panelMemberComposition.setPanelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef());
                    }
                    break;
                case REGIONAL_MEDICAL_MEMBER:
                    panelMemberComposition.setPanelCompositionMemberMedical1(johTier);
                    break;
                case TRIBUNAL_JUDGE:
                case REGIONAL_TRIBUNAL_JUDGE:
                case DISTRICT_TRIBUNAL_JUDGE:
                    panelMemberComposition.setPanelCompositionJudge(johTier);
                    break;
            }
        }
        return panelMemberComposition;
    }

    private boolean isDtjSelected(SscsCaseData caseData) {
        ReserveTo reserveTo = caseData.getSchedulingAndListingFields().getReserveTo();
        return nonNull(reserveTo) && YesNo.isYes(reserveTo.getReservedDistrictTribunalJudge());
    }

    private void updatePanelCompositionFromSpecialismCount(SscsCaseData caseData) {
        String specialismCount = getSpecialismCount(caseData);

        if ("2".equals(specialismCount)) {
            if (isNull(caseData.getPanelMemberComposition().getPanelCompositionMemberMedical1())) {
                caseData.getPanelMemberComposition().setPanelCompositionMemberMedical1(TRIBUNAL_MEMBER_MEDICAL.toRef());
            }
            caseData.getPanelMemberComposition().setPanelCompositionMemberMedical2(TRIBUNAL_MEMBER_MEDICAL.toRef());
        } else {
            caseData.getPanelMemberComposition().setPanelCompositionMemberMedical2(null);
        }
    }

    private String getSpecialismCount(SscsCaseData caseData) {
        return caseData.getSscsIndustrialInjuriesData().getPanelDoctorSpecialism() != null
                ? caseData.getSscsIndustrialInjuriesData().getSecondPanelDoctorSpecialism() != null
                ? "2" : "1" : null;
    }
}

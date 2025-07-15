package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Collections.frequency;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.addIgnoreNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.DISTRICT_TRIBUNAL_JUDGE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.REGIONAL_MEDICAL_MEMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.TRIBUNAL_MEMBER_MEDICAL;
import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberType.getPanelMemberType;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
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
            List<String> johTiersFromExistingPanelComposition = getJohTiersFromPanelComposition(caseData.getPanelMemberComposition(), caseData);
            log.info("Existing Panel Composition for case id: {} has joh tiers: {} ",
                    caseData.getCcdCaseId(), johTiersFromExistingPanelComposition);
            return johTiersFromExistingPanelComposition;
        } else {
            DefaultPanelComposition defaultPanelComposition = getDefaultPanelComposition(caseData);
            log.info("Default Panel Composition for case id {} has been calculated as : {}",
                    caseData.getCcdCaseId(), defaultPanelComposition);
            return nonNull(defaultPanelComposition) && isNotEmpty(defaultPanelComposition.getJohTiers())
                    ? defaultPanelComposition.getJohTiers() : new ArrayList<>();
        }
    }
  
    public PanelMemberComposition createPanelComposition(SscsCaseData caseData) {
        DefaultPanelComposition defaultPanelComposition = getDefaultPanelComposition(caseData);
        List<String> defaultJohTiers =
                nonNull(defaultPanelComposition) && isNotEmpty(defaultPanelComposition.getJohTiers())
                        ? defaultPanelComposition.getJohTiers() : new ArrayList<>();
        return createPanelCompositionFromJohTiers(defaultJohTiers);
    }

    public DefaultPanelComposition getDefaultPanelComposition(SscsCaseData caseData) {
        String benefitIssueCode = caseData.getBenefitCode() + caseData.getIssueCode();
        String specialismCount = getSpecialismCount(caseData);
        String isFqpm =
                isYes(caseData.getIsFqpmRequired()) ? caseData.getIsFqpmRequired().getValue().toLowerCase() : null;
        String isMedicalMember = isYes(caseData.getIsMedicalMemberRequired())
                ? caseData.getIsMedicalMemberRequired().getValue().toLowerCase() : null;
        if (List.of(Issue.UM.toString(), Issue.US.toString()).contains(caseData.getIssueCode())) {
            log.info("Using Universal Credit default panel composition calculator for case {}", caseData.getCcdCaseId());
            return getUniversalCreditDefaultPanelComposition(caseData, benefitIssueCode, specialismCount, isFqpm, isMedicalMember);
        } else {
            log.info("Using default panel composition calculator for case {}", caseData.getCcdCaseId());
            return defaultPanelCompositions.stream()
                    .filter(new DefaultPanelComposition(benefitIssueCode, specialismCount, isFqpm, isMedicalMember)::equals)
                    .findFirst().orElse(null);
        }
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
        return nonNull(reserveTo) && isYes(reserveTo.getReservedDistrictTribunalJudge());
    }

    private DefaultPanelComposition getUniversalCreditDefaultPanelComposition(SscsCaseData caseData, String benefitIssueCode, String specialismCount, String isFqpm, String isMedicalMember) {
        List<String> elementsDisputed = caseData.getAllElementsDisputed();
        List<Issue> issues = getIssues(elementsDisputed);
        DefaultPanelComposition defaultPanelComposition = new DefaultPanelComposition(benefitIssueCode, specialismCount, isFqpm, isMedicalMember);
        Set<String> johTiersSet = new HashSet<>();
        Set<String> sessionCategorySet = new HashSet<>();
        for (Issue issue : issues ) {
            String individualUcBenefitIssueCode = caseData.getBenefitCode() + issue.toString();
            DefaultPanelComposition issueCodePanelComposition =  defaultPanelCompositions.stream()
                    .filter(new DefaultPanelComposition(individualUcBenefitIssueCode, specialismCount, isFqpm, isMedicalMember)::equals)
                    .findFirst().orElse(null);
            if (nonNull(issueCodePanelComposition)) {
                johTiersSet.addAll(issueCodePanelComposition.getJohTiers());
                sessionCategorySet.add(issueCodePanelComposition.getCategory());
                if (issue.equals(Issue.SG) || issue.equals(Issue.WC)) {
                    defaultPanelComposition.setCategory(issueCodePanelComposition.getCategory());
                }
            }
        }
        defaultPanelComposition.setJohTiers(new ArrayList<>(johTiersSet));
        if (isNull(defaultPanelComposition.getCategory())) {
            defaultPanelComposition.setCategory(sessionCategorySet.stream().findAny().get());
        }
        return defaultPanelComposition;
    }

    public boolean isBenefitIssueCodeValid(String benefitCode, String issueCode) {
        return defaultPanelCompositions.stream().anyMatch(panelComposition -> panelComposition.getBenefitIssueCode().equals(benefitCode + issueCode));
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

    public static List<Issue> getIssues(List<String> elements) {
        return elements.stream()
                .map(Issue::getIssue)
                .collect(Collectors.toList());
    }
}

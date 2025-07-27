package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;
import static uk.gov.hmcts.reform.sscs.reference.data.model.DefaultPanelComposition.getSpecialismCount;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.ccd.domain.PanelMemberComposition;
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
        List<String> johTiers;
        if (nonNull(caseData.getPanelMemberComposition()) && !caseData.getPanelMemberComposition().isEmpty()) {
            johTiers = caseData.getPanelMemberComposition().getJohTiers();
            log.info("Existing Panel Composition for case id: {} has JOH tiers: {}", caseData.getCcdCaseId(), johTiers);
        } else {
            johTiers = getDefaultPanelComposition(caseData).getJohTiers();
            log.info("Default JOH tiers for case id {} have been calculated as: {}", caseData.getCcdCaseId(), johTiers);
        }
        return johTiers;
    }

    public DefaultPanelComposition getDefaultPanelComposition(SscsCaseData caseData) {
        if (List.of(Issue.UM.toString(), Issue.US.toString()).contains(caseData.getIssueCode())) {
            log.info("Using Universal Credit default panel composition calculator for case {}",
                    caseData.getCcdCaseId());
            return getUcDefaultPanelComposition(caseData);
        } else {
            log.info("Using default panel composition calculator for case {}", caseData.getCcdCaseId());
            return findDefaultPanelCompForIssueCode(caseData.getIssueCode(), caseData);
        }
    }

    private DefaultPanelComposition getUcDefaultPanelComposition(SscsCaseData caseData) {
        var defaultPanelComposition = new DefaultPanelComposition(caseData.getIssueCode(), caseData);
        if (caseData.getIssueCodesForAllElementsDisputed().isEmpty()) {
            log.info("Case {} has no elements disputed", caseData.getCcdCaseId());
        }
        Set<String> allElementsJohTiers = new HashSet<>();
        caseData.getIssueCodesForAllElementsDisputed().forEach(issueCode -> {
            DefaultPanelComposition ucElementPanelComposition =  findDefaultPanelCompForIssueCode(issueCode, caseData);
            allElementsJohTiers.addAll(ucElementPanelComposition.getJohTiers());
            if (Issue.SG.toString().equals(issueCode) || Issue.WC.toString().equals(issueCode)
                    || isNull(defaultPanelComposition.getCategory())) {
                defaultPanelComposition.setCategory(ucElementPanelComposition.getCategory());
            }
        });
        defaultPanelComposition.setJohTiers(new ArrayList<>(allElementsJohTiers));
        return defaultPanelComposition;
    }

    private DefaultPanelComposition findDefaultPanelCompForIssueCode(String issueCode, SscsCaseData caseData) {
        return defaultPanelCompositions.stream()
                .filter(new DefaultPanelComposition(issueCode, caseData)::equals)
                .findFirst().orElse(new DefaultPanelComposition());
    }

    public PanelMemberComposition resetPanelCompositionIfStale(SscsCaseData caseData, SscsCaseData caseDataBefore) {
        boolean hasBenefitCodeChanged = !Objects.equals(caseData.getBenefitCode(), caseDataBefore.getBenefitCode());
        boolean hasIssueCodeChanged = !Objects.equals(caseData.getIssueCode(), caseDataBefore.getIssueCode());
        boolean hasSpecialismCountChanged =
                !Objects.equals(getSpecialismCount(caseData), getSpecialismCount(caseDataBefore));
        return hasBenefitCodeChanged || hasIssueCodeChanged || hasSpecialismCountChanged
                ? new PanelMemberComposition() : caseData.getPanelMemberComposition();
    }

    public PanelMemberComposition resetPanelCompIfElementsChanged(SscsCaseData caseData, SscsCaseData caseDataBefore) {
        return Objects.equals(caseData.getIssueCodesForAllElementsDisputed(),
                caseDataBefore.getIssueCodesForAllElementsDisputed())
                ? caseData.getPanelMemberComposition() : new PanelMemberComposition();
    }

    public boolean isBenefitIssueCodeValid(String benefitCode, String issueCode) {
        return defaultPanelCompositions.stream()
                .anyMatch(panelComposition ->
                        panelComposition.getBenefitIssueCode().equals(benefitCode + issueCode));
    }
}

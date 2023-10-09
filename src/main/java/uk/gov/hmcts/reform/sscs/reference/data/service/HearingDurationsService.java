package uk.gov.hmcts.reform.sscs.reference.data.service;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode.UC;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.US;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.generateHashMap;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;
import static uk.gov.hmcts.reform.sscs.utility.HearingChannelUtil.isInterpreterRequired;
import static uk.gov.hmcts.reform.sscs.utility.HearingChannelUtil.isPaperCase;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingDuration;

@Getter
@Setter
@Component
public class HearingDurationsService {
    private static final String JSON_DATA_LOCATION = "reference-data/hearing-durations.json";
    private static final int MULTIPLE_ISSUES_EXTRA_TIME = 15;
    public static final int DURATION_DEFAULT = 60;

    private List<HearingDuration> hearingDurations;
    private Map<HearingDuration, HearingDuration> hearingDurationsHashMap;

    public HearingDurationsService() {
        hearingDurations = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        hearingDurationsHashMap = generateHashMap(hearingDurations);
    }

    public HearingDuration getHearingDuration(String benefitCode, String issueCode) {
        return getHearingDuration(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode));
    }

    public HearingDuration getHearingDuration(BenefitCode benefitCode, Issue issue) {
        return hearingDurationsHashMap.get(new HearingDuration(benefitCode, issue));
    }

    public Integer addExtraTimeIfNeeded(Integer initialDuration, BenefitCode benefitCode, Issue issue,
                                        List<String> elements) {
        if (isNull(initialDuration) || isEmpty(elements)) {
            return initialDuration;
        }

        if (isUniversalCreditAndSingleOrMultipleIssues(benefitCode, issue)
                && isIssueWorkCapabilityAssessmentOrIsSupportGroup(getIssues(elements))) {
            return initialDuration + MULTIPLE_ISSUES_EXTRA_TIME;
        }

        return initialDuration;
    }

    public Integer getHearingDurationBenefitIssueCodes(SscsCaseData caseData) {
        HearingDuration hearingDuration = getHearingDuration(caseData.getBenefitCode(), caseData.getIssueCode());

        if (isNull(hearingDuration)) {
            return null;
        }

        if (isYes(caseData.getAppeal().getHearingOptions().getWantsToAttend())) {
            Integer duration = isInterpreterRequired(caseData)
                    ? hearingDuration.getDurationInterpreter()
                    : hearingDuration.getDurationFaceToFace();
            return addExtraTimeIfNeeded(
                    duration,
                    hearingDuration.getBenefitCode(),
                    hearingDuration.getIssue(),
                    getElementsDisputed(caseData));
        } else if (isPaperCase(caseData)) {
            return hearingDuration.getDurationPaper();
        }

        return DURATION_DEFAULT;
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    public List<String> getElementsDisputed(SscsCaseData caseData) {
        List<ElementDisputed> elementDisputed = new ArrayList<>();

        List<List<ElementDisputed>> elementsToCheck = new ArrayList<>();
        elementsToCheck.add(caseData.getElementsDisputedGeneral());
        elementsToCheck.add(caseData.getElementsDisputedSanctions());
        elementsToCheck.add(caseData.getElementsDisputedOverpayment());
        elementsToCheck.add(caseData.getElementsDisputedHousing());
        elementsToCheck.add(caseData.getElementsDisputedChildCare());
        elementsToCheck.add(caseData.getElementsDisputedCare());
        elementsToCheck.add(caseData.getElementsDisputedChildElement());
        elementsToCheck.add(caseData.getElementsDisputedChildDisabled());
        elementsToCheck.add(caseData.getElementsDisputedLimitedWork());

        elementsToCheck.forEach((List<ElementDisputed> list) -> {
            if (isNotEmpty(list)) {
                elementDisputed.addAll(list);
            }
        });

        return elementDisputed.stream()
                .map(ElementDisputed::getValue)
                .map(ElementDisputedDetails::getIssueCode)
                .collect(Collectors.toList());
    }

    public List<Issue> getIssues(List<String> elements) {
        return elements.stream()
                .map(Issue::getIssue)
                .collect(Collectors.toList());
    }

    public boolean isIssueWorkCapabilityAssessmentOrIsSupportGroup(List<Issue> issues) {
        return isIssueWorkCapabilityAssessment(issues)
                || isSupportGroupPlacement(issues);
    }

    public boolean isSupportGroupPlacement(List<Issue> issues) {
        return issues.contains(SG);
    }

    public boolean isIssueWorkCapabilityAssessment(List<Issue> issues) {
        return issues.contains(WC);
    }

    public boolean isUniversalCreditAndSingleOrMultipleIssues(BenefitCode benefitCode, Issue issue) {
        return UC.equals(benefitCode) && isSingleOrMultipleIssues(issue);
    }

    public boolean isSingleOrMultipleIssues(Issue issue) {
        return UM.equals(issue) || US.equals(issue);
    }
}

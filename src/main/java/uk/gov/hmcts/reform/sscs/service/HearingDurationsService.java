package uk.gov.hmcts.reform.sscs.service;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode.UC;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.US;
import static uk.gov.hmcts.reform.sscs.helper.ReferenceDataHelper.generateHashMap;
import static uk.gov.hmcts.reform.sscs.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.model.HearingDuration;

@Getter
@Setter
@Component
public class HearingDurationsService {
    private static final String JSON_DATA_LOCATION = "reference-data/hearing-durations.json";
    private static final int MULTIPLE_ISSUES_EXTRA_TIME = 15;

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
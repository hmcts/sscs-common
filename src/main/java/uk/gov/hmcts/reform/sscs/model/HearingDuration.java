package uk.gov.hmcts.reform.sscs.model;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class HearingDuration extends ReferenceData {

    private BenefitCode benefitCode;
    private Issue issue;
    private Integer durationFaceToFace;
    private Integer durationInterpreter;
    private Integer durationPaper;

    private static final int MULTIPLE_ISSUES_EXTRA_TIME = 15;

    public static final String JSON_DATA_LOCATION = "reference-data/hearing-durations.json";

    private static List<HearingDuration> hearingDurations;
    private static Map<Integer, HearingDuration> hashMap;

    static {
        hearingDurations = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>(){});
        hashMap = generateHashMap(hearingDurations);
    }

    public Integer getDurationFaceToFace(List<String> elementsDisputed) {
        return addExtraTime(durationFaceToFace, elementsDisputed);
    }

    public Integer getDurationInterpreter(List<String> elementsDisputed) {
        return addExtraTime(durationInterpreter, elementsDisputed);
    }

    public static HearingDuration getHearingDuration(String benefitCode, String issueCode) {
        return getHearingDuration(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode));
    }

    public static HearingDuration getHearingDuration(BenefitCode benefitCode, Issue issue) {
        return hashMap.get(getHash(benefitCode, issue));
    }

    public Integer getHash() {
        return getHash(benefitCode, issue);
    }

    private static Integer getHash(BenefitCode benefitCode, Issue issue) {
        return Objects.hash(benefitCode, issue);
    }

    Integer addExtraTime(Integer initialDuration, List<String> elements) {
        if (isNull(initialDuration) || isEmpty(elements)) {
            return initialDuration;
        }

        if (isUniversalCreditAndSingleOrMultipleIssues(benefitCode, issue)) {
            List<Issue> issues = elements.stream()
                    .map(Issue::getIssue)
                    .collect(Collectors.toList());
            if (isIssueWorkCapabilityAssessment(issues) || isSupportGroupPlacement(issues)) {
                return initialDuration + MULTIPLE_ISSUES_EXTRA_TIME;
            }
        }

        return initialDuration;
    }

    boolean isSupportGroupPlacement(List<Issue> issues) {
        return issues.contains(SG);
    }

    boolean isIssueWorkCapabilityAssessment(List<Issue> issues) {
        return issues.contains(WC);
    }

    boolean isUniversalCreditAndSingleOrMultipleIssues(BenefitCode benefitCode, Issue issue) {
        return UC.equals(benefitCode) && isSingleOrMultipleIssues(issue);
    }

    boolean isSingleOrMultipleIssues(Issue issue) {
        return UM.equals(issue) || US.equals(issue);
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum CaseOutcomeMap {
    REFERENCE_REVISED_FAVOUR_APPELLANT("referenceRevisedFavourAppellant", "21"),
    REFERENCE_REVISED_AGAINST_APPELLANT("referenceRevisedAgainstAppellant", "22"),
    DECISION_UPHELD("decisionUpheld", "9"),
    DECISION_REVISED_AGAINST_APPELLANT("decisionRevisedAgainstAppellant", "8"),
    DECISION_RESERVED("decisionReserved", "63"),
    DECISION_IN_FAVOUR_OF_APPELLANT("decisionInFavourOfAppellant", "10"),
    DISABLEMENT_INCREASED_NO_BENEFIT_AWARDED("disablementIncreasedNoBenefitAwarded", "39");

    private final String outcomeKey;
    private final String caseOutcomeCode;

    private static final Map<String, CaseOutcomeMap> LOOKUP_BY_KEY = Arrays.stream(values())
            .collect(Collectors.toMap(CaseOutcomeMap::getOutcomeKey, Function.identity()));

    public static String getCaseOutcomeByOutcome(String outcomeKey) {
        CaseOutcomeMap mapping = LOOKUP_BY_KEY.get(outcomeKey);
        if (isNull(mapping)) {
            throw new IllegalArgumentException("Invalid Outcome: " + outcomeKey);
        }

        return mapping.getCaseOutcomeCode();
    }
}

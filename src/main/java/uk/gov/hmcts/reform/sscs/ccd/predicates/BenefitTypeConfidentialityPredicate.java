package uk.gov.hmcts.reform.sscs.ccd.predicates;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static uk.gov.hmcts.reform.sscs.ccd.domain.SscsType.SSCS2;
import static uk.gov.hmcts.reform.sscs.ccd.domain.SscsType.SSCS5;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;

public class BenefitTypeConfidentialityPredicate {

    public static final BenefitTypeConfidentialityPredicate INSTANCE = new BenefitTypeConfidentialityPredicate();

    private static final Set<String> VALID_CONFIDENTIALITY_BENEFITS = Arrays
        .stream(Benefit.values())
        .filter(benefit -> SSCS2.equals(benefit.getSscsType()) || SSCS5.equals(benefit.getSscsType()))
        .map(Benefit::getShortName)
        .collect(toSet());

    private BenefitTypeConfidentialityPredicate() {
    }

    public static boolean isValidBenefitTypeForConfidentiality(final BenefitType benefitType) {
        return INSTANCE.test(benefitType);
    }

    public static boolean isValidBenefitTypeForConfidentiality(final BenefitType benefitType,
        final List<Benefit> additionalBenefits) {
        return INSTANCE.test(benefitType, additionalBenefits);
    }

    public boolean test(final BenefitType benefitType) {
        return test(benefitType, List.of());
    }

    public boolean test(final BenefitType benefitType, final List<Benefit> additionalBenefits) {
        if (benefitType == null) {
            return false;
        }
        final String benefitCode = benefitType.getCode();
        return VALID_CONFIDENTIALITY_BENEFITS.contains(benefitCode)
            || Optional
            .ofNullable(additionalBenefits)
            .orElse(emptyList())
            .stream()
            .filter(Objects::nonNull)
            .map(Benefit::getShortName)
            .anyMatch(shortName -> shortName.equals(benefitCode));
    }
}

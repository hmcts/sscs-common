package uk.gov.hmcts.reform.sscs.ccd.predicates;

import static java.util.stream.Collectors.toSet;
import static uk.gov.hmcts.reform.sscs.ccd.domain.SscsType.SSCS2;
import static uk.gov.hmcts.reform.sscs.ccd.domain.SscsType.SSCS5;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import uk.gov.hmcts.reform.sscs.ccd.domain.Benefit;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;

public class BenefitTypeConfidentialityPredicate implements Predicate<BenefitType> {

    public static final BenefitTypeConfidentialityPredicate INSTANCE = new BenefitTypeConfidentialityPredicate();

    private static final Set<String> VALID_CONFIDENTIALITY_BENEFITS = Arrays
        .stream(Benefit.values())
        .filter(benefit -> SSCS2.equals(benefit.getSscsType()) || SSCS5.equals(benefit.getSscsType()))
        .map(Benefit::getShortName)
        .collect(toSet());

    private BenefitTypeConfidentialityPredicate() {
    }

    @Override
    public boolean test(final BenefitType benefitType) {
        if (benefitType == null) {
            return false;
        }
        final String benefitCode = benefitType.getCode();
        return VALID_CONFIDENTIALITY_BENEFITS.contains(benefitCode) || Benefit.UC.getShortName().equals(benefitCode);
    }

    public static boolean isValidBenefitTypeForConfidentiality(final BenefitType benefitType) {
        return INSTANCE.test(benefitType);
    }
}

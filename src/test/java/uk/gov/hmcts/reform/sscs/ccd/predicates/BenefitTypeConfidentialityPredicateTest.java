package uk.gov.hmcts.reform.sscs.ccd.predicates;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.CHILD_SUPPORT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.PIP;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.TAX_CREDIT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.UC;

import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;

class BenefitTypeConfidentialityPredicateTest {

    private final Predicate<BenefitType> predicate = BenefitTypeConfidentialityPredicate.INSTANCE;

    @Test
    void test_returnsFalse_whenBenefitTypeIsNull() {
        assertThat(predicate.test(null)).isFalse();
    }

    @Test
    void test_returnsFalse_whenBenefitTypeCodeIsNull() {
        assertThat(predicate.test(BenefitType.builder().build())).isFalse();
    }

    @Test
    void test_returnsTrue_whenBenefitIsSscs2() {
        final BenefitType benefitType = BenefitType.builder().code(CHILD_SUPPORT.getShortName()).build();

        assertThat(predicate.test(benefitType)).isTrue();
    }

    @Test
    void test_returnsTrue_whenBenefitIsSscs5() {
        final BenefitType benefitType = BenefitType.builder().code(TAX_CREDIT.getShortName()).build();

        assertThat(predicate.test(benefitType)).isTrue();
    }

    @Test
    void test_returnsTrue_whenBenefitIsUc() {
        final BenefitType benefitType = BenefitType.builder().code(UC.getShortName()).build();

        assertThat(predicate.test(benefitType)).isTrue();
    }

    @Test
    void test_returnsFalse_whenBenefitIsSscs1() {
        final BenefitType benefitType = BenefitType.builder().code(PIP.getShortName()).build();

        assertThat(predicate.test(benefitType)).isFalse();
    }

    @Test
    void isValidBenefitTypeForConfidentiality_delegatesToTest() {
        final BenefitType valid = BenefitType.builder().code(CHILD_SUPPORT.getShortName()).build();
        final BenefitType invalid = BenefitType.builder().code(PIP.getShortName()).build();

        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(valid)).isTrue();
        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(invalid)).isFalse();
        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(null)).isFalse();
    }
}

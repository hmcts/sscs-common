package uk.gov.hmcts.reform.sscs.ccd.predicates;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.CHILD_SUPPORT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.PIP;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.TAX_CREDIT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.UC;

import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitType;

class BenefitTypeConfidentialityPredicateTest {

    private final BenefitTypeConfidentialityPredicate predicate = BenefitTypeConfidentialityPredicate.INSTANCE;

    @Test
    void test_returnsFalse_whenBenefitTypeIsNull() {
        assertThat(predicate.test(null, List.of())).isFalse();
    }

    @Test
    void test_returnsFalse_whenBenefitTypeCodeIsNull() {
        assertThat(predicate.test(BenefitType.builder().build(), List.of())).isFalse();
    }

    @Test
    void test_returnsTrue_whenBenefitIsSscs2() {
        final BenefitType benefitType = BenefitType.builder().code(CHILD_SUPPORT.getShortName()).build();

        assertThat(predicate.test(benefitType, List.of())).isTrue();
    }

    @Test
    void test_returnsTrue_whenBenefitIsSscs5() {
        final BenefitType benefitType = BenefitType.builder().code(TAX_CREDIT.getShortName()).build();

        assertThat(predicate.test(benefitType, List.of())).isTrue();
    }

    @Test
    void test_returnsTrue_whenBenefitIsInAdditionalBenefitsList() {
        final BenefitType benefitType = BenefitType.builder().code(UC.getShortName()).build();

        assertThat(predicate.test(benefitType, List.of(UC))).isTrue();
    }

    @Test
    void test_returnsFalse_whenBenefitIsNotInAdditionalBenefitsList() {
        final BenefitType benefitType = BenefitType.builder().code(UC.getShortName()).build();

        assertThat(predicate.test(benefitType, List.of())).isFalse();
    }

    @Test
    void test_returnsFalse_whenBenefitIsSscs1() {
        final BenefitType benefitType = BenefitType.builder().code(PIP.getShortName()).build();

        assertThat(predicate.test(benefitType, List.of())).isFalse();
    }

    @Test
    void isValidBenefitTypeForConfidentiality_delegatesToTest() {
        final BenefitType valid = BenefitType.builder().code(CHILD_SUPPORT.getShortName()).build();
        final BenefitType invalid = BenefitType.builder().code(PIP.getShortName()).build();

        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(valid, List.of())).isTrue();
        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(invalid, List.of())).isFalse();
        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(null, List.of())).isFalse();
    }

    @Test
    void test_singleArgOverload_delegatesWithNoAdditionalBenefits() {
        final BenefitType valid = BenefitType.builder().code(CHILD_SUPPORT.getShortName()).build();
        final BenefitType ucOnly = BenefitType.builder().code(UC.getShortName()).build();

        assertThat(predicate.test(valid)).isTrue();
        assertThat(predicate.test(ucOnly)).isFalse();
        assertThat(predicate.test(null)).isFalse();
    }

    @Test
    void isValidBenefitTypeForConfidentiality_singleArgOverload_delegatesWithNoAdditionalBenefits() {
        final BenefitType valid = BenefitType.builder().code(CHILD_SUPPORT.getShortName()).build();
        final BenefitType ucOnly = BenefitType.builder().code(UC.getShortName()).build();

        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(valid)).isTrue();
        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(ucOnly)).isFalse();
        assertThat(BenefitTypeConfidentialityPredicate.isValidBenefitTypeForConfidentiality(null)).isFalse();
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.BEREAVEMENT_BENEFIT;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CaseAccessManagementFieldsTest {

    private CaseAccessManagementFields unitUnderTest;

    @BeforeEach
    public void setup() {
        unitUnderTest = new CaseAccessManagementFields();
    }

    @Test
    public void shouldSetSetCaseNameFields_givenValidCaseName() {
        String testCaseName = "caseName";

        unitUnderTest.setCaseNames(testCaseName);

        assertThat(unitUnderTest.getCaseNameHmctsInternal()).isEqualTo(testCaseName);
        assertThat(unitUnderTest.getCaseNameHmctsRestricted()).isEqualTo(testCaseName);
        assertThat(unitUnderTest.getCaseNamePublic()).isEqualTo(testCaseName);
    }

    @Test
    public void shouldNotSetSetCaseNameFields_givenInvalidCaseName() {
        unitUnderTest.setCaseNames(null);

        assertThat(unitUnderTest.getCaseNameHmctsInternal()).isNull();
        assertThat(unitUnderTest.getCaseNameHmctsRestricted()).isNull();
        assertThat(unitUnderTest.getCaseNamePublic()).isNull();
    }

    @Test
    public void shouldSetCaseAccessCategory_givenValidBenefit() {
        unitUnderTest.setCategories(BEREAVEMENT_BENEFIT);

        assertThat(unitUnderTest.getCaseAccessCategory()).isEqualTo("bereavementBenefit");
    }

    @Test
    public void shouldNotSetCaseAccessCategory_givenInvalidBenefit() {
        unitUnderTest.setCategories(null);

        assertThat(unitUnderTest.getCaseAccessCategory()).isNull();
    }

    @Test
    public void shouldSetCaseManagementCategory_givenValidBenefit() {
        unitUnderTest.setCategories(BEREAVEMENT_BENEFIT);

        DynamicListItem expectedValue = new DynamicListItem("bereavementBenefit", "Bereavement Benefit");
        assertThat(unitUnderTest.getCaseManagementCategory()).isEqualTo(
            new DynamicList(
                expectedValue,
                List.of(expectedValue)));
    }

    @Test
    public void shouldSetCaseManagementCategory_givenInvalidBenefit() {
        unitUnderTest.setCategories(null);

        assertThat(unitUnderTest.getCaseManagementCategory()).isNull();
    }

}
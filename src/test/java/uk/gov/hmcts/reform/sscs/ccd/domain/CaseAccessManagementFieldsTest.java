package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.BEREAVEMENT_BENEFIT;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class CaseAccessManagementFieldsTest {

    private CaseAccessManagementFields unitUnderTest;

    @Before
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
    public void shouldSetCategories_givenValidBenefit() {

        unitUnderTest.setCategories(BEREAVEMENT_BENEFIT);

        DynamicListItem expectedValue = new DynamicListItem("bereavementBenefit", "Bereavement Benefit");
        assertThat(unitUnderTest.getCaseAccessCategory()).isEqualTo("bereavementBenefit");
        assertThat(unitUnderTest.getCaseManagementCategory()).isEqualTo(
            new DynamicList(
                expectedValue,
                List.of(expectedValue)));
    }

    @Test
    public void shouldNotSetCategories_givenInvalidBenefit() {

        unitUnderTest.setCategories(null);

        assertThat(unitUnderTest.getCaseAccessCategory()).isNull();
        assertThat(unitUnderTest.getCaseManagementCategory()).isNull();
    }

}
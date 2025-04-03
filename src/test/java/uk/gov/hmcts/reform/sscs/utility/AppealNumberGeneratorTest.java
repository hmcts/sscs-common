package uk.gov.hmcts.reform.sscs.utility;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.hmcts.reform.sscs.utility.AppealNumberGenerator.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppealNumberGeneratorTest {

    @Test
    public void shouldCreateRandomAppealnumber() {

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{10}$");

        String appealNumber = generateAppealNumber();
        Matcher matcher = pattern.matcher(appealNumber);
        assertTrue(matcher.matches());

    }

    @Test
    public void shouldGenerateRandomAppealNumberOnEachCall() {

        String appealNumber1 = generateAppealNumber();
        String appealNumber2 = generateAppealNumber();

        assertNotEquals(appealNumber1, appealNumber2);
    }

    @Test
    public void shouldReturnFalseWhereCaseIsDraft() {
        SscsCaseDetails caseDetails = Mockito.mock(SscsCaseDetails.class);
        Mockito.when(caseDetails.getState()).thenReturn(State.DRAFT.getId());

        boolean result = filterCaseNotDraftOrArchivedDraft(caseDetails);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhereCaseIsNotDraft() {
        SscsCaseDetails caseDetails = Mockito.mock(SscsCaseDetails.class);
        Mockito.when(caseDetails.getState()).thenReturn(State.APPEAL_CREATED.getId());

        boolean result = filterCaseNotDraftOrArchivedDraft(caseDetails);

        assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("generateInvalidStateScenarios")
    public void filterActiveCasesWithGivenStateThenShouldReturnFalse(State state) {
        SscsCaseDetails caseDetails = Mockito.mock(SscsCaseDetails.class);
        Mockito.when(caseDetails.getState()).thenReturn(state.getId());
        boolean result = filterActiveCasesForCitizen(caseDetails);

        assertFalse(result);
    }

    @Test
    public void filterActiveCasesWithAppealCreatedStateThenShouldReturnTrue() {
        SscsCaseDetails caseDetails = Mockito.mock(SscsCaseDetails.class);
        Mockito.when(caseDetails.getState()).thenReturn(State.APPEAL_CREATED.getId());

        boolean result = filterActiveCasesForCitizen(caseDetails);

        assertTrue(result);
    }

    @Test
    public void filterDormantCasesWithAppealCreatedStateThenShouldReturnFalse() {
        SscsCaseDetails caseDetails = Mockito.mock(SscsCaseDetails.class);
        Mockito.when(caseDetails.getState()).thenReturn(State.DRAFT.getId());

        boolean result = filterDormantCasesForCitizen(caseDetails);

        assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("generateDormantVoidStateScenarios")
    public void filterDormantCasesWithGivenStateThenShouldReturnTrue(State state) {
        SscsCaseDetails caseDetails = Mockito.mock(SscsCaseDetails.class);
        Mockito.when(caseDetails.getState()).thenReturn(state.getId());

        boolean result = filterDormantCasesForCitizen(caseDetails);

        assertTrue(result);
    }

    @Test
    public void shouldReturnTrueWhereCaseIsNull() {
        SscsCaseDetails caseDetails = null;

        boolean result = filterCaseNotDraftOrArchivedDraft(caseDetails);

        assertTrue(result);
    }

    private Object[] generateInvalidStateScenarios() {
        return new Object[]{
            State.DORMANT_APPEAL_STATE,
            State.VOID_STATE,
            State.DRAFT,
            State.DRAFT_ARCHIVED
        };
    }

    private Object[] generateDormantVoidStateScenarios() {
        return new Object[]{
            State.DORMANT_APPEAL_STATE,
            State.VOID_STATE
        };
    }
}

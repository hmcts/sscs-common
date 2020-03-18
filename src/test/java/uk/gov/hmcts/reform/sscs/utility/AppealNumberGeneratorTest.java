package uk.gov.hmcts.reform.sscs.utility;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.utility.AppealNumberGenerator.filterCaseNotDraftOrArchivedDraft;
import static uk.gov.hmcts.reform.sscs.utility.AppealNumberGenerator.generateAppealNumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void shouldReturnTrueWhereCaseIsNull() {
        SscsCaseDetails caseDetails = null;

        boolean result = filterCaseNotDraftOrArchivedDraft(caseDetails);

        assertTrue(result);
    }
}

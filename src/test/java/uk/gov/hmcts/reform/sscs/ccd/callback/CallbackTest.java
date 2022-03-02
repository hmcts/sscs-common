package uk.gov.hmcts.reform.sscs.ccd.callback;

import static junit.framework.TestCase.assertEquals;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.EventType;

@RunWith(MockitoJUnitRunner.class)
public class CallbackTest {

    private final EventType event = EventType.APPEAL_RECEIVED;
    @Mock
    private CaseDetails<CaseData> caseDetails;
    private final Optional<CaseDetails<CaseData>> caseDetailsBefore = Optional.empty();

    private Callback<CaseData> callback;

    @Before
    public void setUp() {
        callback = new Callback<>(
            caseDetails,
            caseDetailsBefore,
            event,
            false
        );
    }

    @Test
    public void should_hold_onto_values() {

        assertEquals(caseDetails, callback.getCaseDetails());
        assertEquals(caseDetailsBefore, callback.getCaseDetailsBefore());
        assertEquals(event, callback.getEvent());
    }
}

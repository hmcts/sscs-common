package uk.gov.hmcts.reform.sscs.ccd.callback;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.EventType;

@ExtendWith(MockitoExtension.class)
public class CallbackTest {

    private final EventType event = EventType.APPEAL_RECEIVED;
    @Mock
    private CaseDetails<CaseData> caseDetails;
    private final Optional<CaseDetails<CaseData>> caseDetailsBefore = Optional.empty();

    private Callback<CaseData> callback;

    @BeforeEach
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

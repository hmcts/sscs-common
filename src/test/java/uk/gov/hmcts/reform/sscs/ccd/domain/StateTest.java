package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.WITH_DWP;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.WITH_UT;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class StateTest {
    // copied from the State TAB in the CCD Definition file
    private static final String ALL_STATES = "appealCreated\n" +
            "closed\n" +
            "dormantAppealState\n" +
            "draft\n" +
            "draftArchived\n" +
            "hearing\n" +
            "incompleteApplication\n" +
            "incompleteApplicationInformationReqsted\n" +
            "interlocutoryReviewState\n" +
            "listingError\n" +
            "notListable\n" +
            "readyToList\n" +
            "responseReceived\n" +
            "validAppeal\n" +
            "voidState\n" +
            "withDwp\n" +
            "withUT";

    @Test
    public void hasAllStatesDefinedInCcdDefinitionFile() {
        String[] allStateIds = ALL_STATES.split("\n");
        State[] allStatesActual = State.values();

        for (String stateId : allStateIds) {
            final Optional<State> found = Arrays.stream(allStatesActual).filter(s -> s.getId().contains(stateId)).findFirst();
            assertTrue(found.isPresent(), String.format("missing %s", stateId));
        }
    }

    @Test
    public void getStateById() {
        assertEquals(WITH_DWP, State.getById("withDwp"));
    }

    @Test
    public void getwithUtId() {
        assertEquals(WITH_UT, State.getById("withUT"));
    }

}

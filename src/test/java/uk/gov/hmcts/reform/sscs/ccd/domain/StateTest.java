package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.WITH_DWP;
import static uk.gov.hmcts.reform.sscs.ccd.domain.State.WITH_UT;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

class StateTest {
    // copied from the State TAB in the CCD Definition file
    private static final String ALL_STATES = """
        appealCreated
        closed
        dormantAppealState
        draft
        draftArchived
        hearing
        incompleteApplication
        incompleteApplicationInformationReqsted
        interlocutoryReviewState
        listingError
        notListable
        readyToList
        responseReceived
        validAppeal
        voidState
        withDwp
        withUT
        awaitOtherPartyData
        """;

    @Test
    void hasAllStatesDefinedInCcdDefinitionFile() {
        String[] allStateIds = ALL_STATES.split("\n");
        State[] allStatesActual = State.values();

        for (String stateId : allStateIds) {
            final Optional<State> found = Arrays.stream(allStatesActual).filter(s -> s.getId().contains(stateId)).findFirst();
            assertTrue(found.isPresent(), String.format("missing %s", stateId));
        }
    }

    @Test
    void getStateById() {
        assertEquals(WITH_DWP, State.getById("withDwp"));
    }

    @Test
    void getwithUtId() {
        assertEquals(WITH_UT, State.getById("withUT"));
    }

}

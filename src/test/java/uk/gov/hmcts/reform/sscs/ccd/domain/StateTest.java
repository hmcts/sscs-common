package uk.gov.hmcts.reform.sscs.ccd.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
            "outcome\n" +
            "pendingAppeal\n" +
            "readyToList\n" +
            "responseReceived\n" +
            "testCreate\n" +
            "validAppeal\n" +
            "incompleteApplicationVoidState\n" +
            "voidState\n" +
            "withDwp";

    @Test
    public void hasAllStatesDefinedInCcdDefinitionFile() {
        String[] allStateIds = ALL_STATES.split("\n");
        State[] allStatesActual = State.values();

        for (String stateId : allStateIds) {
            final Optional<State> found = Arrays.stream(allStatesActual).filter(s -> s.getId().contains(stateId)).findFirst();
            assertTrue(String.format("missing %s", stateId), found.isPresent());
        }
    }

}

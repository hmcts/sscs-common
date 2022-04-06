package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JUnitParamsRunner.class)
public class SessionLookUpServiceTest {

    private static final SessionLookupService sessionLookUpService = new SessionLookupService();

    static {
        sessionLookUpService.init();
    }

    @Test
    public void shouldGetPanelMembersForValidCcdKey() {
        List<String> panelMembers = new ArrayList<>();
        panelMembers.add("Judge");
        panelMembers.add("Doctor");
        panelMembers.add("Disability Member (DQPM)");
        List<String> resultList = sessionLookUpService.getPanelMembers("002CC");
        assertTrue(resultList.containsAll(panelMembers));
    }

    @Test
    public void shouldGetDurationForValidCcdKey() {
        assertEquals(sessionLookUpService.getDuration("002CC"), 60);
        assertEquals(sessionLookUpService.getDuration("051FR"), 20);
        assertEquals(sessionLookUpService.getDuration("061WI"), 20);
    }

    @Test
    public void shouldGetEmptyDurationForCcdKeyWithNoDuration() {
        assertEquals(sessionLookUpService.getDuration("002AA"), 0);
    }

    @Test
    public void shouldReturnZeroIfCcdKeyDoesntExist() {
        assertEquals(sessionLookUpService.getDuration("1234"), 0);
    }

    @Test
    public void sessionCaseCodeMappingMapShouldNotBeNull() {
        assertNotNull(sessionLookUpService.getSessionCaseCodeMappingMap());
    }



}

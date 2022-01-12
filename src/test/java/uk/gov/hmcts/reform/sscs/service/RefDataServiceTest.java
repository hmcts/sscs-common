package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;


public class RefDataServiceTest {

    @Mock
    private IdamService idamService;
    @Mock
    private RefDataApi refDataApi;

    RefDataService refDataService;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
        refDataService = new RefDataService(refDataApi, idamService);
    }

    @Test
    public void shouldReturnCourtVenue() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByName("auth2", "s2s", "venue_name")).thenReturn(CourtVenue.builder().epimsId("epims_id").build());

        CourtVenue venue = refDataService.getVenueRefData("venue_name");

        verify(refDataApi, times(1)).courtVenueByName("auth2", "s2s", "venue_name");
        assertNotNull(venue);
        assertEquals(venue.getEpimsId(), "epims_id");
    }
}

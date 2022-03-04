package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
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
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").email("").serviceAuthorization("s2s").build();

        List<CourtVenue> refData = List.of(CourtVenue.builder().epimsId("epims_id").venueName("venue_name").build(),
                CourtVenue.builder().epimsId("epims_id").venueName("not_venue_name").build());

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByName("auth2", "s2s", "31")).thenReturn(refData);

        CourtVenue venue = refDataService.getVenueRefData("venue_name");

        verify(refDataApi, times(1)).courtVenueByName("auth2", "s2s", "31");
        assertNotNull(venue);
        assertEquals(venue.getEpimsId(), "epims_id");
    }

    @Test
    public void shouldReturnNullReponseForEmptyRefDataResponse() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").email("").serviceAuthorization("s2s").build();

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByName("auth2", "s2s", "31")).thenReturn(null);

        CourtVenue venue = refDataService.getVenueRefData("venue_name");

        verify(refDataApi, times(1)).courtVenueByName("auth2", "s2s", "31");
        assertNull(venue);
    }
}

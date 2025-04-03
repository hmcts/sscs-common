package uk.gov.hmcts.reform.sscs.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@ExtendWith(MockitoExtension.class)
public class RefDataServiceTest {

    private static final String SSCS_COURT_TYPE_ID = "31";
    private static final String EPIMS_ID = "314125";
    @Mock
    private IdamService idamService;
    @Mock
    private RefDataApi refDataApi;

    @InjectMocks
    RefDataService refDataService;

    @Test
    public void getCourtVenueRefDataByEpimsId() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

        List<CourtVenue> courtVenue = List.of(CourtVenue.builder()
                .epimsId(EPIMS_ID)
                .courtTypeId(SSCS_COURT_TYPE_ID)
                .courtStatus("Open")
                .venueName("sscs_venue_name")
                .build(),
            CourtVenue.builder()
                .epimsId(EPIMS_ID)
                .courtTypeId(SSCS_COURT_TYPE_ID)
                .courtStatus("Closed")
                .venueName("sscs_venue_name_closed")
                .build(),
            CourtVenue.builder()
                .epimsId("232341")
                .courtTypeId("22")
                .venueName("other_venue_name")
                .build());

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByEpimsId("auth2", "s2s", EPIMS_ID))
            .thenReturn(courtVenue);

        CourtVenue venue = refDataService.getCourtVenueRefDataByEpimsId(EPIMS_ID);

        assertNotNull(venue);
        assertEquals(venue.getEpimsId(), EPIMS_ID);
    }

    @Test
    public void getCourtVenueRefDataByEpimsIdWrongNumberOfReturnedCourtVenues() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

        List<CourtVenue> courtVenue = List.of(CourtVenue.builder()
                .epimsId(EPIMS_ID)
                .courtTypeId(SSCS_COURT_TYPE_ID)
                .venueName("venue_name")
                .build(),
            CourtVenue.builder()
                .courtTypeId(SSCS_COURT_TYPE_ID)
                .epimsId("epims_id")
                .venueName("not_venue_name")
                .build());

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByEpimsId("auth2", "s2s", EPIMS_ID))
            .thenReturn(courtVenue);

        assertThrows(IllegalStateException.class,
            () -> refDataService.getCourtVenueRefDataByEpimsId(EPIMS_ID));
    }

    @Test
    public void getCourtVenueRefDataByEpimsIdNoCourtVenue() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

        when(idamService.getIdamTokens()).thenReturn(idamTokens);

        assertThrows(IllegalStateException.class,
            () -> refDataService.getCourtVenueRefDataByEpimsId(EPIMS_ID));
    }
}

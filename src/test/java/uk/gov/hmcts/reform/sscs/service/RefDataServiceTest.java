package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

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
    private static final String SSCS_SERVICE_CODE = "BBA3";
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
                .courtStatus("Open")
                .venueName("other_venue_name")
                .build());

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByEpimsId("auth2", "s2s", SSCS_SERVICE_CODE, EPIMS_ID))
            .thenReturn(courtVenue);

        CourtVenue venue = refDataService.getCourtVenueRefDataByEpimsId(EPIMS_ID);

        assertThat(venue).isNotNull();
        assertThat(venue.getEpimsId()).isEqualTo(EPIMS_ID);
        assertThat(venue.getVenueName()).isEqualTo("sscs_venue_name");
    }

    @Test
    public void getCourtVenueRefDataByEpimsIdWrongNumberOfReturnedOpenCourtVenues() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

        List<CourtVenue> courtVenue = List.of(CourtVenue.builder()
                .epimsId(EPIMS_ID)
                .courtTypeId(SSCS_COURT_TYPE_ID)
                .venueName("venue_name")
                .courtStatus("Open")
                .build(),
            CourtVenue.builder()
                .epimsId("epims_id")
                .courtTypeId(SSCS_COURT_TYPE_ID)
                .venueName("not_venue_name")
                .courtStatus("Open")
                .build());

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByEpimsId("auth2", "s2s", SSCS_SERVICE_CODE, EPIMS_ID))
            .thenReturn(courtVenue);

        assertThatThrownBy(() -> refDataService.getCourtVenueRefDataByEpimsId(EPIMS_ID))
            .isInstanceOfAny(IllegalStateException.class)
            .hasMessageContaining("Exactly one SSCS court venue is required for epims ID: " + EPIMS_ID);
    }

    @Test
    public void getCourtVenueRefDataByEpimsIdNoOpenCourtsReturned() {
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

        List<CourtVenue> courtVenue = List.of(CourtVenue.builder()
                        .epimsId(EPIMS_ID)
                        .venueName("venue_name")
                        .courtStatus("Closed")
                        .build());

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(refDataApi.courtVenueByEpimsId("auth2", "s2s", SSCS_SERVICE_CODE, EPIMS_ID))
                .thenReturn(courtVenue);

        assertThatThrownBy(() -> refDataService.getCourtVenueRefDataByEpimsId(EPIMS_ID))
                .isInstanceOfAny(IllegalStateException.class)
                .hasMessageContaining("Exactly one SSCS court venue is required for epims ID: " + EPIMS_ID);
    }
}

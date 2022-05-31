package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;


@RunWith(MockitoJUnitRunner.class)
public class VenueServiceTest {

    private static final String PROCESSING_VENUE_1 = "test_place";
    private static final String PROCESSING_VENUE_2 = "test_other_place";
    private static final String VALID_EPIMS_ID = "45900";
    private static final String INVALID_EPIMS_ID = "abcdes";

    @Mock
    private VenueDataLoader venueDataLoader;

    @Mock
    private AirLookupService airLookupService;

    @InjectMocks
    private VenueService venueService;

    @Test
    public void getHearingLocations_shouldReturnCorrespondingEpimsIdForVenue() {

        setupVenueMaps();

        Optional<String> result = venueService.getEpimsIdForVenue(PROCESSING_VENUE_1);

        assertThat(result).isPresent();

        String epimsId = result.get();

        assertThat(epimsId).isEqualTo("987632");
    }

    @Test
    public void givenEpimsId_shouldReturnCorrespondingVenueDetails() {
        setupVenueMaps();

        VenueDetails result = venueService.getVenueDetailsForActiveVenueByEpims(VALID_EPIMS_ID);

        assertThat(result).isNotNull();
    }

    @Test
    public void givenInvalidEpimsId_shouldReturnNull() {
        setupVenueMaps();

        VenueDetails result = venueService.getVenueDetailsForActiveVenueByEpims(INVALID_EPIMS_ID);

        assertThat(result).isNull();
    }

    private void setupVenueMaps() {
        Map<String, Integer> venueIdMap = Map.of(PROCESSING_VENUE_1,
            68, PROCESSING_VENUE_2, 2);

        Map<String, VenueDetails> venueDetailsMap = Map.of(
            "68", VenueDetails.builder()
                .epimsId("987632")
                .build(),
            "2", VenueDetails.builder()
                .epimsId("111111")
                .build());

        Map<String, VenueDetails> venueDetailsMapByEpims = Map.of(
                VALID_EPIMS_ID, VenueDetails.builder()
                .epimsId(VALID_EPIMS_ID)
                .build(),
            "111111", VenueDetails.builder()
                .epimsId("111111")
                .build());

        when(airLookupService.getLookupVenueIdByAirVenueName()).thenReturn(venueIdMap);
        when(venueDataLoader.getVenueDetailsMap()).thenReturn(venueDetailsMap);
        when(venueDataLoader.getActiveVenueDetailsMapByEpims()).thenReturn(venueDetailsMapByEpims);
    }

}

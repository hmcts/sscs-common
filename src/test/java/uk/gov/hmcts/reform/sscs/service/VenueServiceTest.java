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

    public static final String PROCESSING_VENUE_1 = "test_place";
    public static final String PROCESSING_VENUE_2 = "test_other_place";

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
    public void givenRpcPostcode_shouldReturnCorrespondingEpimsIdForVenue() {
        setupVenueMaps();

        Optional<String> result = venueService.getEpimsIdForActiveVenueByPostcode("LN5 7PS");

        assertThat(result).isPresent();
        String epimsId = result.get();
        assertThat(epimsId).isEqualTo("233333");
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

        Map<String, VenueDetails> venueDetailsMapByPostcode = Map.of(
            "LN5 7PS", VenueDetails.builder()
                .epimsId("233333")
                .build(),
            "MK9 2AJ", VenueDetails.builder()
                .epimsId("111111")
                .build());

        when(airLookupService.getLookupVenueIdByAirVenueName()).thenReturn(venueIdMap);
        when(venueDataLoader.getVenueDetailsMap()).thenReturn(venueDetailsMap);
        when(venueDataLoader.getActiveVenueDetailsMapByPostcode()).thenReturn(venueDetailsMapByPostcode);
    }

}

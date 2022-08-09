package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
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

    private static final String VALID_EPIMS_ID = "45900";
    private static final String INVALID_EPIMS_ID = "abcdes";
    public static final String PROCESSING_VENUE_1 = "test_place";
    public static final String PROCESSING_VENUE_2 = "test_other_place";
    private static final String RPC_LEEDS = "SSCS Leeds";
    private static final String RPC_BIRMINGHAM = "SSCS Birmingham";
    private static final String RPC_CARDIFF = "SSCS Cardiff";

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
    public void getActiveRegionalEpimsIdsForRpc_shouldReturnCorrespondingRegionalEpimsIdsForVenue() {
        setupRegionalVenueMaps();

        List<String> result = venueService.getActiveRegionalEpimsIdsForRpc(RPC_LEEDS);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result.get(0)).isEqualTo("112233");
        assertThat(result.get(1)).isEqualTo("332211");
    }

    @Test
    public void getActiveRegionalEpimsIdsForRpc_shouldReturnEmptyEpimsIdsList() {
        setupRegionalVenueMaps();

        List<String> result = venueService.getActiveRegionalEpimsIdsForRpc(RPC_CARDIFF);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }

    @Test
    public void givenEpimsId_shouldReturnCorrespondingVenueDetails() {
        setupVenueMaps();

        VenueDetails result = venueService.getVenueDetailsForActiveVenueByEpimsId(VALID_EPIMS_ID);

        assertThat(result).isNotNull();
    }

    @Test
    public void givenInvalidEpimsId_shouldReturnNull() {
        setupVenueMaps();

        VenueDetails result = venueService.getVenueDetailsForActiveVenueByEpimsId(INVALID_EPIMS_ID);

        assertThat(result).isNull();
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
                .venName("test_place")
                .epimsId("987632")
                .build(),
            "2", VenueDetails.builder()
                .venName("test_place2")
                .epimsId("111111")
                .build());

        Map<String, VenueDetails> venueDetailsMapByEpims = Map.of(
                VALID_EPIMS_ID, VenueDetails.builder()
                .epimsId(VALID_EPIMS_ID)
                .build(),
            "111111", VenueDetails.builder()
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
        when(venueDataLoader.getActiveVenueDetailsMapByEpimsId()).thenReturn(venueDetailsMapByEpims);
        when(venueDataLoader.getActiveVenueDetailsMapByPostcode()).thenReturn(venueDetailsMapByPostcode);
    }

    private void setupRegionalVenueMaps() {
        Map<String, List<String>> activeVenueEpimsIdsMapByRpc = Map.of(
                RPC_LEEDS, Arrays.asList("112233", "332211", "123123", "321321"),
                RPC_BIRMINGHAM, Arrays.asList("445566", "665544", "456456", "654654"));
        when(venueDataLoader.getActiveVenueEpimsIdsMapByRpc()).thenReturn(activeVenueEpimsIdsMapByRpc);
    }

}

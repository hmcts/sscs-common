package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.lenient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

@ExtendWith(MockitoExtension.class)
public class VenueServiceTest {

    private static final String VALID_EPIMS_ID = "45900";
    private static final String INVALID_EPIMS_ID = "abcdes";

    private static final String INVALID_POSTCODE = "123456";

    public static final String PROCESSING_VENUE_1 = "test_place";
    public static final String PROCESSING_VENUE_2 = "test_other_place";
    private static final String RPC_LEEDS = "SSCS Leeds";
    private static final String RPC_BIRMINGHAM = "SSCS Birmingham";
    private static final String RPC_NEWCASTLE = "SSCS Newcastle";

    @Mock
    private VenueDataLoader venueDataLoader;

    @Mock
    private AirLookupService airLookupService;

    @InjectMocks
    private VenueService venueService;

    @Test
    public void getEpimsIdForVenue_shouldReturnCorrespondingEpimsIdForVenue() {
        setupVenueMaps();

        String result = venueService.getEpimsIdForVenue(PROCESSING_VENUE_1);

        assertThat(result).isEqualTo("987632");
    }

    @Test
    public void getEpimsIdForVenue_givenInvalidEpimsId_shouldThrowException() {
        setupVenueMaps();

        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> venueService.getEpimsIdForVenue(INVALID_POSTCODE));
    }

    @Test
    public void getActiveRegionalEpimsIdsForRpc_shouldReturnCorrespondingRegionalEpimsIdsForVenue() {
        setupRegionalVenueMaps();

        List<VenueDetails> result = venueService.getActiveRegionalEpimsIdsForRpc("112233");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getEpimsId()).isEqualTo("112233");
        assertThat(result.get(1).getEpimsId()).isEqualTo("332211");
    }

    @Test
    public void getActiveRegionalEpimsIdsForRpc_shouldReturnEmptyEpimsIdsList() {
        setupRegionalVenueMaps();

        List<VenueDetails> result = venueService.getActiveRegionalEpimsIdsForRpc(INVALID_EPIMS_ID);

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

        String result = venueService.getEpimsIdForActiveVenueByPostcode("LN5 7PS");

        assertThat(result).isEqualTo("233333");
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

        lenient().when(airLookupService.getLookupVenueIdByAirVenueName()).thenReturn(venueIdMap);
        lenient().when(venueDataLoader.getVenueDetailsMap()).thenReturn(venueDetailsMap);
        lenient().when(venueDataLoader.getActiveVenueDetailsMapByEpimsId()).thenReturn(venueDetailsMapByEpims);
        lenient().when(venueDataLoader.getActiveVenueDetailsMapByPostcode()).thenReturn(venueDetailsMapByPostcode);
    }

    private void setupRegionalVenueMaps() {
        Map<String, VenueDetails> venueDetailsMapByEpimsId = Map.of(
            "112233", VenueDetails.builder()
                .regionalProcessingCentre(RPC_LEEDS)
                .epimsId("112233")
                .build(),
            "445566", VenueDetails.builder()
                .regionalProcessingCentre(RPC_BIRMINGHAM)
                .epimsId("445566")
                .build());


        Map<String, List<VenueDetails>> activeVenueEpimsIdsMapByRpc = Map.of(
                RPC_LEEDS, Arrays.asList(
                        VenueDetails.builder().epimsId("112233").build(),
                        VenueDetails.builder().epimsId("332211").build(),
                        VenueDetails.builder().epimsId("123123").build(),
                        VenueDetails.builder().epimsId("321321").build()),
                RPC_BIRMINGHAM, Arrays.asList(
                        VenueDetails.builder().epimsId("445566").build(),
                        VenueDetails.builder().epimsId("665544").build(),
                        VenueDetails.builder().epimsId("456456").build(),
                        VenueDetails.builder().epimsId("654654").build()));

        lenient().when(venueDataLoader.getActiveVenueDetailsMapByEpimsId()).thenReturn(venueDetailsMapByEpimsId);
        lenient().when(venueDataLoader.getActiveVenueEpimsIdsMapByRpc()).thenReturn(activeVenueEpimsIdsMapByRpc);
    }

}

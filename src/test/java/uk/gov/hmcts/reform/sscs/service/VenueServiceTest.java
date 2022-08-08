package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

@RunWith(MockitoJUnitRunner.class)
public class VenueServiceTest {

    private static final String VALID_EPIMS_ID = "45900";
    private static final String INVALID_EPIMS_ID = "abcdes";

    private static final String INVALID_POSTCODE = "123456";

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
    public void getRegionalEpimsIdsForRpc_shouldReturnCorrespondingRegionalEpimsIdsForVenue() {
        setupRegionalVenueMaps();

        List<String> result = venueService.getRegionalEpimsIdsForRpc(RPC_LEEDS);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsAnyOf("112233", "332211");
    }

    @Test
    public void getRegionalEpimsIdsForRpc_shouldReturnEmptyEpimsIdsList() {
        setupRegionalVenueMaps();

        List<String> result = venueService.getRegionalEpimsIdsForRpc(RPC_CARDIFF);

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

        when(airLookupService.getLookupVenueIdByAirVenueName()).thenReturn(venueIdMap);
        when(venueDataLoader.getVenueDetailsMap()).thenReturn(venueDetailsMap);
        when(venueDataLoader.getActiveVenueDetailsMapByEpimsId()).thenReturn(venueDetailsMapByEpims);
        when(venueDataLoader.getActiveVenueDetailsMapByPostcode()).thenReturn(venueDetailsMapByPostcode);
    }

    private void setupRegionalVenueMaps() {
        Map<RegionalProcessingCenter, VenueDetails> venueDetailsMapByRpc = Map.of(
                RegionalProcessingCenter.builder()
                        .epimsId("112233")
                        .name("SSCS Leeds")
                        .build(),
                        VenueDetails.builder()
                                .epimsId("112233")
                                .regionalProcessingCentre("SSCS Leeds")
                                .build(),
                RegionalProcessingCenter.builder()
                        .epimsId("445566")
                        .name("SSCS Birmingham")
                        .build(),
                        VenueDetails.builder()
                                .epimsId("445566")
                                .regionalProcessingCentre("SSCS Birmingham")
                                .build(),
                RegionalProcessingCenter.builder()
                        .epimsId("332211")
                        .name("SSCS Leeds")
                        .build(),
                        VenueDetails.builder()
                                .epimsId("332211")
                                .regionalProcessingCentre("SSCS Leeds")
                                .build());

        when(venueDataLoader.getVenueDetailsMapByRpc()).thenReturn(venueDetailsMapByRpc);
    }

}

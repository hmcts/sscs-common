package uk.gov.hmcts.reform.sscs.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueDataLoader venueDataLoader;
    private final AirLookupService airLookupService;

    public String getEpimsIdForVenue(String processingVenue) {

        String venueId = String.valueOf(airLookupService.getLookupVenueIdByAirVenueName()
            .get(processingVenue));

        VenueDetails venueDetails = venueDataLoader.getVenueDetailsMap()
            .get(String.valueOf(venueId));

        return Optional.ofNullable(venueDetails)
            .map(VenueDetails::getEpimsId)
            .orElseThrow(() -> {
                throw new IllegalStateException("Unable to find epims ID for processing venue: " + processingVenue);
            });
    }

    public List<String> getRegionalEpimsIdsForRpc(String rpc) {

        return venueDataLoader.getVenueDetailsMapByRpc()
                .entrySet()
                .stream()
                .filter(entry -> rpc.equals(entry.getKey().getName()))
                .map(entry -> entry.getValue().getEpimsId())
                .collect(Collectors.toList());
    }

    public VenueDetails getVenueDetailsForActiveVenueByEpimsId(String epimsId) {

        return venueDataLoader.getActiveVenueDetailsMapByEpimsId()
            .get(epimsId);
    }

    public String getEpimsIdForActiveVenueByPostcode(String postcode) {

        VenueDetails venueDetails = venueDataLoader.getActiveVenueDetailsMapByPostcode()
            .get(postcode);

        return Optional.ofNullable(venueDetails)
            .map(VenueDetails::getEpimsId).orElseThrow(() -> {
                throw new IllegalStateException("Unable to find epims ID for postcode: " + postcode);
            });
    }

}

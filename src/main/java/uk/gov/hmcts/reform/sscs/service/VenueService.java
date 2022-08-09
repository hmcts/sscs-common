package uk.gov.hmcts.reform.sscs.service;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueDataLoader venueDataLoader;
    private final AirLookupService airLookupService;

    public Optional<String> getEpimsIdForVenue(String processingVenue) {

        String venueId = String.valueOf(airLookupService.getLookupVenueIdByAirVenueName()
            .get(processingVenue));

        VenueDetails venueDetails = venueDataLoader.getVenueDetailsMap()
            .get(String.valueOf(venueId));

        return Optional.ofNullable(venueDetails)
            .map(VenueDetails::getEpimsId);
    }

    public List<String> getActiveRegionalEpimsIdsForRpc(String rpc) {

        return venueDataLoader.getActiveVenueEpimsIdsMapByRpc()
                .entrySet()
                .stream()
                .filter(e -> rpc.equals(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()).stream().collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }

    public VenueDetails getVenueDetailsForActiveVenueByEpimsId(String epimsId) {

        return venueDataLoader.getActiveVenueDetailsMapByEpimsId()
            .get(epimsId);
    }

    public Optional<String> getEpimsIdForActiveVenueByPostcode(String postcode) {

        VenueDetails venueDetails = venueDataLoader.getActiveVenueDetailsMapByPostcode()
            .get(postcode);

        return Optional.ofNullable(venueDetails)
            .map(VenueDetails::getEpimsId);
    }

}

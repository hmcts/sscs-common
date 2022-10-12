package uk.gov.hmcts.reform.sscs.service;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

        return getEpimsIdForVenueId(venueId);
    }

    public String getEpimsIdForVenueId(String venueId) {
        VenueDetails venueDetails = venueDataLoader.getVenueDetailsMap()
            .get(String.valueOf(venueId));

        return Optional.ofNullable(venueDetails)
            .map(VenueDetails::getEpimsId)
            .orElseThrow(() -> {
                throw new IllegalStateException("Unable to find epims ID for venue ID: " + venueId);
            });
    }

    public List<VenueDetails> getActiveRegionalEpimsIdsForRpc(String epimsId) {
        VenueDetails venueDetails = getVenueDetailsForActiveVenueByEpimsId(epimsId);
        if (nonNull(venueDetails)) {
            return venueDataLoader.getActiveVenueEpimsIdsMapByRpc()
                .entrySet()
                .stream()
                .filter(e -> venueDetails.getRegionalProcessingCentre().equals(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()).stream().collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        }
        return Collections.emptyList();
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

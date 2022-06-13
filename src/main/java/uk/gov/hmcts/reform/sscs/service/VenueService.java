package uk.gov.hmcts.reform.sscs.service;

import java.util.Optional;
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

}

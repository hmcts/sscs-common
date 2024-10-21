package uk.gov.hmcts.reform.sscs.service;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseManagementLocation;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;

@Service
public class CaseManagementLocationService {

    private final RefDataService refDataService;
    private final VenueService venueService;

    public CaseManagementLocationService(RefDataService refDataService,
                                         VenueService venueService) {
        this.refDataService = refDataService;
        this.venueService = venueService;
    }

    public Optional<CaseManagementLocation> retrieveCaseManagementLocation(String processingVenue, RegionalProcessingCenter
            regionalProcessingCenter) {
        if (isNotBlank(processingVenue) && nonNull(regionalProcessingCenter)) {

            String venueEpimsId = venueService.getEpimsIdForVenue(processingVenue);
            CourtVenue courtVenue = refDataService.getCourtVenueRefDataByEpimsId(venueEpimsId);

            if (nonNull(courtVenue)
                && isNotBlank(courtVenue.getRegionId())) {
                return Optional.of(CaseManagementLocation.builder()
                    .baseLocation(regionalProcessingCenter.getEpimsId())
                    .region(courtVenue.getRegionId())
                    .build());
            }
        }
        return Optional.empty();
    }

}

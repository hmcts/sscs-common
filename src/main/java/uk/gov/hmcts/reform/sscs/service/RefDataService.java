package uk.gov.hmcts.reform.sscs.service;

import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;



@Service
@Slf4j
@ConditionalOnProperty(value = "location_ref.enabled", havingValue = "true")
public class RefDataService {
    private static final String SSCS_COURT_TYPE_ID = "31";
    private final RefDataApi refDataApi;
    private final IdamService idamService;

    public RefDataService(@Autowired RefDataApi refDataApi,
                          @Autowired IdamService idamService) {
        this.refDataApi = refDataApi;
        this.idamService = idamService;
    }

    public CourtVenue getVenueRefData(@NonNull String venueName) {
        log.info("Requesting venue ref data for venue name: {}", venueName);
        IdamTokens idamTokens = idamService.getIdamTokens();
        log.info("Userdetails = " + idamTokens.getEmail());

        List<CourtVenue> venues = refDataApi.courtVenueByName(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), SSCS_COURT_TYPE_ID);

        return venues != null ? venues.stream()
                .filter(v -> venueName.equals(v.getVenueName())).findAny().orElse(null) : null;
    }
}

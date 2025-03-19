package uk.gov.hmcts.reform.sscs.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.client.CommonReferenceDataApi;
import uk.gov.hmcts.reform.sscs.client.RefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.CourtVenue;
import uk.gov.hmcts.reform.sscs.model.DefaultPanelCategory;
import uk.gov.hmcts.reform.sscs.model.PanelResponse;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "location_ref.enabled", havingValue = "true")
public class RefDataService {
    private static final String SSCS_COURT_TYPE_ID = "31";
    public static final String OPEN = "Open";
    private final RefDataApi refDataApi;
    private final CommonReferenceDataApi commonReferenceDataApi;
    private final IdamService idamService;

    public CourtVenue getCourtVenueRefDataByEpimsId(@NonNull String epimsId) {
        log.info("Requesting venue ref data for epims Id: {}", epimsId);
        IdamTokens idamTokens = idamService.getIdamTokens();

        List<CourtVenue> venues = refDataApi.courtVenueByEpimsId(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), epimsId);

        List<CourtVenue> sscsCourtVenues =
            venues.stream().filter(venue -> SSCS_COURT_TYPE_ID.equals(venue.getCourtTypeId())
                && OPEN.equalsIgnoreCase(venue.getCourtStatus()))
                .collect(Collectors.toList());

        if (sscsCourtVenues.size() != 1) {
            throw new IllegalStateException("Exactly one SSCS court venue is required for epims ID: " + epimsId);
        }

        return sscsCourtVenues.get(0);
    }

    public List<CourtVenue> getCourtVenues() {
        log.info("Requesting court venues for SSCS");
        IdamTokens idamTokens = idamService.getIdamTokens();

        return refDataApi.courtVenues(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), SSCS_COURT_TYPE_ID);
    }

    public List<DefaultPanelCategory> getDefaultPanelCategory(@NonNull String serviceId, @NonNull String key) {
        log.info("Requesting default panel category for serviceId: {} and key: {}", serviceId, key);
        IdamTokens idamTokens = idamService.getIdamTokens();

        PanelResponse response = commonReferenceDataApi.getDefaultPanelCategory(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), serviceId, key);

        DefaultPanelCategory category = response.getListOfValues().stream().findFirst().orElse(null);

        if (category != null) {
            return commonReferenceDataApi.getPanelCategoryMember(idamTokens.getIdamOauth2Token(),
                    idamTokens.getServiceAuthorization(), serviceId, category.getParentCategory(), category.getParentKey()).getListOfValues();
        } else {
            return null;
        }

    }
}

package uk.gov.hmcts.reform.sscs.service;

import static com.google.common.collect.Maps.newHashMap;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.google.common.collect.ImmutableMap;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.Venue;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

@Service
@Slf4j
public class VenueDataLoader {

    private static final String CSV_FILE_PATH = "reference-data/sscs-venues.csv";
    private final Map<String, VenueDetails> venueDetailsMap = newHashMap();
    private final Map<String, VenueDetails> venueDetailsMapByVenueName = newHashMap();
    private final Map<String, VenueDetails> activeVenueDetailsMapByEpims = newHashMap();

    @PostConstruct
    protected void init() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(CSV_FILE_PATH);
        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {

            List<String[]> linesList = reader.readAll();
            linesList.forEach(line -> {
                    VenueDetails venueDetails = VenueDetails.builder()
                            .venueId(line[0])
                            .threeDigitReference(line[1])
                            .regionalProcessingCentre(line[2])
                            .venName(line[3])
                            .venAddressLine1(line[4])
                            .venAddressLine2(line[5])
                            .venAddressTown(line[6])
                            .venAddressCounty(line[7])
                            .venAddressPostcode(line[8])
                            .venAddressTelNo(line[9])
                            .districtId(line[10])
                            .url(line[11])
                            .active(line[12])
                            .gapsVenName(line[13])
                            .comments(line[14])
                            .epimsId(line[15])
                            .build();
                    venueDetailsMap.put(line[0], venueDetails);
                    venueDetailsMapByVenueName.put(line[3] + line[8], venueDetails);
                if (isYes(venueDetails.getActive())) {
                    activeVenueDetailsMapByEpims.put(line[15], venueDetails);
                }
                }
            );
        } catch (IOException | CsvException e) {
            log.error("Error occurred while loading the sscs venues reference data file: " + CSV_FILE_PATH + e);
        }
    }

    public Map<String, VenueDetails> getVenueDetailsMap() {
        return ImmutableMap.copyOf(venueDetailsMap);
    }

    public Map<String, VenueDetails> getActiveVenueDetailsMapByEpims() {
        return ImmutableMap.copyOf(activeVenueDetailsMapByEpims);
    }

    public String getGapVenueName(Venue venue, String venueId) {
        if (StringUtils.isNotBlank(venueId)) {
            VenueDetails venueDetails = venueDetailsMap.get(venueId);
            if (venueDetails != null) {
                return venueDetails.getGapsVenName();
            } else if (venue != null && StringUtils.isNotBlank(venue.getName())) {
                venueDetails = venueDetailsMapByVenueName.get(venue.getName() + venue.getAddress().getPostcode());
                if (venueDetails != null) {
                    return venueDetails.getGapsVenName();
                }
            }
        } else if (venue != null && StringUtils.isNotBlank(venue.getName())) {
            VenueDetails venueDetails = venueDetailsMapByVenueName.get(venue.getName() + venue.getAddress().getPostcode());
            if (venueDetails != null) {
                return venueDetails.getGapsVenName();
            }
        }
        return (venue != null) ? venue.getName() : null;
    }
}

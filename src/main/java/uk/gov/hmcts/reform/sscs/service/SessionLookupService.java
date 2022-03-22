package uk.gov.hmcts.reform.sscs.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.model.CaseCodeMappingDetails;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Service
@Slf4j
public class SessionLookupService {
    private static final String CSV_FILE_PATH = "reference-data/casecodeDetails.csv";
    private final Map<String, CaseCodeMappingDetails> caseCodeDetailsMap = newHashMap();

    @PostConstruct
    protected void init() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(CSV_FILE_PATH);
        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {

            List<String[]> linesList = reader.readAll();
            linesList.forEach(line -> {
                CaseCodeMappingDetails caseCodeDetails = CaseCodeMappingDetails.builder()
                        .benefitDescription(line[0])
                        .benefitCode(line[1])
                        .issueDescription(line[2])
                        .caseCode(line[3])
                        .ccdKey(line[4])
                        .sessionCategory(line[5])
                        .otherSessionCategory(line[6])
                        .duration(Integer.parseInt(line[7]))
                        .panelMembers(line[8])
                        .build();
                caseCodeDetailsMap.put(line[0],caseCodeDetails);
                    }
            );
        } catch (IOException e) {
            log.error("Error occurred while loading the casecodeDetails  reference data file: " + CSV_FILE_PATH + e);
        } catch (CsvException e) {
            log.error("Error occurred while loading the casecodeDetails  reference data file: " + CSV_FILE_PATH + e);
        }
    }

    public String getPanelMembers(String ccdCode){

        if (StringUtils.isNotBlank(ccdCode)) {
            CaseCodeMappingDetails caseCodeMappingDetails = caseCodeDetailsMap.get(ccdCode);
            if(caseCodeMappingDetails!=null){
                return caseCodeMappingDetails.getPanelMembers();
            }
        }
    }

    public int getDuration(String caseCode){
        if (StringUtils.isNotBlank(ccdCode)) {
            return caseCodeDetailsMap.get(caseCode).getDuration();
        }
    }


}

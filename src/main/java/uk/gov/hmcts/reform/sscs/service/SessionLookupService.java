package uk.gov.hmcts.reform.sscs.service;

import static com.google.common.collect.Maps.newHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.exception.SessionDetailsLookUpException;
import uk.gov.hmcts.reform.sscs.model.SessionCaseCodeMapping;

@Service
@Slf4j
public class SessionLookupService {

    private static final String MAPPING_FILE_PATH = "reference-data/sscs-session-mapping.json";
    public static final String MINUTES = "minutes";
    private Map<String, SessionCaseCodeMapping> sessionCaseCodeMappingMap = newHashMap();

    @PostConstruct
    protected void init() {

        try  {
            loadSessionMappingFile();
        } catch (IOException e) {
            logErrorWithSessionDetailLookUp(e);
        }
    }

    private void logErrorWithSessionDetailLookUp(IOException e) {
        String message = "Unable to read in spreadsheet with session-mapping data: " + MAPPING_FILE_PATH;
        SessionDetailsLookUpException ex = new SessionDetailsLookUpException(e);
        log.error(message, ex);
    }

    private void logErrorWithSessionDetailLookUp(String error) {
        SessionDetailsLookUpException ex = new SessionDetailsLookUpException(error);
        log.error("Error while processing the session look up file",ex);
    }

    private void loadSessionMappingFile() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(MAPPING_FILE_PATH);
        try (InputStream inputStream  = classPathResource.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            MapUtils.populateMap(sessionCaseCodeMappingMap, mapper.readValue(inputStream, new TypeReference<List<SessionCaseCodeMapping>>() {}), SessionCaseCodeMapping::getCcdKey);
        }

    }

    public SessionCaseCodeMapping getSessionMappingsByCcdKey(String ccdKey) {
        return sessionCaseCodeMappingMap.get(ccdKey);
    }

    public int getDuration(String ccdKey) {
        if (sessionCaseCodeMappingMap.containsKey(ccdKey)) {
            return returnDurationInt(sessionCaseCodeMappingMap.get(ccdKey).getDurationFaceToFace());
        } else {
            return 0;
        }
    }

    private int returnDurationInt(String durationFaceToFace) {
        int duration = 0;
        if (!durationFaceToFace.isBlank() && durationFaceToFace.contains(MINUTES)) {
            duration = Integer.parseInt(StringUtils.strip(durationFaceToFace, MINUTES).trim());
        }
        return duration;
    }

    public List<String> getPanelMembers(String ccdKey) {
        return Stream.of(sessionCaseCodeMappingMap.get(ccdKey).getPanelMembers().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public Map<String, SessionCaseCodeMapping> getSessionCaseCodeMappingMap() {
        return sessionCaseCodeMappingMap;
    }

    public int testGet() {
        return 1;
    }


}

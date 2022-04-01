package uk.gov.hmcts.reform.sscs.service;

import static com.google.common.collect.Maps.newHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.exception.SessionDetailsLookUpException;
import uk.gov.hmcts.reform.sscs.model.SessionCaseCodeMapping;

@Service
@Slf4j
public class SessionLookupService {

    private static final String MAPPING_FILE_PATH = "reference-data/sscs-session-mapping.json";
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
            sessionCaseCodeMappingMap =
                    mapper.readValue(inputStream, new TypeReference<Map<String,SessionCaseCodeMapping>>(){});
        }

    }

    public SessionCaseCodeMapping getSessionMappingsByCcdKey(String ccdKey) {
        return sessionCaseCodeMappingMap.get(ccdKey);
    }

    public int getDuration(String ccdKey) {
        return returnDurationInt(sessionCaseCodeMappingMap.get(ccdKey).getDurationFaceToFace());
    }

    private int returnDurationInt(String durationFaceToFace) {
        return Integer.parseInt(StringUtils.strip(durationFaceToFace,"minutes").trim());
    }

    public String getPanelMembers(String ccdKey) {
        return sessionCaseCodeMappingMap.get(ccdKey).getPanelMembers();

    }


}

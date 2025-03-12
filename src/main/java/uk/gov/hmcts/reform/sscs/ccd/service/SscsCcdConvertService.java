package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.exception.CcdDeserializationException;

@Service
@Slf4j
public class SscsCcdConvertService {

    private static final Logger LOG = getLogger(SscsCcdConvertService.class);

    private Map<String, Object> hmctsServiceIdMap = new HashMap<>() {
        {
            put("HMCTSServiceId", "BBA3");
        }
    };

    private Map<String, Map<String, Object>> supplementaryDataRequestMap = new HashMap<>() {
        {
            put("$set", hmctsServiceIdMap);
        }
    };

    public CaseDataContent getCaseDataContent(SscsCaseData caseData, StartEventResponse startEventResponse,
                                               String summary, String description) {
        log.info("getCaseDataContent for case id {}", caseData.getCcdCaseId());
        return CaseDataContent.builder()
                .eventToken(startEventResponse.getToken())
                .event(Event.builder()
                        .id(startEventResponse.getEventId())
                        .summary(summary)
                        .description(description)
                        .build())
                .data(caseData)
                .supplementaryDataRequest(supplementaryDataRequestMap)
                .build();
    }

    public CaseDataContent getCaseDataContent(String eventToken, String eventId, SscsCaseData caseData, String summary, 
                                               String description) {
        return CaseDataContent.builder()
                .eventToken(eventToken)
                .event(Event.builder()
                        .id(eventId)
                        .summary(summary)
                        .description(description)
                        .build())
                .data(caseData)
                .supplementaryDataRequest(supplementaryDataRequestMap)
                .build();
    }

    public SscsCaseDetails getCaseDetails(StartEventResponse startEventResponse) {
        SscsCaseDetails sscsCaseDetails = getCaseDetails(startEventResponse.getCaseDetails());
        sscsCaseDetails.setEventToken(startEventResponse.getToken());
        sscsCaseDetails.setEventId(startEventResponse.getEventId());
        return sscsCaseDetails;
    }

    public SscsCaseDetails getCaseDetails(CaseDetails caseDetails) {
        log.info("getCaseDetails for id {}", caseDetails.getId());
        if (caseDetails.getId() != null) {
            if (caseDetails.getData() == null) {
                caseDetails = caseDetails.toBuilder().data(new HashMap<>()).build();
            }
            caseDetails.getData().put("ccdCaseId", caseDetails.getId().toString());
        }
        return SscsCaseDetails.builder()
                .id(caseDetails.getId())
                .jurisdiction(caseDetails.getJurisdiction())
                .caseTypeId(caseDetails.getCaseTypeId())
                .createdDate(caseDetails.getCreatedDate())
                .lastModified(caseDetails.getLastModified())
                .state(caseDetails.getState())
                .lockedBy(caseDetails.getLockedBy())
                .securityLevel(caseDetails.getSecurityLevel())
                .data(getCaseData(caseDetails.getData()))
                .securityClassification(caseDetails.getSecurityClassification())
                .callbackResponseStatus(caseDetails.getCallbackResponseStatus())
                .build();
    }

    public CaseDetails getCaseDetails(SscsCaseDetails sscsCaseDetails) {
        if (sscsCaseDetails.getId() != null) {
            if (sscsCaseDetails.getData() == null) {
                sscsCaseDetails = sscsCaseDetails.toBuilder()
                        .data(SscsCaseData.builder().build()).build();
            }
            sscsCaseDetails.getData().setCcdCaseId(sscsCaseDetails.getId().toString());
        }
        return CaseDetails.builder()
                .id(sscsCaseDetails.getId())
                .jurisdiction(sscsCaseDetails.getJurisdiction())
                .caseTypeId(sscsCaseDetails.getCaseTypeId())
                .createdDate(sscsCaseDetails.getCreatedDate())
                .lastModified(sscsCaseDetails.getLastModified())
                .state(sscsCaseDetails.getState())
                .lockedBy(sscsCaseDetails.getLockedBy())
                .securityLevel(sscsCaseDetails.getSecurityLevel())
                .data(getCaseDataMap(sscsCaseDetails.getData()))
                .securityClassification(sscsCaseDetails.getSecurityClassification())
                .callbackResponseStatus(sscsCaseDetails.getCallbackResponseStatus())
                .build();
    }

    public SscsCaseData getCaseData(Map<String, Object> dataMap) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.findAndRegisterModules();
        try {
            SscsCaseData sscsCaseData = mapper.convertValue(dataMap, SscsCaseData.class);
            if (hasAppellantIdentify(sscsCaseData)) {
                sscsCaseData.getAppeal().getAppellant().getIdentity().setNino(
                    normaliseNino(sscsCaseData.getAppeal().getAppellant().getIdentity().getNino())
                );
                sscsCaseData.sortCollections();
            }
            return sscsCaseData;
        } catch (Exception e) {
            CcdDeserializationException ccdDeserializationException = new CcdDeserializationException(e);
            LOG.error("Error occurred when SscsCaseDetails are mapped into SscsCaseData", ccdDeserializationException);
            throw ccdDeserializationException;
        }
    }

    public Map<String, Object> getCaseDataMap(SscsCaseData caseData) {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .convertValue(caseData, new TypeReference<>(){});
    }

    public static boolean hasAppellantIdentify(SscsCaseData sscsCaseData) {
        return sscsCaseData != null
            && sscsCaseData.getAppeal() != null
            && sscsCaseData.getAppeal().getAppellant() != null
            && sscsCaseData.getAppeal().getAppellant().getIdentity() != null;
    }

    public static String normaliseNino(String unclean) {
        return StringUtils.isEmpty(unclean) ? unclean : unclean.replaceAll("\\s", "").toUpperCase();
    }
}

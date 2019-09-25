package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
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
public class SscsCcdConvertService {

    private static final Logger LOG = getLogger(SscsCcdConvertService.class);

    public CaseDataContent getCaseDataContent(SscsCaseData caseData, StartEventResponse startEventResponse,
                                               String summary, String description) {
        return CaseDataContent.builder()
                .eventToken(startEventResponse.getToken())
                .event(Event.builder()
                        .id(startEventResponse.getEventId())
                        .summary(summary)
                        .description(description)
                        .build())
                .data(caseData)
                .build();
    }

    public SscsCaseDetails getCaseDetails(CaseDetails caseDetails) {
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

    public SscsCaseData getCaseData(Map<String, Object> dataMap) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.registerModule(new JavaTimeModule());
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

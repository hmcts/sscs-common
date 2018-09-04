package uk.gov.hmcts.reform.sscs.ccd;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
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
class SscsCcdConvertService {

    private static final Logger LOG = getLogger(SscsCcdConvertService.class);

    CaseDataContent getCaseDataContent(SscsCaseData caseData, StartEventResponse startEventResponse,
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

    SscsCaseDetails getCaseDetails(CaseDetails caseDetails) {
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

    private SscsCaseData getCaseData(Map<String, Object> dataMap) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        try {
            return mapper.convertValue(dataMap, SscsCaseData.class);
        } catch (Exception e) {
            CcdDeserializationException ccdDeserializationException = new CcdDeserializationException(e);
            LOG.error("Error occurred when SscsCaseDetails are mapped into SscsCaseData", ccdDeserializationException);
            throw ccdDeserializationException;
        }
    }
}

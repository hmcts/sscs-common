package uk.gov.hmcts.reform.sscs.ccd;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;

public class CcdUtil {

    private static final Logger LOG = getLogger(CcdUtil.class);

    private CcdUtil() {

    }

    public static SscsCaseData getCaseData(Map<String, Object> dataMap) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        try {
            return mapper.convertValue(dataMap, SscsCaseData.class);
        } catch (Exception e) {
            CcdDeserializationError ccdDeserializationError = new CcdDeserializationError(e);
            LOG.error("Error occurred when SscsCaseDetails are mapped into SscsCaseData", ccdDeserializationError);
            throw ccdDeserializationError;
        }
    }

    public static SscsCaseDetails getCaseDetails(uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails) {
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
}



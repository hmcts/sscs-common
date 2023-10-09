package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.ccd.client.model.Classification;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SscsCaseDetails {
    private Long id;
    private String eventId;
    private String eventToken;
    private String jurisdiction;
    private String caseTypeId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
    private String state;
    private Integer lockedBy;
    private Integer securityLevel;
    private SscsCaseData data;
    private Classification securityClassification;
    private String callbackResponseStatus;
}

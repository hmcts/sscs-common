package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SendToFirstTier {
    @CCD(label = "Decision", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_actionSendToFirstTier")
    private SendToFirstTierActions action;
    @CCD(
            label = "Decision Document",
            hint = "All documents must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document,
            typeParameterOverride = "FL_actionSendToFirstTier"
    )
    private DocumentLink decisionDocument;
}
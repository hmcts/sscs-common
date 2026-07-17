package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DwpResponseDocument {
    @CCD(
            label = "Secretary of State's response",
            showCondition = "documentFileName=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.Label
    )
    private String dwpResponseDocumentTitle;

    @CCD(
            label = "HM Revenue and Customs",
            showCondition = "documentFileName=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.Label
    )
    private String hmrcResponseDocumentTitle;

    @CCD(
            label = "Infected Blood Compensation Authority's response",
            showCondition = "documentFileName=\"AnyValueToFailThisCondition\"",
            typeOverride = FieldType.Label
    )
    private String ibcaResponseDocumentTitle;

    private DocumentLink documentLink;
    private String documentFileName;

    @JsonCreator
    public DwpResponseDocument(@JsonProperty("dwpResponseDocumentTitle") String dwpResponseDocumentTitle,
                               @JsonProperty("hmrcResponseDocumentTitle") String hmrcResponseDocumentTitle,
                               @JsonProperty("ibcaResponseDocumentTitle") String ibcaResponseDocumentTitle,
                               @JsonProperty("documentLink") DocumentLink documentLink,
                               @JsonProperty("documentFileName") String documentFileName) {
        this.dwpResponseDocumentTitle = dwpResponseDocumentTitle;
        this.hmrcResponseDocumentTitle = hmrcResponseDocumentTitle;
        this.ibcaResponseDocumentTitle = ibcaResponseDocumentTitle;
        this.documentLink = documentLink;
        this.documentFileName = documentFileName;
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentGeneration {
    @JsonProperty("generateNotice")
    private YesNo generateNotice;
    @JsonProperty("correctionGenerateNotice")
    private YesNo correctionGenerateNotice;
    @JsonProperty("statementOfReasonsGenerateNotice")
    private YesNo statementOfReasonsGenerateNotice;
    @JsonProperty("libertyToApplyGenerateNotice")
    private YesNo libertyToApplyGenerateNotice;
    @JsonProperty("permissionToAppealGenerateNotice")
    private YesNo permissionToAppealGenerateNotice;
    @JsonProperty("bodyContent")
    private String bodyContent;
    @JsonProperty("correctionBodyContent")
    private String correctionBodyContent;
    @JsonProperty("statementOfReasonsBodyContent")
    private String statementOfReasonsBodyContent;
    @JsonProperty("libertyToApplyBodyContent")
    private String libertyToApplyBodyContent;
    @JsonProperty("permissionToAppealBodyContent")
    private String permissionToAppealBodyContent;
    @JsonProperty("directionNoticeContent")
    private String directionNoticeContent;
    @JsonProperty("signedBy")
    private String signedBy;
    @JsonProperty("signedRole")
    private String signedRole;
    @JsonProperty("correctionSignedBy")
    private String correctionSignedBy;
    @JsonProperty("correctionSignedRole")
    private String correctionSignedRole;
    @JsonProperty("statementOfReasonsSignedBy")
    private String statementOfReasonsSignedBy;
    @JsonProperty("statementOfReasonsSignedRole")
    private String statementOfReasonsSignedRole;
    @JsonProperty("libertyToApplySignedBy")
    private String libertyToApplySignedBy;
    @JsonProperty("libertyToApplySignedRole")
    private String libertyToApplySignedRole;
}

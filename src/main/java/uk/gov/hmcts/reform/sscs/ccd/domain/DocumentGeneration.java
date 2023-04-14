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
    @JsonProperty("bodyContent")
    private String bodyContent;
    @JsonProperty("bodyContentCorrection")
    private String bodyContentCorrection;
    @JsonProperty("directionNoticeContent")
    private String directionNoticeContent;
    @JsonProperty("signedBy")
    private String signedBy;
    @JsonProperty("signedRole")
    private String signedRole;
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.util.Objects.nonNull;

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
    @JsonProperty("directionNoticeContent")
    private String directionNoticeContent;
    @JsonProperty("signedBy")
    private String signedBy;
    @JsonProperty("signedRole")
    private String signedRole;
    @JsonProperty("correctionGenerateNotice")
    private YesNo correctionGenerateNotice;
    @JsonProperty("correctionBodyContent")
    private String correctionBodyContent;

    @SuppressWarnings("unused")
    @JsonIgnore
    public YesNo getGenerateNotice() {
        if (nonNull(correctionGenerateNotice)) {
            bodyContent = correctionBodyContent;
            return correctionGenerateNotice;
        }

        return generateNotice;
    }
}

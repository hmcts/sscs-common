package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "doc", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
public class DocumentDetails {
    @CCD(label = "Date Received", typeOverride = FieldType.Date)
    private String dateReceived;
    @CCD(label = "Evidence Type")
    private String evidenceType;
    @CCD(label = "Evidence Provided By")
    private String evidenceProvidedBy;

    @JsonCreator
    public DocumentDetails(@JsonProperty("dateReceived") String dateReceived,
                           @JsonProperty("evidenceType") String evidenceType,
                           @JsonProperty("evidenceProvidedBy") String evidenceProvidedBy) {
        this.dateReceived = dateReceived;
        this.evidenceType = evidenceType;
        this.evidenceProvidedBy = evidenceProvidedBy;
    }

    @JsonIgnore
    public LocalDate getEvidenceDateTimeFormatted() {
        try {
            if (StringUtils.isEmpty(dateReceived)) {
                return null;
            }
            return LocalDate.parse(dateReceived);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudioVideoEvidenceDetails {
    private String documentType;
    private DocumentLink documentLink;
    private DocumentLink surveillanceDocument;
    private String fileName;
    private LocalDate dateAdded;

    @JsonCreator
    public AudioVideoEvidenceDetails(@JsonProperty("documentType") String documentType,
                                     @JsonProperty("documentLink") DocumentLink documentLink,
                                     @JsonProperty("surveillanceDocument") DocumentLink surveillanceDocument,
                                     @JsonProperty("fileName") String fileName,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                                @JsonSerialize(using = LocalDateSerializer.class)
                                                @JsonProperty("dateAdded") LocalDate dateAdded) {
        this.documentType = documentType;
        this.documentLink = documentLink;
        this.surveillanceDocument = surveillanceDocument;
        this.fileName = fileName;
        this.dateAdded = dateAdded;
    }

}

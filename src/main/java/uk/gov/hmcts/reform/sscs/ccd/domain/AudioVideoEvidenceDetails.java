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
    private DocumentLink documentLink;
    private DocumentLink rip1Document;
    private String fileName;
    private LocalDate dateAdded;
    private LocalDate dateApproved;
    private UploadParty partyUploaded;

    @JsonCreator
    public AudioVideoEvidenceDetails(@JsonProperty("documentLink") DocumentLink documentLink,
                                     @JsonProperty("rip1Document") DocumentLink rip1Document,
                                     @JsonProperty("fileName") String fileName,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                                @JsonSerialize(using = LocalDateSerializer.class)
                                                @JsonProperty("dateAdded") LocalDate dateAdded,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                         @JsonSerialize(using = LocalDateSerializer.class)
                                         @JsonProperty("dateApproved") LocalDate dateApproved,
                                     @JsonProperty("partyUploaded") UploadParty partyUploaded) {
        this.documentLink = documentLink;
        this.rip1Document = rip1Document;
        this.fileName = fileName;
        this.dateAdded = dateAdded;
        this.dateApproved = dateApproved;
        this.partyUploaded = partyUploaded;
    }

}

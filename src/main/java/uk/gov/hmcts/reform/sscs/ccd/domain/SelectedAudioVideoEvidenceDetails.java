package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class SelectedAudioVideoEvidenceDetails {
    private String documentType;
    private DocumentLink documentLink;
    private DocumentLink rip1Document;
    private LocalDate dateAdded;
    private String partyUploaded;

    @JsonCreator
    public SelectedAudioVideoEvidenceDetails(@JsonProperty("documentType") String documentType,
                      @JsonProperty("documentLink") DocumentLink documentLink,
                      @JsonProperty("rip1Document") DocumentLink rip1Document,
                      @JsonProperty("dateAdded") LocalDate dateAdded,
                      @JsonProperty("partyUploaded") String partyUploaded) {
        this.documentType = documentType;
        this.documentLink = documentLink;
        this.rip1Document = rip1Document;
        this.dateAdded = dateAdded;
        this.partyUploaded = partyUploaded;
    }
}

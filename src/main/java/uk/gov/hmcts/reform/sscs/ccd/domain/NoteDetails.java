package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteDetails {
    String noteDate;
    String author;
    String noteDetail;

    @JsonCreator
    public NoteDetails(@JsonProperty("noteDate") String noteDate,
                       @JsonProperty("author") String author,
                       @JsonProperty("noteDetail") String noteDetail) {
        this.noteDate = noteDate;
        this.author = author;
        this.noteDetail = noteDetail;
    }
}

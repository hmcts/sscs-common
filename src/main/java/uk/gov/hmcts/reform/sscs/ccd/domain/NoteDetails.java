package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "appealNote", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteDetails {
    @CCD(label = "Date of note", typeOverride = FieldType.Date)
    String noteDate;
    @CCD(label = "Author")
    String author;
    @CCD(label = "Note", typeOverride = FieldType.TextArea)
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

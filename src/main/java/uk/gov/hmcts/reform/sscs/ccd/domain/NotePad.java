package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotePad {
    @CCD(label = "Note pad", typeOverride = FieldType.Collection, typeParameterOverride = "appealNote")
    private List<Note> notesCollection;

    @JsonCreator
    public NotePad(@JsonProperty("notesCollection") List<Note> notesCollection) {
        this.notesCollection = notesCollection;
    }
}

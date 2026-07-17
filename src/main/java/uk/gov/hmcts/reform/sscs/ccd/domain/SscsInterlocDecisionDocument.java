package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsInterlocDecisionDocument {

    @CCD(label = "Type", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_InterlocDecision")
    private String documentType;
    @CCD(label = "File name")
    private String documentFileName;
    @CCD(label = "Date added")
    private LocalDate documentDateAdded;
    @CCD(
            label = "Document Url",
            hint = "All documents must be PDF formatted",
            regex = ".pdf",
            typeOverride = FieldType.Document
    )
    private DocumentLink documentLink;

    @JsonCreator
    public SscsInterlocDecisionDocument(@JsonProperty("documentType") String documentType,
                                    @JsonProperty("documentFileName") String documentFileName,
                                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                        @JsonSerialize(using = LocalDateSerializer.class)
                                    @JsonProperty("documentDateAdded") LocalDate documentDateAdded,
                                    @JsonProperty("documentLink") DocumentLink documentLink) {
        this.documentType = documentType;
        this.documentFileName = documentFileName;
        this.documentDateAdded = documentDateAdded;
        this.documentLink = documentLink;
    }
}

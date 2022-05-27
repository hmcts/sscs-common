package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ListOfValue {
    @JsonIgnore
    private String id;
    private String key;
    private String value;
}

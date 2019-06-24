package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DynamicListItem {

    @JsonProperty("code")
    private String code;

    @JsonProperty("label")
    private String label;

    public DynamicListItem(@JsonProperty("code") String code, @JsonProperty("label") String label) {
        this.code = code;
        this.label = label;
    }
}

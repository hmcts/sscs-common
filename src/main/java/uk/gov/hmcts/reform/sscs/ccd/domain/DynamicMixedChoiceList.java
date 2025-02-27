package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicMixedChoiceList {

    @JsonProperty("value")
    private List<DynamicListItem> value;

    @JsonProperty("list_items")
    private List<DynamicListItem> listItems;

    public DynamicMixedChoiceList(@JsonProperty("value") List<DynamicListItem> value,
                                  @JsonProperty("list_items") List<DynamicListItem> listItems) {
        this.value = value;
        this.listItems = listItems;
    }
}
package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicList {

    @JsonProperty("value")
    private DynamicListItem value;

    @JsonProperty("list_items")
    private List<DynamicListItem> listItems;

    private String urlValue;

    public DynamicList(@JsonProperty("value") DynamicListItem value,
                       @JsonProperty("list_items") List<DynamicListItem> listItems) {
        this.value = value;
        this.listItems = listItems;
    }

    public DynamicList(String urlValue) {
        this.urlValue = urlValue;
    }
}
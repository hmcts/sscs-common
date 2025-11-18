package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class DynamicRadioList {

    private DynamicRadioListElement value;

    @JsonProperty("list_items")
    private List<DynamicRadioListElement> listItems;

}

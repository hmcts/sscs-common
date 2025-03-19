package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanelResponse {

    @JsonProperty("list_of_values")
    private List<DefaultPanelCategory> listOfValues;

}


package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Categories {

    @JsonProperty("list_of_values")
    private List<Category> listOfCategory;
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedSscsCaseData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private YesNo selectNextHmcHearingType;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DynamicList features;
    
    public void enableFeature(String feature) {

        if (features == null || features.getListItems() == null || features.getListItems().isEmpty()) {
            // List.of is immutable - need to review this re. adding/removing items
            features = new DynamicList(null, List.of(new DynamicListItem(feature, feature)));
        } else {
            features.getListItems().add(new DynamicListItem(feature, feature));
        }
    }
    
    public void disableFeature(String feature) {
        if (features == null || features.getListItems() == null) {return;}
        features.getListItems().removeIf(item -> item.getCode().equals(feature));
    }
    
    public boolean isFeatureEnabled(String feature) {
        if (features == null || features.getListItems() == null) {return false;}
        return features.getListItems().stream().anyMatch(item -> item.getCode().equals(feature));
    }
}

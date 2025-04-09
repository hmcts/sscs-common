package uk.gov.hmcts.reform.sscs.reference.data.service;

import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;

@Getter
@Setter
@Component
public class PanelCategoryService {
    private static final String JSON_DATA_LOCATION = "reference-data/panel-category-map.json";
    private List<PanelCategory> panelCategories;

    public PanelCategoryService() {
        panelCategories = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
    }

    public PanelCategory getPanelCategory(String benefitIssueCode, String specialism, String fqpm) {
        PanelCategory panelCategorySelection = new PanelCategory(benefitIssueCode, specialism, fqpm);
        return panelCategories.stream()
                .filter(panelCategorySelection::equals)
                .findFirst().orElse(null);
    }
}

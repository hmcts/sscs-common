package uk.gov.hmcts.reform.sscs.reference.data.service;

import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.generateHashMap;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getReferenceData;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategoryMap;

@Getter
@Setter
@Component
public class PanelCategoryMapService {
    private static final String JSON_DATA_LOCATION = "reference-data/panel-category-map.json";
    private List<PanelCategoryMap> panelCategoryMaps;
    private Map<PanelCategoryMap, PanelCategoryMap> panelCategoryHashMap;
    public PanelCategoryMapService() {
        panelCategoryMaps = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {
        });
        panelCategoryHashMap = generateHashMap(panelCategoryMaps);
    }

    public PanelCategoryMap getPanelCategoryMap(String benefitIssueCode, String category, String specialism, String fqpm) {
        return panelCategoryHashMap.get(new PanelCategoryMap(benefitIssueCode, category, specialism, fqpm));
    }
}

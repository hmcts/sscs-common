package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.CaseUtils;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkAllocationFields {
    private String caseNameHmctsInternal;
    private String caseNameHmctsRestricted;
    private String caseNamePublic;
    private String ogdType;
    private DynamicList caseManagementCategory;
    @JsonProperty("CaseAccessCategory")
    private String caseAccessCategory;
    @JsonInclude
    private String addedDocuments;

    public void setCaseNames(String caseName) {
        caseNameHmctsInternal = caseName;
        caseNameHmctsRestricted = caseName;
        caseNamePublic = caseName;
    }

    public void setCategories(Benefit benefit) {
        DynamicListItem caseManagementCategoryItem = new DynamicListItem(benefit.getShortName(), benefit.getDescription());
        List<DynamicListItem> listItems = Arrays.asList(caseManagementCategoryItem);
        caseAccessCategory = CaseUtils.toCamelCase(benefit.getDescription(), false, ' ');
        caseManagementCategory = new DynamicList(caseManagementCategoryItem, listItems);

    }
}

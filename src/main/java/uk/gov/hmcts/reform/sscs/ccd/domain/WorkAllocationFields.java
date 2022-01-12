package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private String caseAccessCategory;

    public void setCaseNames(String caseName) {
        caseNameHmctsInternal = caseName;
        caseNameHmctsRestricted = caseName;
        caseNamePublic = caseName;
    }

    public void setCategorys(Benefit benefit) {
        DynamicListItem caseManagementCategoryItem = new DynamicListItem(benefit.getShortName(), benefit.getDescription());
        List<DynamicListItem> listItems = Arrays.asList(caseManagementCategoryItem);
        caseAccessCategory = benefit.getDescription();
        caseManagementCategory = new DynamicList(caseManagementCategoryItem, listItems);

    }
}

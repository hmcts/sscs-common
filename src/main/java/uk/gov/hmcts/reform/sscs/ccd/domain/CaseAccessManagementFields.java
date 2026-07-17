package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.CaseUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.GSProfileRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.CaseworkerWaTaskConfigurationRAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.HmrcresponsewriterCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseAccessManagementFields {
    @CCD(label = "Case name HMCTS internal", access = {SscsCitizenCrudAccess.class, GSProfileRAccess.class})
    private String caseNameHmctsInternal;
    @CCD(label = "Case name HMCTS restricted", access = {SscsCitizenCrudAccess.class})
    private String caseNameHmctsRestricted;
    @CCD(label = "Case name public", access = {SscsCitizenCrudAccess.class, CaseworkerWaTaskConfigurationRAccess.class})
    private String caseNamePublic;
    @CCD(label = "OGD type", access = {SscsCitizenCrudAccess.class})
    private String ogdType;
    @CCD(
            label = "Case management category",
            typeOverride = FieldType.DynamicList,
            access = {SscsCitizenCrudAccess.class, GSProfileRAccess.class, CaseworkerWaTaskConfigurationRAccess.class}
    )
    private DynamicList caseManagementCategory;
    @CCD(label = "Case access category", access = {SscsCitizenCrudAccess.class, HmrcresponsewriterCrudAccess.class})
    @JsonProperty("CaseAccessCategory")
    private String caseAccessCategory;

    public void setCaseNames(String caseName) {
        caseNameHmctsInternal = caseName;
        caseNameHmctsRestricted = caseName;
        caseNamePublic = caseName;
    }

    public void setCategories(Benefit benefit) {
        if (nonNull(benefit)) {
            DynamicListItem caseManagementCategoryItem = new DynamicListItem(benefit.getShortName(), benefit.getDescription());
            List<DynamicListItem> listItems = List.of(caseManagementCategoryItem);
            caseAccessCategory = CaseUtils.toCamelCase(benefit.getDescription(), false, ' ');
            caseManagementCategory = new DynamicList(caseManagementCategoryItem, listItems);
        }
    }
}

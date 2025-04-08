package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategory;
import uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryService;

public class PanelCategoryServiceTest {

    private final PanelCategoryService panelCategoryService = new PanelCategoryService();

    @Test
    @DisplayName("Valid call to getPanelCategoryMap should return correct johTier")
    public void getPanelCategoryMap(){
        PanelCategory result = panelCategoryService.getPanelCategoryMap("001AD", null, null);
        assertThat(result).isNotNull();
        assertThat(result.getBenefitIssueCode()).isEqualTo("001AD");
        assertThat(result.getJohTiers()).isNotEmpty();
        assertThat(result.getJohTiers().get(0)).isEqualTo("84");
    }

    @Test
    @DisplayName("Invalid call to getPanelCategoryMap should return null")
    public void getPanelCategoryMapWithInvalidParameters() {
        PanelCategory result = panelCategoryService.getPanelCategoryMap(null, null, null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Call to getPanelCategoryMap with FQPM should return correct johTier")
    public void getPanelCategoryMapWithFQPM() {
        PanelCategory result = panelCategoryService.getPanelCategoryMap("016CC", null, "true");
        assertThat(result).isNotNull();
        assertThat(result.getJohTiers().stream().anyMatch("50"::equals)).isTrue();
    }

    @Test
    @DisplayName("Call to getPanelCategoryMap with specialism should return correct johTier")
    public void getPanelCategoryMapWithSpecialism() {
        PanelCategory oneSpecialismResult = panelCategoryService.getPanelCategoryMap("067CB", "1", null);
        PanelCategory twoSpecialismResult = panelCategoryService.getPanelCategoryMap("067CB", "2", null);
        assertThat(oneSpecialismResult).isNotNull();
        assertThat(twoSpecialismResult).isNotNull();
        assertThat(oneSpecialismResult.getJohTiers().stream().filter("58"::equals).count()).isEqualTo(1);
        assertThat(twoSpecialismResult.getJohTiers().stream().filter("58"::equals).count()).isEqualTo(2);
    }
}

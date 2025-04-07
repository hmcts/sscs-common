package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategoryMap;
import uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryMapService;

@RunWith(JUnitParamsRunner.class)
public class PanelCategoryMapServiceTest {

    private final PanelCategoryMapService panelCategoryMapService = new PanelCategoryMapService();

    @Test
    @DisplayName("Valid call to getPanelCategoryMap should return correct johTier")
    public void getPanelCategoryMap(){
        PanelCategoryMap result = panelCategoryMapService.getPanelCategoryMap("001AD", null, null);
        assertThat(result).isNotNull();
        assertThat(result.getBenefitIssueCode()).isEqualTo("001AD");
        assertThat(result.getJohTiers()).isNotEmpty();
        assertThat(result.getJohTiers().get(0)).isEqualTo("84");
    }

    @Test
    @DisplayName("Invalid call to getPanelCategoryMap should return null")
    public void getPanelCategoryMapWithInvalidParameters() {
        PanelCategoryMap result = panelCategoryMapService.getPanelCategoryMap(null, null, null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Call to getPanelCategoryMap with FQPM should return correct johTier")
    public void getPanelCategoryMapWithFQPM() {
        PanelCategoryMap result = panelCategoryMapService.getPanelCategoryMap("016CC", null, "true");
        assertThat(result).isNotNull();
        assertThat(result.getJohTiers().stream().anyMatch("50"::equals)).isTrue();
    }

    @Test
    @DisplayName("Call to getPanelCategoryMap with specialism should return correct johTier")
    public void getPanelCategoryMapWithSpecialism() {
        PanelCategoryMap oneSpecialismResult = panelCategoryMapService.getPanelCategoryMap("067CB", "1", null);
        PanelCategoryMap twoSpecialismResult = panelCategoryMapService.getPanelCategoryMap("067CB", "2", null);
        assertThat(oneSpecialismResult).isNotNull();
        assertThat(twoSpecialismResult).isNotNull();
        assertThat(oneSpecialismResult.getJohTiers().stream().filter("58"::equals).count()).isEqualTo(1);
        assertThat(twoSpecialismResult.getJohTiers().stream().filter("58"::equals).count()).isEqualTo(2);
    }
}

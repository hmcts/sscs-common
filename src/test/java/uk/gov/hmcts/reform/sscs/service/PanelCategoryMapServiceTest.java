package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.sscs.reference.data.model.PanelCategoryMap;
import uk.gov.hmcts.reform.sscs.reference.data.service.PanelCategoryMapService;

@RunWith(JUnitParamsRunner.class)
public class PanelCategoryMapServiceTest {

    private final PanelCategoryMapService panelCategoryMapService = new PanelCategoryMapService();

    @Test
    public void getPanelCategoryMap(){
        PanelCategoryMap result = panelCategoryMapService.getPanelCategoryMap("001AD", "1", null, null);
        assertThat(result).isNotNull();
    }
}

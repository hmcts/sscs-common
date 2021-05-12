package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PanelCompositionTest {

    @Test
    @Parameters(
            {"JUDGE_DOCTOR_AND_DISABILITY_EXPERT, judge\\, doctor and disability expert, barnwr\\, meddyg ac arbenigwr anableddau",
            "JUDGE_AND_A_DOCTOR, judge and a doctor, barnwr a meddyg (os yw’n berthnasol)",
            "JUDGE, judge, barnwr",
            "JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE, judge\\, doctor and disability expert (if applicable), barnwr a meddyg (os yw’n berthnasol)"}
    )
    public void assertAllValues(PanelComposition panelComposition, String english, String welsh) {
        assertThat(panelComposition.getEnglish(), is(english));
        assertThat(panelComposition.getWelsh(), is(welsh));
    }

}
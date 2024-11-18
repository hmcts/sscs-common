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
    @Parameters({
        "JUDGE_DOCTOR_AND_DISABILITY_EXPERT, judge\\, doctor and disability expert, barnwr\\, meddyg ac arbenigwr anableddau",
        "JUDGE_AND_A_DOCTOR, judge and a doctor, barnwr a meddyg",
        "JUDGE, judge, barnwr",
        "JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE, judge\\, doctor and disability expert (if applicable), barnwr\\, meddyg ac arbenigwr anabledd (os yw’n berthnasol)",
        "JUDGE_AND_ONE_OR_TWO_DOCTORS, judge and 1 or 2 doctors, barnwr ac 1 neu 2 feddyg",
        "JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER, judge and Financially Qualified Panel Member (if applicable), Barnwr ac Aelod Panel sydd â chymhwyster i ddelio gyda materion Ariannol (os yw’n berthnasol)",
        "IBC_PANEL_COMPOSITION, judge and if applicable a medical member and/or a qualified tribunal member, barnwr ac os yw’n berthnasol\\, aelod meddygol ac/neu aelod tribiwnlys cymwys"}
    )
    public void assertAllValues(PanelComposition panelComposition, String english, String welsh) {
        assertThat(panelComposition.getEnglish(), is(english));
        assertThat(panelComposition.getWelsh(), is(welsh));
    }

}
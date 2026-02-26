package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;

@Getter
public enum PanelComposition {
    JUDGE_DOCTOR_AND_DISABILITY_EXPERT("judge, doctor and disability expert", "barnwr, meddyg ac arbenigwr anableddau"),
    JUDGE_AND_A_DOCTOR("judge and a doctor", "barnwr a meddyg"),
    JUDGE_AND_DOCTOR_IF_APPLICABLE("judge and doctor (if applicable)", "barnwr a meddyg (os yw’n berthnasol)"),
    JUDGE("judge", "barnwr"),
    JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE("judge, doctor and disability expert (if applicable)", "barnwr, meddyg ac arbenigwr anabledd (os yw’n berthnasol)"),
    JUDGE_AND_ONE_OR_TWO_DOCTORS("judge and 1 or 2 doctors", "barnwr ac 1 neu 2 feddyg"),
    JUDGE_AND_FINANCIALLY_QUALIFIED_PANEL_MEMBER("judge and Financially Qualified Panel Member (if applicable)", "Barnwr ac Aelod Panel sydd â chymhwyster i ddelio gyda materion Ariannol (os yw’n berthnasol)"),
    IBC_PANEL_COMPOSITION("judge and if applicable a medical member and/or a financially qualified tribunal member", "barnwr ac os yw’n berthnasol aelod meddygol a/neu aelod o’r tribiwnlys sy’n gymwys mewn materion ariannol");

    private final String english;
    private final String welsh;

    PanelComposition(String english, String welsh) {
        this.english = english;
        this.welsh = welsh;
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Getter;

@Getter
public enum PanelComposition {
    JUDGE_DOCTOR_AND_DISABILITY_EXPERT("judge, doctor and disability expert", "barnwr, meddyg ac arbenigwr anableddau"),
    JUDGE_AND_A_DOCTOR("judge and a doctor", "barnwr a meddyg (os yw’n berthnasol)"),
    JUDGE("judge", "barnwr"),
    JUDGE_DOCTOR_AND_DISABILITY_EXPERT_IF_APPLICABLE("judge, doctor and disability expert (if applicable)", "barnwr a meddyg (os yw’n berthnasol)");

    private final String english;
    private final String welsh;

    PanelComposition(String english, String welsh) {
        this.english = english;
        this.welsh = welsh;
    }
}

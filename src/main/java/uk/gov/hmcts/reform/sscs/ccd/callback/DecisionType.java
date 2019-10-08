package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DecisionType {

    STRIKE_OUT("strikeOut");

    private String value;

    DecisionType(String value) {
        this.value = value;
    }
}
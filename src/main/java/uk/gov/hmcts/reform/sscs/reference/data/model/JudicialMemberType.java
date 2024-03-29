package uk.gov.hmcts.reform.sscs.reference.data.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JudicialMemberType {

    TRIBUNAL_PRESIDENT("65","President of Tribunal"),
    REGIONAL_TRIBUNAL_JUDGE("74","Regional Tribunal Judge"),
    TRIBUNAL_JUDGE("84","Tribunal Judge");

    private final String hmcReference;
    private final String descriptionEn;

    @Override
    @JsonValue
    public String toString() {
        return hmcReference;
    }

}

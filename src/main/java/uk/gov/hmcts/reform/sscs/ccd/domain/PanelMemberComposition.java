package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanelMemberComposition {

    @JsonCreator
    public PanelMemberComposition(@JsonProperty("panelCompositionJudge") String panelCompositionJudge,
                                  @JsonProperty("panelCompositionMemberMedical1") String panelCompositionMemberMedical1,
                                  @JsonProperty("panelCompositionMemberMedical2") String panelCompositionMemberMedical2,
                                  @JsonProperty("panelCompositionDisabilityAndFqMember") String panelCompositionDisabilityAndFqMember) {
        this.panelCompositionJudge = panelCompositionJudge;
        this.panelCompositionMemberMedical1 = panelCompositionMemberMedical1;
        this.panelCompositionMemberMedical2 = panelCompositionMemberMedical2;
        this.panelCompositionDisabilityAndFqMember = panelCompositionDisabilityAndFqMember;
    }

    String panelCompositionJudge;

    String panelCompositionMemberMedical1;

    String panelCompositionMemberMedical2;

    String panelCompositionDisabilityAndFqMember;

}

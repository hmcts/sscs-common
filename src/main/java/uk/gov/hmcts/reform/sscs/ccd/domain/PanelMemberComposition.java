package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PanelMemberComposition {
    private String panelCompositionJudge;
    private String panelCompositionMemberMedical1;
    private String panelCompositionMemberMedical2;
    private List<String> panelCompositionDisabilityAndFqMember;

    public PanelMemberComposition(String panelCompositionJudge, String panelCompositionMemberMedical1,
                                  String panelCompositionMemberMedical2,
                                  List<String> panelCompositionDisabilityAndFqMember) {
        this.panelCompositionJudge = panelCompositionJudge;
        this.panelCompositionMemberMedical1 = panelCompositionMemberMedical1;
        this.panelCompositionMemberMedical2 = panelCompositionMemberMedical2;
        this.panelCompositionDisabilityAndFqMember = panelCompositionDisabilityAndFqMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PanelMemberComposition that = (PanelMemberComposition) o;
        return Objects.equals(panelCompositionJudge, that.panelCompositionJudge)
                && Objects.equals(panelCompositionMemberMedical1, that.panelCompositionMemberMedical1)
                && Objects.equals(panelCompositionMemberMedical2, that.panelCompositionMemberMedical2)
                && Objects.equals(panelCompositionDisabilityAndFqMember, that.panelCompositionDisabilityAndFqMember);
    }

    @Schema(example = "84")
    @JsonProperty(value = "panelCompositionJudge")
    public String isPanelCompositionJudge() {
        return panelCompositionJudge;
    }

    @Schema(example = "58")
    @JsonProperty(value = "panelCompositionMemberMedical1")
    public String getPanelCompositionMemberMedical1() {
        return panelCompositionMemberMedical1;
    }

    @Schema(example = "58")
    @JsonProperty(value = "panelCompositionMemberMedical2")
    public String isPanelCompositionMemberMedical2() {
        return panelCompositionMemberMedical2;
    }

    @Schema(example = "44")
    @JsonProperty(value = "panelCompositionDisabilityAndFqMember")
    public List<String> getPanelCompositionDisabilityAndFqMember() {
        return panelCompositionDisabilityAndFqMember;
    }

    @Override
    public int hashCode() {
        return Objects.hash(panelCompositionJudge, panelCompositionMemberMedical1, panelCompositionMemberMedical2,
                panelCompositionDisabilityAndFqMember);
    }

    @Override
    public String toString() {
        return "HearingArrangements{"
                + "panelCompositionJudge=" + panelCompositionJudge
                + ", panelCompositionMemberMedical1='" + panelCompositionMemberMedical1 + '\''
                + ", panelCompositionMemberMedical2=" + panelCompositionMemberMedical2
                + ", panelCompositionDisabilityAndFqMember='" + panelCompositionDisabilityAndFqMember + '\''
                + '}';
    }
}

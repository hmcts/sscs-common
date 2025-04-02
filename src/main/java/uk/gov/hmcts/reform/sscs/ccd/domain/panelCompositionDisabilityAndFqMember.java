package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public class panelCompositionDisabilityAndFqMember {

    private PanelMemberType panelCompositionDisabilityMember;
    private PanelMemberType panelCompositionFqMember;


    public panelCompositionDisabilityAndFqMember(PanelMemberType panelCompositionDisabilityMember, PanelMemberType panelCompositionFqMember) {
        this.panelCompositionDisabilityMember = panelCompositionDisabilityMember;
        this.panelCompositionFqMember = panelCompositionFqMember;
    }

    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "panelCompositionDisabilityMember")
    public PanelMemberType isPanelCompositionDisabilityMember() {
        return panelCompositionDisabilityMember;
    }

    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "panelCompositionFqMember")
    public PanelMemberType isPanelCompositionFqMember() {
        return panelCompositionFqMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        panelCompositionDisabilityAndFqMember that = (panelCompositionDisabilityAndFqMember) o;
        return panelCompositionDisabilityMember == that.panelCompositionDisabilityMember
                && panelCompositionFqMember == that.panelCompositionFqMember;
    }

    @Override
    public int hashCode() {
        return Objects.hash(panelCompositionDisabilityMember, panelCompositionFqMember);
    }
}

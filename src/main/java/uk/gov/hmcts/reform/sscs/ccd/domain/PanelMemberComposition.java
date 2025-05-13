package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanelMemberComposition {


    private String panelCompositionJudge;

    private String panelCompositionMemberMedical1;

    private String panelCompositionMemberMedical2;

    private List<String> panelCompositionDisabilityAndFqMember;

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
                && new HashSet<>(panelCompositionDisabilityAndFqMember)
                .containsAll(that.panelCompositionDisabilityAndFqMember);
    }
}

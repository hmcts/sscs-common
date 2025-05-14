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


    private String panelCompJudge;

    private String panelCompMedical1;

    private String panelCompMedical2;

    private List<String> panelCompDisabilityAndFqm;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PanelMemberComposition that = (PanelMemberComposition) o;
        return Objects.equals(panelCompJudge, that.panelCompJudge)
                && Objects.equals(panelCompMedical1, that.panelCompMedical1)
                && Objects.equals(panelCompMedical2, that.panelCompMedical2)
                && new HashSet<>(panelCompDisabilityAndFqm)
                .containsAll(that.panelCompDisabilityAndFqm);
    }
}

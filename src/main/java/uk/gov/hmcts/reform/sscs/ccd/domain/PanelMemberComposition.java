package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

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

    public boolean isEmpty() {
        return Objects.isNull(panelCompositionJudge) &&
                Objects.isNull(panelCompositionMemberMedical1) &&
                Objects.isNull(panelCompositionMemberMedical2) &&
                ObjectUtils.isEmpty(panelCompositionDisabilityAndFqMember);
    }
}

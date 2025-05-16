package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import static java.util.Objects.isNull;

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

    @JsonIgnore
    public boolean isEmpty() {
        return isNull(panelCompositionJudge) &&
                isNull(panelCompositionMemberMedical1) &&
                isNull(panelCompositionMemberMedical2) &&
                ObjectUtils.isEmpty(panelCompositionDisabilityAndFqMember);
    }
}

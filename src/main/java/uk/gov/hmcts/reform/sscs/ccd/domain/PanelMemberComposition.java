package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.reference.data.model.JudicialMemberType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PanelMemberComposition {
    private JudicialMemberType panelCompositionJudge;
    private PanelMemberType panelCompositionMemberMedical1;
    private PanelMemberType panelCompositionMemberMedical2;
    private panelCompositionDisabilityAndFqMember panelCompositionDisabilityAndFqMember;

}

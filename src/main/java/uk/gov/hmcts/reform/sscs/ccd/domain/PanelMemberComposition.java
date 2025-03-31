package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.reform.sscs.reference.data.model.JudicialMemberType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class PanelMemberComposition {
    private JudicialMemberType panelCompositionJudge;
    private PanelMemberType panelCompositionMemberMedical1;
    private PanelMemberType panelCompositionMemberMedical2;
    private PanelMemberType panelCompositionMemberDisabilityAndFinanciallyQualified;
}

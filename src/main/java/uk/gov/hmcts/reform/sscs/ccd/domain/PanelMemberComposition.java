package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
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


    private List<String> panelCompositionJudge;

    private List<String> panelCompositionMemberMedical1;

    private List<String> panelCompositionMemberMedical2;

    private List<String> panelCompositionDisabilityAndFqMember;

}

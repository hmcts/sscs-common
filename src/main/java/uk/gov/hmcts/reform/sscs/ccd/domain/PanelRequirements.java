package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PanelRequirements {
    private List<String> roleType;
    private List<String> authorisationTypes;
    private List<String> authorisationSubType;
    private List<PanelPreference> panelPreferences;
    private List<String> panelSpecialisms;
}

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
    private List<CcdValue<String>> roleTypes;
    private List<CcdValue<String>> authorisationTypes;
    private List<CcdValue<String>> authorisationSubType;
    private List<CcdValue<PanelPreference>> panelPreferences;
    private List<CcdValue<String>> panelSpecialisms;
}

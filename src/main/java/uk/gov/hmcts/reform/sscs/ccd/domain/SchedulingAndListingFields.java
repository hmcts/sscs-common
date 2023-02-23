package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulingAndListingFields {
    private HearingRoute hearingRoute;
    private HearingState hearingState;
    private JudicialUserBase reservedJudge;
    @JsonProperty("panelMemberExclusions")
    private PanelMemberExclusions panelMemberExclusions;
    private OverrideFields overrideFields;
    private OverrideFields defaultListingValues;
    private List<AmendReason> amendReasons;

    @SuppressWarnings("unused")
    @JsonIgnore
    public PanelMemberExclusions getPanelMemberExclusions() {
        if (panelMemberExclusions == null) {
            panelMemberExclusions = PanelMemberExclusions.builder().build();
        }
        return panelMemberExclusions;
    }

    @SuppressWarnings("unused")
    @JsonProperty
    public void setPanelMemberExclusions(PanelMemberExclusions panelMemberExclusions) {
        this.panelMemberExclusions = panelMemberExclusions;
    }
}

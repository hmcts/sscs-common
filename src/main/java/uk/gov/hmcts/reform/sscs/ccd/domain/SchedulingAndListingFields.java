package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulingAndListingFields {
    private HearingRoute hearingRoute;
    private HearingState hearingState;
    private JudicialUserBase reservedJudge;
    @JsonProperty
    private PanelMemberExclusions panelMemberExclusions;
    private OverrideFields overrideFields;
    private OverrideFields defaultListingValues;
    private List<AmendReason> amendReasons;

    @SuppressWarnings("unused")
    @JsonIgnore
    public PanelMemberExclusions getPanelMemberExclusions() {
        if (panelMemberExclusions == null) {
            this.panelMemberExclusions = PanelMemberExclusions.builder().build();
        }
        return panelMemberExclusions;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public HearingRoute getHearingRoute() {
        return hearingRoute;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public void setHearingRoute(HearingRoute hearingRoute) {
        this.hearingRoute = hearingRoute;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public HearingState getHearingState() {
        return hearingState;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public void setHearingState(HearingState hearingState) {
        this.hearingState = hearingState;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public JudicialUserBase getReservedJudge() {
        return reservedJudge;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public void setReservedJudge(JudicialUserBase reservedJudge) {
        this.reservedJudge = reservedJudge;
    }

    @SuppressWarnings("unused")
    @JsonProperty
    public void setPanelMemberExclusions(PanelMemberExclusions panelMemberExclusions) {
        this.panelMemberExclusions = panelMemberExclusions;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public OverrideFields getOverrideFields() {
        return overrideFields;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public void setOverrideFields(OverrideFields overrideFields) {
        this.overrideFields = overrideFields;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public OverrideFields getDefaultListingValues() {
        return defaultListingValues;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public void setDefaultListingValues(OverrideFields defaultListingValues) {
        this.defaultListingValues = defaultListingValues;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public List<AmendReason> getAmendReasons() {
        return amendReasons;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public void setAmendReasons(List<AmendReason> amendReasons) {
        this.amendReasons = amendReasons;
    }
}

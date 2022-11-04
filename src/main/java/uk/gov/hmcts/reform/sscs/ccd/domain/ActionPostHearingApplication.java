package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionPostHearingApplication {
    @JsonProperty("actionPostHearingApplicationTypeSelected")
    private ActionPostHearingTypes typeSelected;
    @Getter(AccessLevel.NONE)
    @JsonProperty("actionSetAside")
    private ActionSetAside actionSetAside;
    @Getter(AccessLevel.NONE)
    @JsonProperty("actionCorrection")
    private ActionCorrection actionCorrection;
    @Getter(AccessLevel.NONE)
    @JsonProperty("actionStatementOfReasons")
    private ActionStatementOfReasons actionStatementOfReasons;
    @Getter(AccessLevel.NONE)
    @JsonProperty("actionPermissionToAppeal")
    private ActionPermissionToAppeal actionPermissionToAppeal;
    @Getter(AccessLevel.NONE)
    @JsonProperty("actionLibertyToApply")
    private ActionLibertyToApply actionLibertyToApply;

    @JsonIgnore
    public ActionSetAside getActionSetAside() {
        if (isNull(actionSetAside)) {
            actionSetAside = new ActionSetAside();
        }
        return actionSetAside;
    }

    @JsonIgnore
    public ActionCorrection getActionCorrection() {
        if (isNull(actionCorrection)) {
            actionCorrection = new ActionCorrection();
        }
        return actionCorrection;
    }

    @JsonIgnore
    public ActionStatementOfReasons getActionStatementOfReasons() {
        if (isNull(actionStatementOfReasons)) {
            actionStatementOfReasons = new ActionStatementOfReasons();
        }
        return actionStatementOfReasons;
    }

    @JsonIgnore
    public ActionPermissionToAppeal getActionPermissionToAppeal() {
        if (isNull(actionPermissionToAppeal)) {
            actionPermissionToAppeal = new ActionPermissionToAppeal();
        }
        return actionPermissionToAppeal;
    }

    @JsonIgnore
    public ActionLibertyToApply getActionLibertyToApply() {
        if (isNull(actionLibertyToApply)) {
            actionLibertyToApply = new ActionLibertyToApply();
        }
        return actionLibertyToApply;
    }
}

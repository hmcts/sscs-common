package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude
public class PanelMemberExclusions {

    @SuppressWarnings("unused")
    public PanelMemberExclusions(YesNo arePanelMembersExcluded,
                                 List<JudicialUserBase> excludedPanelMembers,
                                 YesNo arePanelMembersReserved) {
        this.arePanelMembersExcluded = arePanelMembersExcluded;
        this.excludedPanelMembers = excludedPanelMembers;
        this.arePanelMembersReserved = arePanelMembersReserved;
    }

    @JsonProperty("arePanelMembersExcluded")
    private YesNo arePanelMembersExcluded;
    @JsonProperty("excludedPanelMembers")
    private List<JudicialUserBase> excludedPanelMembers;
    @JsonProperty("arePanelMembersReserved")
    private YesNo arePanelMembersReserved;
}

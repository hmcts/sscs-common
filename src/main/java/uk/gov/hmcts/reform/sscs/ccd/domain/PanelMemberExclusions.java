package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
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
@JsonInclude
public class PanelMemberExclusions {
    private YesNo arePanelMembersReserved;
    private YesNo arePanelMembersExcluded;
    private List<CollectionItem<JudicialUserBase>> excludedPanelMembers;
    private List<CollectionItem<JudicialUserBase>> reservedPanelMembers;
}

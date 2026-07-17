package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class PanelMemberExclusions {
    @CCD(label = "Are Panel Members Reserved?", typeOverride = FieldType.YesOrNo)
    private YesNo arePanelMembersReserved;
    @CCD(label = "Are Panel Members Excluded?", typeOverride = FieldType.YesOrNo)
    private YesNo arePanelMembersExcluded;
    @CCD(label = "Excluded Panel Member", typeOverride = FieldType.Collection, typeParameterOverride = "JudicialUser")
    private List<CollectionItem<JudicialUserBase>> excludedPanelMembers;
    @CCD(label = "Reserved Panel Member", typeOverride = FieldType.Collection, typeParameterOverride = "JudicialUser")
    private List<CollectionItem<JudicialUserBase>> reservedPanelMembers;
}

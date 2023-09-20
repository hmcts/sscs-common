package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class JudicialUserPanel {
    @JsonProperty("assignedTo")
    private JudicialUserBase assignedTo;
    @JsonProperty("panelMembers")
    private List<CollectionItem<JudicialUserBase>> panelMembers;

    @SuppressWarnings("unused")
    @JsonIgnore
    public List<JudicialUserBase> getAllPanelMembers() {
        List<JudicialUserBase> allPanelMembers = new LinkedList<>();
        if (nonNull(assignedTo)) {
            allPanelMembers.add(assignedTo);
        }

        if (nonNull(panelMembers)) {
            allPanelMembers.addAll(panelMembers.stream().filter(Objects::nonNull).map(CollectionItem::getValue).toList());
        }

        return allPanelMembers.stream().filter(Objects::nonNull).toList();
    }
}

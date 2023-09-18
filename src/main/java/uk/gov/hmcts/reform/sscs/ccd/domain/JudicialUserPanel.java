package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    JudicialUserBase assignedTo;
    List<CollectionItem<JudicialUserBase>> panelMembers;

    @SuppressWarnings("unused")
    @JsonIgnore
    public List<JudicialUserBase> getAllPanelMembers() {
        List<JudicialUserBase> allPanelMembers;
        if (nonNull(panelMembers)) {
            allPanelMembers = panelMembers.stream().map(CollectionItem::getValue).toList();
            allPanelMembers.add(0, assignedTo);
        } else {
            allPanelMembers = List.of(assignedTo);
        }

        return allPanelMembers.stream().filter(Objects::nonNull).toList();
    }
}

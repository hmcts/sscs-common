package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PartyTest {

    @Test
    void getConfidentialityRequiredAnswerReturnsNullWhenConfidentialityRequirementIsNull() {
        final Party party = OtherParty.builder().build();

        assertThat(party.getConfidentialityRequiredAnswer()).isNull();
    }

    @Test
    void getConfidentialityRequiredAnswerReturnsValueFromConfidentialityRequirement() {
        final Party party = OtherParty.builder().build();
        party.setConfidentialityRequirement(new DynamicList(
            new DynamicListItem(YesNoUnknown.YES.name(), YesNoUnknown.YES.toString()), null));

        assertThat(party.getConfidentialityRequiredAnswer()).isEqualTo(YesNoUnknown.YES);
    }

    @Test
    void setConfidentialityRequiredAnswerCreatesDynamicListWhenNoneExists() {
        final Party party = OtherParty.builder().build();

        party.setConfidentialityRequiredAnswer(YesNoUnknown.UNKNOWN);

        assertThat(party.getConfidentialityRequirement().getValue().getCode()).isEqualTo(YesNoUnknown.UNKNOWN.name());
        assertThat(party.getConfidentialityRequirement().getValue().getLabel()).isEqualTo(YesNoUnknown.UNKNOWN.toString());
        assertThat(party.getConfidentialityRequirement().getListItems()).isNull();
    }

    @Test
    void setConfidentialityRequiredAnswerWithNullCreatesNullDynamicListWhenNoneExists() {
        final Party party = OtherParty.builder().build();

        party.setConfidentialityRequiredAnswer(null);

        assertThat(party.getConfidentialityRequirement()).isNull();
    }

    @Test
    void setConfidentialityRequiredAnswerPreservesExistingListItemsWhenDynamicListExists() {
        final Party party = OtherParty.builder().build();
        final DynamicListItem listItem = new DynamicListItem("code", "label");
        party.setConfidentialityRequirement(new DynamicList(
            new DynamicListItem(YesNoUnknown.NO.name(), YesNoUnknown.NO.toString()), List.of(listItem)));

        party.setConfidentialityRequiredAnswer(YesNoUnknown.YES);

        assertThat(party.getConfidentialityRequirement().getValue().getCode()).isEqualTo(YesNoUnknown.YES.name());
        assertThat(party.getConfidentialityRequirement().getValue().getLabel()).isEqualTo(YesNoUnknown.YES.toString());
        assertThat(party.getConfidentialityRequirement().getListItems()).containsExactly(listItem);
    }

    @Test
    void setConfidentialityRequiredAnswerWithNullClearsValueWhenDynamicListExists() {
        final Party party = OtherParty.builder().build();
        party.setConfidentialityRequirement(new DynamicList(
            new DynamicListItem(YesNoUnknown.YES.name(), YesNoUnknown.YES.toString()), null));

        party.setConfidentialityRequiredAnswer(null);

        assertThat(party.getConfidentialityRequirement().getValue().getCode()).isNull();
        assertThat(party.getConfidentialityRequirement().getValue().getLabel()).isNull();
    }
}

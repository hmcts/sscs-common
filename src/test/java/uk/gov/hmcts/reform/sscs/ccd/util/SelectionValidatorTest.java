package uk.gov.hmcts.reform.sscs.ccd.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentSelectionDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.DynamicList;
import uk.gov.hmcts.reform.sscs.ccd.domain.DynamicListItem;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherPartySelectionDetails;

class SelectionValidatorTest {

    @Test
    void givenNullOtherPartySelection_thenReturnsFalse() {
        assertThat(SelectionValidator.otherPartySelectionContainsDuplicates(null)).isFalse();
    }

    @Test
    void givenEmptyOtherPartySelection_thenReturnsFalse() {
        assertThat(SelectionValidator.otherPartySelectionContainsDuplicates(List.of())).isFalse();
    }

    @Test
    void givenOtherPartySelectionWithNoDuplicates_thenReturnsFalse() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            otherPartySelectionEntry("party1"),
            otherPartySelectionEntry("party2")
        );

        assertThat(SelectionValidator.otherPartySelectionContainsDuplicates(selection)).isFalse();
    }

    @Test
    void givenOtherPartySelectionWithDuplicates_thenReturnsTrue() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            otherPartySelectionEntry("party1"),
            otherPartySelectionEntry("party1")
        );

        assertThat(SelectionValidator.otherPartySelectionContainsDuplicates(selection)).isTrue();
    }

    @Test
    void givenOtherPartySelectionWithNullDynamicList_thenIgnoresNullsAndReturnsFalse() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            CcdValue.<OtherPartySelectionDetails>builder()
                .value(OtherPartySelectionDetails.builder().otherPartiesList(null).build())
                .build(),
            otherPartySelectionEntry("party1")
        );

        assertThat(SelectionValidator.otherPartySelectionContainsDuplicates(selection)).isFalse();
    }

    @Test
    void givenOtherPartySelectionWithNullDynamicListValue_thenIgnoresNullsAndReturnsFalse() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            CcdValue.<OtherPartySelectionDetails>builder()
                .value(OtherPartySelectionDetails.builder()
                    .otherPartiesList(new DynamicList(null, List.of()))
                    .build())
                .build(),
            otherPartySelectionEntry("party1")
        );

        assertThat(SelectionValidator.otherPartySelectionContainsDuplicates(selection)).isFalse();
    }

    @Test
    void givenNullDocumentSelection_thenReturnsFalse() {
        assertThat(SelectionValidator.documentSelectionContainsDuplicates(null)).isFalse();
    }

    @Test
    void givenEmptyDocumentSelection_thenReturnsFalse() {
        assertThat(SelectionValidator.documentSelectionContainsDuplicates(List.of())).isFalse();
    }

    @Test
    void givenDocumentSelectionWithNoDuplicates_thenReturnsFalse() {
        final List<CcdValue<DocumentSelectionDetails>> selection = List.of(
            documentSelectionEntry("doc1"),
            documentSelectionEntry("doc2")
        );

        assertThat(SelectionValidator.documentSelectionContainsDuplicates(selection)).isFalse();
    }

    @Test
    void givenDocumentSelectionWithDuplicates_thenReturnsTrue() {
        final List<CcdValue<DocumentSelectionDetails>> selection = List.of(
            documentSelectionEntry("doc1"),
            documentSelectionEntry("doc1")
        );

        assertThat(SelectionValidator.documentSelectionContainsDuplicates(selection)).isTrue();
    }

    @Test
    void givenDocumentSelectionWithNullDynamicList_thenIgnoresNullsAndReturnsFalse() {
        final List<CcdValue<DocumentSelectionDetails>> selection = List.of(
            CcdValue.<DocumentSelectionDetails>builder()
                .value(DocumentSelectionDetails.builder().documentsList(null).build())
                .build(),
            documentSelectionEntry("doc1")
        );

        assertThat(SelectionValidator.documentSelectionContainsDuplicates(selection)).isFalse();
    }

    private CcdValue<OtherPartySelectionDetails> otherPartySelectionEntry(final String code) {
        return CcdValue.<OtherPartySelectionDetails>builder()
            .value(OtherPartySelectionDetails.builder()
                .otherPartiesList(new DynamicList(new DynamicListItem(code, code), List.of()))
                .build())
            .build();
    }

    private CcdValue<DocumentSelectionDetails> documentSelectionEntry(final String code) {
        return CcdValue.<DocumentSelectionDetails>builder()
            .value(DocumentSelectionDetails.builder()
                .documentsList(new DynamicList(new DynamicListItem(code, code), List.of()))
                .build())
            .build();
    }
}
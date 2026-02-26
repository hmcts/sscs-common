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
    void givenNullOtherPartySelection_thenReturnsEmpty() {
        assertThat(SelectionValidator.validateOtherPartySelection(null)).isEmpty();
    }

    @Test
    void givenEmptyOtherPartySelection_thenReturnsEmpty() {
        assertThat(SelectionValidator.validateOtherPartySelection(List.of())).isEmpty();
    }

    @Test
    void givenOtherPartySelectionWithNoDuplicates_thenReturnsEmpty() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            otherPartySelectionEntry("party1"),
            otherPartySelectionEntry("party2")
        );

        assertThat(SelectionValidator.validateOtherPartySelection(selection)).isEmpty();
    }

    @Test
    void givenOtherPartySelectionWithDuplicates_thenReturnsError() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            otherPartySelectionEntry("party1"),
            otherPartySelectionEntry("party1")
        );

        assertThat(SelectionValidator.validateOtherPartySelection(selection))
            .contains("Other parties cannot be selected more than once");
    }

    @Test
    void givenOtherPartySelectionWithNullDynamicList_thenIgnoresNullsAndReturnsEmpty() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            CcdValue.<OtherPartySelectionDetails>builder()
                .value(OtherPartySelectionDetails.builder().otherPartiesList(null).build())
                .build(),
            otherPartySelectionEntry("party1")
        );

        assertThat(SelectionValidator.validateOtherPartySelection(selection)).isEmpty();
    }

    @Test
    void givenOtherPartySelectionWithNullDynamicListValue_thenIgnoresNullsAndReturnsEmpty() {
        final List<CcdValue<OtherPartySelectionDetails>> selection = List.of(
            CcdValue.<OtherPartySelectionDetails>builder()
                .value(OtherPartySelectionDetails.builder()
                    .otherPartiesList(new DynamicList(null, List.of()))
                    .build())
                .build(),
            otherPartySelectionEntry("party1")
        );

        assertThat(SelectionValidator.validateOtherPartySelection(selection)).isEmpty();
    }

    @Test
    void givenNullDocumentSelection_thenReturnsEmpty() {
        assertThat(SelectionValidator.validateDocumentSelection(null)).isEmpty();
    }

    @Test
    void givenEmptyDocumentSelection_thenReturnsEmpty() {
        assertThat(SelectionValidator.validateDocumentSelection(List.of())).isEmpty();
    }

    @Test
    void givenDocumentSelectionWithNoDuplicates_thenReturnsEmpty() {
        final List<CcdValue<DocumentSelectionDetails>> selection = List.of(
            documentSelectionEntry("doc1"),
            documentSelectionEntry("doc2")
        );

        assertThat(SelectionValidator.validateDocumentSelection(selection)).isEmpty();
    }

    @Test
    void givenDocumentSelectionWithDuplicates_thenReturnsError() {
        final List<CcdValue<DocumentSelectionDetails>> selection = List.of(
            documentSelectionEntry("doc1"),
            documentSelectionEntry("doc1")
        );

        assertThat(SelectionValidator.validateDocumentSelection(selection))
            .contains("The same document cannot be selected more than once");
    }

    @Test
    void givenDocumentSelectionWithNullDynamicList_thenIgnoresNullsAndReturnsEmpty() {
        final List<CcdValue<DocumentSelectionDetails>> selection = List.of(
            CcdValue.<DocumentSelectionDetails>builder()
                .value(DocumentSelectionDetails.builder().documentsList(null).build())
                .build(),
            documentSelectionEntry("doc1")
        );

        assertThat(SelectionValidator.validateDocumentSelection(selection)).isEmpty();
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
package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SscsDocumentTest {

    @Test
    void sortsByDocumentDateAddedDescending() {
        final SscsDocument oldest = sscsDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf");
        final SscsDocument middle = sscsDocumentWithDateAddedAndFileName("2021-06-15", "a.pdf");
        final SscsDocument newest = sscsDocumentWithDateAddedAndFileName("2022-12-25", "a.pdf");

        final List<SscsDocument> documents = new ArrayList<>(List.of(middle, oldest, newest));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentDateAdded())
            .containsExactly("2022-12-25", "2021-06-15", "2020-01-01");
    }

    @Test
    void sortsDocumentsWithNullDocumentDateAddedLast() {
        final SscsDocument withDate = sscsDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf");
        final SscsDocument withoutDate = sscsDocumentWithDateAddedAndFileName(null, "a.pdf");

        final List<SscsDocument> documents = new ArrayList<>(List.of(withoutDate, withDate));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentDateAdded())
            .containsExactly("2020-01-01", null);
    }

    @Test
    void preservesOriginalOrderWhenDocumentDateAddedIsEqual() {
        final SscsDocument fileB = sscsDocumentWithDateAddedAndFileName("2020-01-01", "b.pdf");
        final SscsDocument fileA = sscsDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf");
        final SscsDocument fileC = sscsDocumentWithDateAddedAndFileName("2020-01-01", "c.pdf");

        final List<SscsDocument> documents = new ArrayList<>(List.of(fileB, fileA, fileC));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentFileName())
            .containsExactly("b.pdf", "a.pdf", "c.pdf");
    }

    @Test
    void sortsByDocumentDateAddedDescendingPreservingOriginalOrderWithinEqualDates() {
        final SscsDocument oldFileB = sscsDocumentWithDateAddedAndFileName("2020-01-01", "b.pdf");
        final SscsDocument newFileA = sscsDocumentWithDateAddedAndFileName("2022-01-01", "a.pdf");
        final SscsDocument oldFileC = sscsDocumentWithDateAddedAndFileName("2020-01-01", "c.pdf");
        final SscsDocument newFileZ = sscsDocumentWithDateAddedAndFileName("2022-01-01", "z.pdf");
        final SscsDocument newFileM = sscsDocumentWithDateAddedAndFileName("2022-01-01", "m.pdf");

        final List<SscsDocument> documents = new ArrayList<>(
            List.of(oldFileB, newFileA, oldFileC, newFileZ, newFileM));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(
                document -> document.getValue().getDocumentDateAdded(),
                document -> document.getValue().getDocumentFileName())
            .containsExactly(
                tuple("2022-01-01", "a.pdf"),
                tuple("2022-01-01", "z.pdf"),
                tuple("2022-01-01", "m.pdf"),
                tuple("2020-01-01", "b.pdf"),
                tuple("2020-01-01", "c.pdf"));
    }

    private static SscsDocument sscsDocumentWithDateAddedAndFileName(final String documentDateAdded, final String documentFileName) {
        final SscsDocumentDetails value = SscsDocumentDetails.builder()
            .documentDateAdded(documentDateAdded)
            .documentFileName(documentFileName)
            .build();
        return SscsDocument.builder().value(value).build();
    }
}

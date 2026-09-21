package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SscsWelshDocumentTest {

    @Test
    void sortsByDocumentDateAddedDescending() {
        final SscsWelshDocument oldest = sscsWelshDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf");
        final SscsWelshDocument middle = sscsWelshDocumentWithDateAddedAndFileName("2021-06-15", "a.pdf");
        final SscsWelshDocument newest = sscsWelshDocumentWithDateAddedAndFileName("2022-12-25", "a.pdf");

        final List<SscsWelshDocument> documents = new ArrayList<>(List.of(middle, oldest, newest));

        documents.sort(SscsWelshDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentDateAdded())
            .containsExactly("2022-12-25", "2021-06-15", "2020-01-01");
    }

    @Test
    void sortsDocumentsWithNullDocumentDateAddedLast() {
        final SscsWelshDocument withDate = sscsWelshDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf");
        final SscsWelshDocument withoutDate = sscsWelshDocumentWithDateAddedAndFileName(null, "a.pdf");

        final List<SscsWelshDocument> documents = new ArrayList<>(List.of(withoutDate, withDate));

        documents.sort(SscsWelshDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentDateAdded())
            .containsExactly("2020-01-01", null);
    }

    @Test
    void sortsByDocumentDateAddedDescendingPreservingOriginalOrderWithinEqualDates() {
        final SscsWelshDocument oldFileB = sscsWelshDocumentWithDateAddedAndFileName("2020-01-01", "b.pdf");
        final SscsWelshDocument newFileA = sscsWelshDocumentWithDateAddedAndFileName("2022-01-01", "a.pdf");
        final SscsWelshDocument oldFileC = sscsWelshDocumentWithDateAddedAndFileName("2020-01-01", "c.pdf");
        final SscsWelshDocument newFileZ = sscsWelshDocumentWithDateAddedAndFileName("2022-01-01", "z.pdf");
        final SscsWelshDocument newFileM = sscsWelshDocumentWithDateAddedAndFileName("2022-01-01", "m.pdf");

        final List<SscsWelshDocument> documents = new ArrayList<>(
            List.of(oldFileB, newFileA, oldFileC, newFileZ, newFileM));

        documents.sort(SscsWelshDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

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

    private static SscsWelshDocument sscsWelshDocumentWithDateAddedAndFileName(final String documentDateAdded, final String documentFileName) {
        final SscsWelshDocumentDetails value = SscsWelshDocumentDetails.builder()
            .documentDateAdded(documentDateAdded)
            .documentFileName(documentFileName)
            .build();
        return SscsWelshDocument.builder().value(value).build();
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SscsDocumentTest {

    @Test
    void sortsByDocumentDateAddedDescending() {
        final SscsDocument oldest = sscsDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf", null);
        final SscsDocument middle = sscsDocumentWithDateAddedAndFileName("2021-06-15", "a.pdf", null);
        final SscsDocument newest = sscsDocumentWithDateAddedAndFileName("2022-12-25", "a.pdf", null);

        final List<SscsDocument> documents = new ArrayList<>(List.of(middle, oldest, newest));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentDateAdded())
            .containsExactly("2022-12-25", "2021-06-15", "2020-01-01");
    }

    @Test
    void sortsDocumentsWithNullDocumentDateAddedLast() {
        final SscsDocument withDate = sscsDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf", null);
        final SscsDocument withoutDate = sscsDocumentWithDateAddedAndFileName(null, "a.pdf", null);

        final List<SscsDocument> documents = new ArrayList<>(List.of(withoutDate, withDate));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentDateAdded())
            .containsExactly("2020-01-01", null);
    }

    @Test
    void sortsByBundleAdditionLetterDescendingWhenDocumentDateAddedIsEqual() {
        final SscsDocument fileA = sscsDocumentWithDateAddedAndFileName("2022-01-01", "a.pdf", "A");
        final SscsDocument fileZ = sscsDocumentWithDateAddedAndFileName("2022-01-01", "z.pdf", "Z");
        final SscsDocument fileM = sscsDocumentWithDateAddedAndFileName("2022-01-01", "m.pdf", "M");

        final List<SscsDocument> documents = new ArrayList<>(List.of(fileA, fileZ, fileM));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getBundleAddition())
            .containsExactly("Z", "M", "A");
    }

    @Test
    void sortsByBundleAdditionNumberDescendingWhenDocumentDateAddedAndBundleAdditionLetterAreEqual() {
        final SscsDocument fileB1 = sscsDocumentWithDateAddedAndFileName("2022-01-01", "b1.pdf", "B1");
        final SscsDocument fileB10 = sscsDocumentWithDateAddedAndFileName("2022-01-01", "b10.pdf", "B10");
        final SscsDocument fileB2 = sscsDocumentWithDateAddedAndFileName("2022-01-01", "b2.pdf", "B2");

        final List<SscsDocument> documents = new ArrayList<>(List.of(fileB1, fileB10, fileB2));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getBundleAddition())
            .containsExactly("B10", "B2", "B1");
    }

    @Test
    void sortsDocumentsWithNullBundleAdditionLastWhenDocumentDateAddedIsEqual() {
        final SscsDocument withoutBundleAddition = sscsDocumentWithDateAddedAndFileName("2022-01-01", "j.pdf", null);
        final SscsDocument withBundleAddition = sscsDocumentWithDateAddedAndFileName("2022-01-01", "a.pdf", "A");

        final List<SscsDocument> documents = new ArrayList<>(List.of(withoutBundleAddition, withBundleAddition));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getBundleAddition())
            .containsExactly("A", null);
    }

    @Test
    void sortsByDocumentDateAddedBeforeBundleAddition() {
        final SscsDocument oldFileZ = sscsDocumentWithDateAddedAndFileName("1999-12-11", "z.pdf", "Z");
        final SscsDocument newFileA = sscsDocumentWithDateAddedAndFileName("2022-01-01", "a.pdf", "A");

        final List<SscsDocument> documents = new ArrayList<>(List.of(oldFileZ, newFileA));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(
                document -> document.getValue().getDocumentDateAdded(),
                document -> document.getValue().getBundleAddition())
            .containsExactly(
                tuple("2022-01-01", "A"),
                tuple("1999-12-11", "Z"));
    }

    @Test
    void preservesOriginalOrderWhenDocumentDateAddedIsEqual() {
        final SscsDocument fileB = sscsDocumentWithDateAddedAndFileName("2020-01-01", "b.pdf", null);
        final SscsDocument fileA = sscsDocumentWithDateAddedAndFileName("2020-01-01", "a.pdf", null);
        final SscsDocument fileC = sscsDocumentWithDateAddedAndFileName("2020-01-01", "c.pdf", null);

        final List<SscsDocument> documents = new ArrayList<>(List.of(fileB, fileA, fileC));

        documents.sort(SscsDocument.BY_DOCUMENT_DATE_ADDED_DESCENDING);

        assertThat(documents)
            .extracting(document -> document.getValue().getDocumentFileName())
            .containsExactly("b.pdf", "a.pdf", "c.pdf");
    }

    private static SscsDocument sscsDocumentWithDateAddedAndFileName(final String documentDateAdded, final String documentFileName,
        final String bundleAddition) {
        final SscsDocumentDetails value = SscsDocumentDetails.builder()
            .documentDateAdded(documentDateAdded)
            .documentFileName(documentFileName)
            .bundleAddition(bundleAddition)
            .build();
        return SscsDocument.builder().value(value).build();
    }
}

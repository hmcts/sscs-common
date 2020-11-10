package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;

public class SscsCaseDataTest {

    private LocalDate now = LocalDate.now();

    @Test
    public void sortHearingsByDateWhenIdsAreBlank() {
        List<Hearing> hearings = new ArrayList<>();
        Hearing hearing1 = Hearing.builder().value(HearingDetails.builder().hearingDate("2019-01-01").time("12:00").build()).build();
        Hearing hearing2 = Hearing.builder().value(HearingDetails.builder().hearingDate("2019-03-01").time("12:00").build()).build();
        Hearing hearing3 = Hearing.builder().value(HearingDetails.builder().hearingDate("2019-02-01").time("12:00").build()).build();
        hearings.add(hearing1);
        hearings.add(hearing2);
        hearings.add(hearing3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().hearings(hearings).build();
        sscsCaseData.sortCollections();

        assertEquals("2019-03-01", sscsCaseData.getHearings().get(0).getValue().getHearingDate());
        assertEquals("2019-02-01", sscsCaseData.getHearings().get(1).getValue().getHearingDate());
        assertEquals("2019-01-01", sscsCaseData.getHearings().get(2).getValue().getHearingDate());
    }

    @Test
    public void sortHearingsByIdFirstThenDate() {
        List<Hearing> hearings = new ArrayList<>();
        Hearing hearing1 = Hearing.builder().value(HearingDetails.builder().hearingId("2").hearingDate("2019-04-01").time("12:00").build()).build();
        Hearing hearing2 = Hearing.builder().value(HearingDetails.builder().hearingId("20").hearingDate("2019-03-01").time("12:00").build()).build();
        Hearing hearing3 = Hearing.builder().value(HearingDetails.builder().hearingId("20").hearingDate("2019-02-01").time("12:00").build()).build();
        Hearing hearing4 = Hearing.builder().value(HearingDetails.builder().hearingId("3").hearingDate("2019-02-01").time("12:00").build()).build();

        hearings.add(hearing1);
        hearings.add(hearing2);
        hearings.add(hearing3);
        hearings.add(hearing4);

        SscsCaseData sscsCaseData = SscsCaseData.builder().hearings(hearings).build();
        sscsCaseData.sortCollections();

        assertEquals("2019-03-01", sscsCaseData.getHearings().get(0).getValue().getHearingDate());
        assertEquals("20", sscsCaseData.getHearings().get(0).getValue().getHearingId());
        assertEquals("2019-02-01", sscsCaseData.getHearings().get(1).getValue().getHearingDate());
        assertEquals("20", sscsCaseData.getHearings().get(1).getValue().getHearingId());
        assertEquals("2019-02-01", sscsCaseData.getHearings().get(2).getValue().getHearingDate());
        assertEquals("3", sscsCaseData.getHearings().get(2).getValue().getHearingId());
        assertEquals("2019-04-01", sscsCaseData.getHearings().get(3).getValue().getHearingDate());
        assertEquals("2", sscsCaseData.getHearings().get(3).getValue().getHearingId());
    }

    @Test
    public void sortHearingsByIdWhenFewHearingsHaveBlankId() {
        List<Hearing> hearings = new ArrayList<>();
        Hearing hearing1 = Hearing.builder().value(HearingDetails.builder().hearingDate("2019-04-01").time("12:00").build()).build();
        Hearing hearing2 = Hearing.builder().value(HearingDetails.builder().hearingDate("2019-05-01").time("12:00").build()).build();
        Hearing hearing3 = Hearing.builder().value(HearingDetails.builder().hearingId("1").hearingDate("2019-02-01").time("12:00").build()).build();
        hearings.add(hearing1);
        hearings.add(hearing2);
        hearings.add(hearing3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().hearings(hearings).build();
        sscsCaseData.sortCollections();

        assertEquals("2019-02-01", sscsCaseData.getHearings().get(0).getValue().getHearingDate());
        assertEquals("1", sscsCaseData.getHearings().get(0).getValue().getHearingId());
        assertEquals("2019-05-01", sscsCaseData.getHearings().get(1).getValue().getHearingDate());
        assertNull(sscsCaseData.getHearings().get(1).getValue().getHearingId());
        assertEquals("2019-04-01", sscsCaseData.getHearings().get(2).getValue().getHearingDate());
        assertNull(sscsCaseData.getHearings().get(2).getValue().getHearingId());
    }

    @Test
    public void sortEventsByDate() {
        List<Event> events = new ArrayList<>();
        Event event1 = Event.builder().value(EventDetails.builder().date("2019-01-01").build()).build();
        Event event2 = Event.builder().value(EventDetails.builder().date("2019-03-01").build()).build();
        Event event3 = Event.builder().value(EventDetails.builder().date("2019-02-01").build()).build();
        events.add(event1);
        events.add(event2);
        events.add(event3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().events(events).build();
        sscsCaseData.sortCollections();

        assertEquals("2019-03-01", sscsCaseData.getEvents().get(0).getValue().getDate());
        assertEquals("2019-02-01", sscsCaseData.getEvents().get(1).getValue().getDate());
        assertEquals("2019-01-01", sscsCaseData.getEvents().get(2).getValue().getDate());
    }

    @Test
    public void sortEvidenceByDate() {
        List<Document> documents = new ArrayList<>();
        Document document1 = Document.builder().value(DocumentDetails.builder().dateReceived("2019-01-01").build()).build();
        Document document2 = Document.builder().value(DocumentDetails.builder().dateReceived("2019-03-01").build()).build();
        Document document3 = Document.builder().value(DocumentDetails.builder().dateReceived("2019-02-01").build()).build();

        documents.add(document1);
        documents.add(document2);
        documents.add(document3);
        Evidence evidence = Evidence.builder().documents(documents).build();

        SscsCaseData sscsCaseData = SscsCaseData.builder().evidence(evidence).build();
        sscsCaseData.sortCollections();

        assertEquals("2019-03-01", sscsCaseData.getEvidence().getDocuments().get(0).getValue().getDateReceived());
        assertEquals("2019-02-01", sscsCaseData.getEvidence().getDocuments().get(1).getValue().getDateReceived());
        assertEquals("2019-01-01", sscsCaseData.getEvidence().getDocuments().get(2).getValue().getDateReceived());
    }


    @Test
    public void sortCorrespondenceByDateAndTime() {
        List<Correspondence> correspondence = new ArrayList<>();
        Correspondence correspondence1 = Correspondence.builder().value(CorrespondenceDetails.builder().sentOn("1 Feb 2019 11:22").build()).build();
        Correspondence correspondence2 = Correspondence.builder().value(CorrespondenceDetails.builder().sentOn("1 Jan 2019 11:22").build()).build();
        Correspondence correspondence3 = Correspondence.builder().value(CorrespondenceDetails.builder().sentOn("1 Jan 2019 11:23").build()).build();

        correspondence.add(correspondence1);
        correspondence.add(correspondence2);
        correspondence.add(correspondence3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().correspondence(correspondence).build();
        sscsCaseData.sortCollections();

        assertEquals("1 Feb 2019 11:22", sscsCaseData.getCorrespondence().get(0).getValue().getSentOn());
        assertEquals("1 Jan 2019 11:23", sscsCaseData.getCorrespondence().get(1).getValue().getSentOn());
        assertEquals("1 Jan 2019 11:22", sscsCaseData.getCorrespondence().get(2).getValue().getSentOn());
    }

    @Test
    public void shouldCreateInfoRequest() throws JsonParseException, IOException {
        String expectedValue = "{\"appellantInfoRequestCollection\":[{\"value\":{\"appellantInfoParagraph\"" +
                ":\"Par1\",\"appellantInfoRequestDate\":\"date1\"},\"id\":null}]}";
        List<AppellantInfoRequest> appellantInfoRequests = new ArrayList<>();
        AppellantInfoRequest appellantInfoRequest1 = AppellantInfoRequest.builder()
                .appellantInfo(AppellantInfo.builder().paragraph("Par1").requestDate("date1").build()).build();

        appellantInfoRequests.add(appellantInfoRequest1);

        SscsCaseData sscsCaseData = SscsCaseData.builder().infoRequests(InfoRequests.builder()
                .appellantInfoRequest(appellantInfoRequests).build()).build();

        ObjectMapper mapper = new ObjectMapper();
        String infoRequestValue = mapper.writeValueAsString(sscsCaseData.getInfoRequests());

        assertEquals(expectedValue, infoRequestValue);
    }

    @Test
    public void givenACaseHasOneDocument_thenSelectThisDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertEquals("testUrl", result.getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleDocumentsOfSameType_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("oldTestUrl", DocumentType.DECISION_NOTICE, now.minusDays(2).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertEquals("latestTestUrl", result.getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleDocumentsOfSameTypeWithBundleFieldPopulated_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), "A", null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), "B", null));
        documents.add(buildSscsDocument("oldTestUrl", DocumentType.DECISION_NOTICE, now.minusDays(2).toString(), "C", null));
        documents.add(buildSscsDocument("noDateOldTestUrl", DocumentType.DECISION_NOTICE, null, "D", null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertEquals("latestTestUrl", result.getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleDocumentsOfSameTypeWithTwoOnSameDay_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertEquals("latestTestUrl", result.getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleDocumentsOfDifferentTypes_thenSelectTheLatestDocumentForDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DIRECTION_NOTICE);

        assertEquals("latestTestUrl", result.getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenAWithMultipleDocuments_thenSortByDateAdded() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("otherDoc", sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("otherDoc2", sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("testUrl", sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl", sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenAWithMultipleDocumentsAndOneDocAddedDateIsEmpty_thenSortByDateAddedAndPutEmptyDocumentLast() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("emptyDateAddedDoc", DocumentType.OTHER_DOCUMENT, null, null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("otherDoc", sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("otherDoc2", sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("testUrl", sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl", sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("emptyDateAddedDoc", sscsCaseData.getSscsDocument().get(4).getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleDocumentsOfSameTypeOnSameDayWithBundleAdditions_thenSortByBundleLetter() {
        List<SscsDocument> documents = new ArrayList<>();

        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, now.toString(), "B", null));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, now.toString(), "C", null));
        documents.add(buildSscsDocument("firstTestUrl", DocumentType.DECISION_NOTICE, now.toString(), "A", null));
        documents.add(buildSscsDocument("anotherTestUrl3", DocumentType.DECISION_NOTICE, now.toString(), "D", null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("firstTestUrl", sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl", sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl2", sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl3", sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl());
    }


    @Test
    public void givenACaseHasMultipleDocumentsWithNoDate_thenSelectTheLastOneItFinds() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, null, null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertEquals("latestTestUrl", result.getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenADocumentTypeIsNull_thenHandleCorrectly() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", null, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("testUrl2", DocumentType.DECISION_NOTICE, now.minusDays(2).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertEquals("testUrl2", result.getValue().getDocumentLink().getDocumentUrl());
    }


    @Test
    public void givenACaseHasOneWelshDocument_thenSelectThisDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        Assert.assertTrue("Result has a value", result.isPresent());
        assertEquals("testUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleWelshDocumentsOfSameType_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), null));
        documents.add(buildWelshSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null));
        documents.add(buildWelshSscsDocument("oldTestUrl", DocumentType.DECISION_NOTICE, now.minusDays(2).toString(), null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        Assert.assertTrue("Result has a value", result.isPresent());
        assertEquals("latestTestUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleWelshDocumentsOfSameTypeWithTwoOnSameDay_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.toString(), null));
        documents.add(buildWelshSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null));
        documents.add(buildWelshSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, now.toString(), null));
        documents.add(buildWelshSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result  = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        Assert.assertTrue("Result has a value", result.isPresent());
        assertEquals("latestTestUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasNoWelshDocuments_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(null).build();
        Optional<SscsWelshDocument> result  = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        Assert.assertTrue("Result is empty", result.isEmpty());
    }

    @Test
    public void givenACaseHasMultipleWelshDocumentsOfDifferentTypes_thenSelectTheLatestDocumentForDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null));
        documents.add(buildWelshSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null));
        documents.add(buildWelshSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null));
        documents.add(buildWelshSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null));
        documents.add(buildWelshSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null));
        documents.add(buildWelshSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null));
        documents.add(buildWelshSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result  = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DIRECTION_NOTICE);

        Assert.assertTrue("Result has a value", result.isPresent());
        assertEquals("latestTestUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
    }


    @Test
    public void givenACaseHasMultipleSscsDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_REQUESTED));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        assertEquals("Yes", sscsCaseData.getTranslationWorkOutstanding());
    }

    @Test
    public void givenACaseHasMultipleSscsDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly2() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        assertEquals("No", sscsCaseData.getTranslationWorkOutstanding());
    }

    @Test
    public void givenACaseHasMultipleSscsDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly3() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_REQUIRED));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        assertEquals("Yes", sscsCaseData.getTranslationWorkOutstanding());
    }

    private SscsDocument buildSscsDocument(String documentUrl, DocumentType documentType, String date, String bundleAddition, SscsDocumentTranslationStatus translationStatus) {
        String docType = documentType == null ? null : documentType.getValue();
        return SscsDocument.builder().value(
                SscsDocumentDetails.builder().documentType(docType)
                        .documentLink(DocumentLink.builder().documentUrl(documentUrl).build())
                        .documentDateAdded(date)
                        .bundleAddition(bundleAddition)
                        .documentTranslationStatus(translationStatus)
                        .build()).build();
    }

    private SscsWelshDocument buildWelshSscsDocument(String documentUrl, DocumentType documentType, String date, String bundleAddition) {
        String docType = documentType == null ? null : documentType.getValue();
        return SscsWelshDocument.builder().value(
                SscsWelshDocumentDetails.builder().documentType(docType)
                        .documentLink(DocumentLink.builder().documentUrl(documentUrl).build())
                        .documentDateAdded(date)
                        .build()).build();
    }

    @Test
    public void givenLanguagePreferenceWelshIsNull_thenIsLanguagePreferenceWelshShouldReturnFalse() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh(null).build();
        assertEquals(sscsCaseData.isLanguagePreferenceWelshAsBoolean(), Boolean.FALSE);
    }

    @Test
    public void givenLanguagePreferenceWelshIsYes_thenIsLanguagePreferenceWelshShouldReturnTrue() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("Yes").build();
        assertEquals(sscsCaseData.isLanguagePreferenceWelshAsBoolean(), Boolean.TRUE);
    }

    @Test
    public void givenLanguagePreferenceWelshIsNo_thenIsLanguagePreferenceWelshShouldReturnFalse() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("No").build();
        assertEquals(sscsCaseData.isLanguagePreferenceWelshAsBoolean(), Boolean.FALSE);
    }

    @Test
    public void givenLanguagePreferenceWelshIsNull_thenIsLanguagePreferenceWelshShouldReturnEnglish() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh(null).build();
        assertEquals(sscsCaseData.getLanguagePreference(), LanguagePreference.ENGLISH);
    }

    @Test
    public void givenLanguagePreferenceWelshIsNo_thenIsLanguagePreferenceWelshShouldReturnEnglish() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("No").build();
        assertEquals(sscsCaseData.getLanguagePreference(), LanguagePreference.ENGLISH);
    }

    @Test
    public void givenLanguagePreferenceWelshIsYes_thenIsLanguagePreferenceWelshShouldReturnWelsh() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("Yes").build();
        assertEquals(sscsCaseData.getLanguagePreference(), LanguagePreference.WELSH);
    }

    @Test
    public void givenUrgentHearingInfo_thenShouldReturnUrgentHearingInfo() {
        String todaysDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        String expectedUrgentHearingOutcome = "In progress";
        SscsCaseData sscsCaseData = SscsCaseData.builder().urgentCase("Yes").urgentHearingRegistered(todaysDate).urgentHearingOutcome("In progress").build();
        assertEquals("Yes", sscsCaseData.getUrgentCase());
        assertEquals(todaysDate, sscsCaseData.getUrgentHearingRegistered());
        assertEquals(expectedUrgentHearingOutcome, sscsCaseData.getUrgentHearingOutcome());
    }

}

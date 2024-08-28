package uk.gov.hmcts.reform.sscs.ccd.domain;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;
import uk.gov.hmcts.reform.sscs.ccd.callback.DwpDocumentType;

@RunWith(JUnitParamsRunner.class)
public class SscsCaseDataTest {

    private LocalDate now = LocalDate.now();
    private ObjectMapper mapper;

    @Before
    public void setUp() {

        Jackson2ObjectMapperBuilder objectMapperBuilder =
                new Jackson2ObjectMapperBuilder()
                        .featuresToEnable(READ_ENUMS_USING_TO_STRING)
                        .featuresToEnable(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                        .featuresToEnable(WRITE_ENUMS_USING_TO_STRING)
                        .serializationInclusion(JsonInclude.Include.NON_ABSENT);

        mapper = objectMapperBuilder.createXmlMapper(false).build();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void sortSscsDocuments() throws IOException {

        String path = getClass().getClassLoader().getResource("sscsDocumentSorting.json").getFile();
        String json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());
        List<SscsDocument> newSscsDocument  = mapper.readValue(json, new TypeReference<List<SscsDocument>>(){});
        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(newSscsDocument).build();
        sscsCaseData.sortCollections();

        assertTrue("No sorting error", true);
    }

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
        Document document4 = Document.builder().value(DocumentDetails.builder().dateReceived(null).build()).build();
        Document document5 = Document.builder().value(DocumentDetails.builder().dateReceived("NaN").build()).build();

        documents.add(document1);
        documents.add(document2);
        documents.add(document3);
        documents.add(document4);
        documents.add(document5);
        Evidence evidence = Evidence.builder().documents(documents).build();

        SscsCaseData sscsCaseData = SscsCaseData.builder().evidence(evidence).build();
        sscsCaseData.sortCollections();

        assertEquals("2019-03-01", sscsCaseData.getEvidence().getDocuments().get(0).getValue().getDateReceived());
        assertEquals("2019-02-01", sscsCaseData.getEvidence().getDocuments().get(1).getValue().getDateReceived());
        assertEquals("2019-01-01", sscsCaseData.getEvidence().getDocuments().get(2).getValue().getDateReceived());
        assertNull(sscsCaseData.getEvidence().getDocuments().get(3).getValue().getDateReceived());
        assertEquals("NaN", sscsCaseData.getEvidence().getDocuments().get(4).getValue().getDateReceived());
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
    public void sortSscsDocumentsByDateAdded() {
        List<SscsDocument> sscsDocuments = new ArrayList<>();
        SscsDocument sscsDocument1 = SscsDocument.builder().value(SscsDocumentDetails.builder().documentDateAdded("2019-01-01").build()).build();
        SscsDocument sscsDocument2 = SscsDocument.builder().value(SscsDocumentDetails.builder().documentDateAdded("2019-03-01").build()).build();
        SscsDocument sscsDocument3 = SscsDocument.builder().value(SscsDocumentDetails.builder().documentDateAdded("2019-01-01").build()).build();
        sscsDocuments.add(sscsDocument1);
        sscsDocuments.add(sscsDocument2);
        sscsDocuments.add(sscsDocument3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(sscsDocuments).build();
        sscsCaseData.sortCollections();

        assertEquals("2019-01-01", sscsCaseData.getSscsDocument().get(0).getValue().getDocumentDateAdded());
        assertEquals("2019-01-01", sscsCaseData.getSscsDocument().get(1).getValue().getDocumentDateAdded());
        assertEquals("2019-03-01", sscsCaseData.getSscsDocument().get(2).getValue().getDocumentDateAdded());
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
    public void givenACaseWithMultipleDocuments_thenSortByDateAdded() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("otherDoc", sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("testUrl", sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("otherDoc2", sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl", sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseWithMultipleDocumentsAndOneDocAddedDateIsEmpty_thenSortByDateAddedAndPutEmptyDocumentLast() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("emptyDateAddedDoc", DocumentType.OTHER_DOCUMENT, null, null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("otherDoc", sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("testUrl", sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("otherDoc2", sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl", sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("emptyDateAddedDoc", sscsCaseData.getSscsDocument().get(4).getValue().getDocumentLink().getDocumentUrl());

    }

    @Test
    public void givenACaseHasMultipleDocumentsOfSameTypeOnSameDayWithBundleAdditions_thenSortByBundleLetter() {
        List<SscsDocument> documents = new ArrayList<>();

        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-10-09", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-10-09", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-22", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-22", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-21", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-21", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-21", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-20", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-20", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-20", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-15", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-15", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-15", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-13", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-13", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-10", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-06", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-03", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-02", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-02", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-02", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-02", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-02", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-09-02", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-07-07", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-07-07", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-04-13", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-04-09", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-04-09", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-04-09", null, null));
        documents.add(buildSscsDocument(null, DocumentType.DECISION_NOTICE, "2021-03-16", null, null));
        documents.add(buildSscsDocument("A", DocumentType.DECISION_NOTICE, "2021-03-25", "A", null));
        documents.add(buildSscsDocument("B", DocumentType.DECISION_NOTICE, "2021-04-08", "B", null));
        documents.add(buildSscsDocument("C", DocumentType.DECISION_NOTICE, "2021-04-08", "C", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-04-08", "D", null));
        documents.add(buildSscsDocument("B", DocumentType.DECISION_NOTICE, "2021-04-09", "E", null));
        documents.add(buildSscsDocument("C", DocumentType.DECISION_NOTICE, "2021-04-09", "F", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-04-09", "G", null));

        documents.add(buildSscsDocument("B", DocumentType.DECISION_NOTICE, "2021-04-09", "H", null));
        documents.add(buildSscsDocument("C", DocumentType.DECISION_NOTICE, "2021-04-09", "I", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-04-09", "J", null));
        documents.add(buildSscsDocument("B", DocumentType.DECISION_NOTICE, "2021-04-09", "K", null));
        documents.add(buildSscsDocument("C", DocumentType.DECISION_NOTICE, "2021-04-09", "L", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-04-09", "M", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-04-09", "N", null));
        documents.add(buildSscsDocument("C", DocumentType.DECISION_NOTICE, "2021-04-14", "O", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-04-14", "P", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-04-14", "Q", null));
        documents.add(buildSscsDocument("C", DocumentType.DECISION_NOTICE, "2021-05-10", "R", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-05-28", "S", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-06-08", "T", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-06-08", "U", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-06-17", "V", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-08-20", "W", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-09-28", "X", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-06", "Y", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-14", "Z", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-19", "Z1", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z10", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z11", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z12", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z13", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z14", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-04", "Z15", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-02", "Z16", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-02", "Z17", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-15", "Z18", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-16", "Z19", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-19", "Z2", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-18", "Z20", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-19", "Z3", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-19", "Z4", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-21", "Z5", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-02", "Z6", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z7", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z8", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-11-03", "Z9", null));

        documents.add(buildSscsDocument("B", DocumentType.DECISION_NOTICE, "2021-10-09", "B", null));
        documents.add(buildSscsDocument("C", DocumentType.DECISION_NOTICE, "2021-10-09", "C", null));
        documents.add(buildSscsDocument("A", DocumentType.DECISION_NOTICE, "2021-10-09", "A", null));
        documents.add(buildSscsDocument("D", DocumentType.DECISION_NOTICE, "2021-10-09", "D", null));
        documents.add(buildSscsDocument("Z1", DocumentType.DECISION_NOTICE, now.toString(), "Z1", null));
        documents.add(buildSscsDocument("Z11", DocumentType.DECISION_NOTICE, now.toString(), "Z11", null));
        documents.add(buildSscsDocument("Z2", DocumentType.DECISION_NOTICE, now.toString(), "Z2", null));
        documents.add(buildSscsDocument("Z19", DocumentType.DECISION_NOTICE, now.toString(), "Z19", null));
        documents.add(buildSscsDocument("Z20", DocumentType.DECISION_NOTICE, now.toString(), "Z20", null));
        documents.add(buildSscsDocument("Z", DocumentType.DECISION_NOTICE, now.toString(), "Z", null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("A", sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("B", sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("C", sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("D", sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("Z", sscsCaseData.getSscsDocument().get(4).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("Z1", sscsCaseData.getSscsDocument().get(5).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("Z2", sscsCaseData.getSscsDocument().get(6).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("Z11", sscsCaseData.getSscsDocument().get(7).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("Z19", sscsCaseData.getSscsDocument().get(8).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("Z20", sscsCaseData.getSscsDocument().get(9).getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasScannedMultipleDocumentsOfSameDayWithControlNumber_thenSortByControlNumber() {
        List<ScannedDocument> documents = new ArrayList<>();

        documents.add(buildScannedDocument("2000", DocumentType.OTHER_EVIDENCE, now.toString(), "2000"));
        documents.add(buildScannedDocument("3000", DocumentType.AUDIO_DOCUMENT, now.minusDays(1).toString(), "3000"));
        documents.add(buildScannedDocument("1000", DocumentType.OTHER_EVIDENCE, now.toString(), "1000"));
        documents.add(buildScannedDocument("4000", DocumentType.OTHER_EVIDENCE, now.toString(), "4000"));
        documents.add(buildScannedDocument("6000", DocumentType.OTHER_EVIDENCE, now.toString(), "6000"));
        documents.add(buildScannedDocument("8000", DocumentType.AUDIO_DOCUMENT, now.toString(), "8000"));
        documents.add(buildScannedDocument("7000", DocumentType.AUDIO_DOCUMENT, now.toString(), "7000"));
        documents.add(buildScannedDocument("5000", DocumentType.AUDIO_DOCUMENT, now.toString(), "5000"));
        documents.add(buildScannedDocument("dummyString", DocumentType.AUDIO_DOCUMENT, now.toString(), "dummyString"));

        SscsCaseData sscsCaseData = SscsCaseData.builder().scannedDocuments(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("3000", sscsCaseData.getScannedDocuments().get(0).getValue().getUrl().getDocumentUrl());
        assertEquals("1000", sscsCaseData.getScannedDocuments().get(1).getValue().getUrl().getDocumentUrl());
        assertEquals("2000", sscsCaseData.getScannedDocuments().get(2).getValue().getUrl().getDocumentUrl());
        assertEquals("4000", sscsCaseData.getScannedDocuments().get(3).getValue().getUrl().getDocumentUrl());
        assertEquals("5000", sscsCaseData.getScannedDocuments().get(4).getValue().getUrl().getDocumentUrl());
        assertEquals("6000", sscsCaseData.getScannedDocuments().get(5).getValue().getUrl().getDocumentUrl());
        assertEquals("7000", sscsCaseData.getScannedDocuments().get(6).getValue().getUrl().getDocumentUrl());
        assertEquals("8000", sscsCaseData.getScannedDocuments().get(7).getValue().getUrl().getDocumentUrl());
        assertEquals("dummyString", sscsCaseData.getScannedDocuments().get(8).getValue().getUrl().getDocumentUrl());
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
    public void givenACaseWithMultipleDwpDocuments_thenSortByDateAdded() {
        List<DwpDocument> documents = new ArrayList<>();
        documents.add(buildDwpDocument("testUrl", DwpDocumentType.UCB, now.minusDays(1).toString()));
        documents.add(buildDwpDocument("anotherTestUrl", DwpDocumentType.UCB, now.toString()));
        documents.add(buildDwpDocument("otherDoc", DwpDocumentType.APPENDIX_12, now.minusDays(2).toString()));
        documents.add(buildDwpDocument("otherDoc2", DwpDocumentType.APPENDIX_12, now.minusDays(1).toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().dwpDocuments(documents).build();
        sscsCaseData.sortCollections();

        assertEquals("otherDoc", sscsCaseData.getDwpDocuments().get(0).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("testUrl", sscsCaseData.getDwpDocuments().get(1).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("otherDoc2", sscsCaseData.getDwpDocuments().get(2).getValue().getDocumentLink().getDocumentUrl());
        assertEquals("anotherTestUrl", sscsCaseData.getDwpDocuments().get(3).getValue().getDocumentLink().getDocumentUrl());
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
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertTrue("Result has a value", result.isPresent());
        assertEquals("testUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleWelshDocumentsOfSameType_thenSelectTheEarliestDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("earliestTestUrl", DocumentType.DECISION_NOTICE, now.minusDays(2).toString()));
        documents.add(buildWelshSscsDocument("newTestUrl", DocumentType.DECISION_NOTICE, now.toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertTrue("Result has a value", result.isPresent());
        assertEquals("earliestTestUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasMultipleWelshDocumentsOfSameTypeWithTwoOnSameDay_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result  = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertTrue("Result has a value", result.isPresent());
        assertEquals("latestTestUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
    }

    @Test
    public void givenACaseHasNoWelshDocuments_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(null).build();
        Optional<SscsWelshDocument> result  = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertTrue("Result is empty", result.isEmpty());
    }

    @Test
    public void givenACaseHasMultipleWelshDocumentsOfDifferentTypes_thenSelectTheEarliestDocumentForDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("earliestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("newUrl", DocumentType.DIRECTION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result  = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DIRECTION_NOTICE);

        assertTrue("Result has a value", result.isPresent());
        assertEquals("earliestTestUrl", result.get().getValue().getDocumentLink().getDocumentUrl());
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

    @Test
    @Parameters({"TRANSLATION_REQUESTED", "TRANSLATION_REQUIRED", "null"})
    public void givenACaseHasMultipleDwpDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly3(@Nullable SscsDocumentTranslationStatus translationStatus) {
        List<DwpDocument> documents = new ArrayList<>();
        documents.add(buildDwpDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildDwpDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildDwpDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildDwpDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, translationStatus));

        SscsCaseData sscsCaseData = SscsCaseData.builder().dwpDocuments(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        if (translationStatus == null) {
            assertEquals(NO.getValue(), sscsCaseData.getTranslationWorkOutstanding());
        } else {
            assertEquals(YES.getValue(), sscsCaseData.getTranslationWorkOutstanding());
        }
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

    private SscsWelshDocument buildWelshSscsDocument(String documentUrl, DocumentType documentType, String date) {
        String docType = documentType == null ? null : documentType.getValue();
        return SscsWelshDocument.builder().value(
                SscsWelshDocumentDetails.builder().documentType(docType)
                        .documentLink(DocumentLink.builder().documentUrl(documentUrl).build())
                        .documentDateAdded(date)
                        .build()).build();
    }

    private DwpDocument buildDwpDocument(String documentUrl, DocumentType documentType, String date, String bundleAddition, SscsDocumentTranslationStatus translationStatus) {
        String docType = documentType == null ? null : documentType.getValue();
        return DwpDocument.builder().value(
                DwpDocumentDetails.builder().documentType(docType)
                        .documentLink(DocumentLink.builder().documentUrl(documentUrl).build())
                        .documentDateAdded(date)
                        .bundleAddition(bundleAddition)
                        .documentTranslationStatus(translationStatus)
                        .build()).build();
    }

    private DwpDocument buildDwpDocument(String documentUrl, DwpDocumentType documentType, String date) {
        String docType = documentType == null ? null : documentType.getValue();
        return DwpDocument.builder().value(
                DwpDocumentDetails.builder().documentType(docType)
                        .documentLink(DocumentLink.builder().documentUrl(documentUrl).build())
                        .documentDateAdded(date)
                        .build()).build();
    }

    private ScannedDocument buildScannedDocument(String documentUrl, DocumentType documentType, String date, String controlNumber) {
        String docType = documentType == null ? null : documentType.getValue();
        return ScannedDocument.builder()
                .value(ScannedDocumentDetails.builder()
                        .type(docType)
                        .url(DocumentLink.builder()
                                .documentUrl(documentUrl)
                                .build())
                        .scannedDate(date)
                        .controlNumber(controlNumber)
                        .build())
                .build();
    }

    @Test
    public void givenLanguagePreferenceWelshIsNull_thenIsLanguagePreferenceWelshShouldReturnFalse() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh(null).build();
        assertEquals(sscsCaseData.isLanguagePreferenceWelsh(), Boolean.FALSE);
    }

    @Test
    public void givenLanguagePreferenceWelshIsYes_thenIsLanguagePreferenceWelshShouldReturnTrue() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("Yes").build();
        assertEquals(sscsCaseData.isLanguagePreferenceWelsh(), Boolean.TRUE);
    }

    @Test
    public void givenLanguagePreferenceWelshIsNo_thenIsLanguagePreferenceWelshShouldReturnFalse() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("No").build();
        assertEquals(sscsCaseData.isLanguagePreferenceWelsh(), Boolean.FALSE);
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

    @Test
    public void givenBenefitTypeEsaUpperCase_thenShouldReturnBenefitTypeEsa() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("ESA").build()).build()).build();
        assertEquals(Optional.of(Benefit.ESA), sscsCaseData.getBenefitType());
        assertTrue(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertFalse(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenBenefitTypeEsaLowerCase_thenShouldReturnBenefitTypeEsa() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("esa").build()).build()).build();
        assertEquals(Optional.of(Benefit.ESA), sscsCaseData.getBenefitType());
        assertTrue(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertFalse(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenBenefitTypePipUpperCase_thenShouldReturnBenefitTypePip() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).build()).build();
        assertEquals(Optional.of(Benefit.PIP), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertTrue(sscsCaseData.isBenefitType(Benefit.PIP));
        assertFalse(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenBenefitTypePipLowerCase_thenShouldReturnBenefitTypePip() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("pip").build()).build()).build();
        assertEquals(Optional.of(Benefit.PIP), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertTrue(sscsCaseData.isBenefitType(Benefit.PIP));
        assertFalse(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenBenefitTypeUcUpperCase_thenShouldReturnBenefitTypeUc() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("UC").build()).build()).build();
        assertEquals(Optional.of(Benefit.UC), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertTrue(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenBenefitTypeUcLowerCase_thenShouldReturnBenefitTypeUc() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("uc").build()).build()).build();
        assertEquals(Optional.of(Benefit.UC), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertTrue(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenBenefitTypeCarersAllowanceCase_thenShouldReturnBenefitTypeCarersAllowance() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().code("carersAllowance").build()).build()).build();
        assertEquals(Optional.of(Benefit.CARERS_ALLOWANCE), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertTrue(sscsCaseData.isBenefitType(Benefit.CARERS_ALLOWANCE));
    }

    @Test
    public void givenNoBenefitTypeCode_thenShouldReturnEmptyOptional() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().benefitType(BenefitType.builder().build()).build()).build();
        assertEquals(Optional.empty(), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertFalse(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenNoBenefitType_thenShouldReturnEmptyOptional() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().build()).build();
        assertEquals(Optional.empty(), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertFalse(sscsCaseData.isBenefitType(Benefit.UC));
    }

    @Test
    public void givenNoAppeal_thenShouldReturnEmptyOptional() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        assertEquals(Optional.empty(), sscsCaseData.getBenefitType());
        assertFalse(sscsCaseData.isBenefitType(Benefit.ESA));
        assertFalse(sscsCaseData.isBenefitType(Benefit.PIP));
        assertFalse(sscsCaseData.isBenefitType(Benefit.UC));
    }

    public void givenACaseDoesNotHaveReasonableAdjustmentLetters_ThenFlagIsNo() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(null).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertEquals(NO, sscsCaseData.getReasonableAdjustmentsOutstanding());
    }

    @Test
    public void givenACaseHasReasonableAdjustmentsLettersRequired_ThenFlagIsYes() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.REQUIRED).build()).build());

        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters).build()).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertEquals(YES, sscsCaseData.getReasonableAdjustmentsOutstanding());
    }

    @Test
    public void givenACaseHasReasonableAdjustmentsLettersStatusIsNull_ThenFlagIsYes() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(null).build()).build());

        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters).build()).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertEquals(YES, sscsCaseData.getReasonableAdjustmentsOutstanding());
    }

    @Test
    public void givenACaseHasNoReasonableAdjustmentsLettersRequired_ThenFlagIsNo() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build()).build());

        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters).build()).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertEquals(NO, sscsCaseData.getReasonableAdjustmentsOutstanding());
    }

    @Test
    public void givenACaseHasWithReasonableAdjustmentsLettersForMultipleParties_ThenFlagIsYes() {
        List<Correspondence> letters1 = new ArrayList<>();
        letters1.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build()).build());

        List<Correspondence> letters2 = new ArrayList<>();
        letters2.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.REQUIRED).build()).build());

        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters1).jointParty(letters2).build()).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertEquals(YES, sscsCaseData.getReasonableAdjustmentsOutstanding());
    }

    @Test
    public void givenACaseWithRequiredReasonableAdjustmentsLettersForOtherParties_ThenFlagIsYes() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build()).build());
        letters.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.REQUIRED).build()).build());

        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().otherParty(letters).build()).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertEquals(YES, sscsCaseData.getReasonableAdjustmentsOutstanding());
    }

    @Test
    public void givenACaseWithActionedReasonableAdjustmentsLettersForOtherParties_ThenFlagIsNo() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build()).build());
        letters.add(Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build()).build());

        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().otherParty(letters).build()).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertEquals(NO, sscsCaseData.getReasonableAdjustmentsOutstanding());
    }

    @Test
    public void givenNoDateSentToGapsOrDateTimeReturnNone() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().dateCaseSentToGaps(null).dateTimeCaseSentToGaps(null).build();
        assertTrue(sscsCaseData.getDateTimeSentToGaps().isEmpty());
    }

    @Test
    public void givenDateSentToGapsAndNoDateTimeReturnEmpty() {

        LocalDate today = LocalDate.now();

        SscsCaseData sscsCaseData = SscsCaseData.builder().dateCaseSentToGaps(today.toString()).dateTimeCaseSentToGaps(null).build();

        assertTrue(sscsCaseData.getDateTimeSentToGaps().isEmpty());
    }

    @Test
    public void givenNoDateSentToGapsAndDateTimeReturnDateTime() {
        LocalDateTime now = LocalDateTime.parse("2020-05-22 20:30:23", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SscsCaseData sscsCaseData = SscsCaseData.builder().dateCaseSentToGaps(null).dateTimeCaseSentToGaps(now.toString()).build();
        assertEquals(now, sscsCaseData.getDateTimeSentToGaps().get());
    }

    @Test
    public void givenDateSentToGapsAndDateTimeReturnDateTime() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.parse("2020-05-22 20:30:23", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SscsCaseData sscsCaseData = SscsCaseData.builder().dateCaseSentToGaps(today.toString()).dateTimeCaseSentToGaps(now.toString()).build();
        assertEquals(now, sscsCaseData.getDateTimeSentToGaps().get());
    }

    @Test
    public void givenMultipleHearingsAvailable_ThenEnsureGetLatestHearingSortsByHearingIdThenHearingDateTimeThenHearingRequested() {
        // given
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ArrayList<Hearing> hearings = new ArrayList<>();

        Hearing h1 = Hearing.builder().value(HearingDetails.builder()
                .hearingRequested(LocalDateTime.parse("2022-09-05 10:30:00", pattern))
                .hearingDate("2022-09-01").time("11:45:30")
                .hearingId("3").build()).build();
        Hearing h2 = Hearing.builder().value(HearingDetails.builder()
                .hearingRequested(LocalDateTime.parse("2022-09-01 10:30:00", pattern))
                .hearingDate("2022-09-08").time("11:45:30")
                .hearingId("4").build()).build();
        Hearing expectedValue = Hearing.builder().value(HearingDetails.builder()
                .hearingRequested(LocalDateTime.parse("2022-09-07 10:30:00", pattern))
                .hearingDate("2022-09-09").time("11:45:30")
                .hearingId("4").build()).build();
        Hearing h4 = Hearing.builder().value(HearingDetails.builder()
                .hearingRequested(LocalDateTime.parse("2022-09-03 10:30:00", pattern))
                .hearingDate("2022-09-09").time("11:45:30")
                .hearingId("4").build()).build();
        Hearing h5 = Hearing.builder().value(HearingDetails.builder()
                .hearingRequested(LocalDateTime.parse("2022-09-03 10:30:00", pattern))
                .hearingDate("2022-09-08").time("11:45:30")
                .hearingId("2").build()).build();
        Hearing h6 = Hearing.builder().value(HearingDetails.builder()
                .hearingRequested(LocalDateTime.parse("2022-09-03 10:30:00", pattern))
                .hearingDate("2022-09-08").time("11:45:30")
                .hearingId("3").build()).build();
        Hearing h7 = Hearing.builder().value(HearingDetails.builder()
                .hearingRequested(LocalDateTime.parse("2022-09-03 10:30:00", pattern))
                .hearingDate("2022-09-08").time("11:45:30")
                .hearingId("1").build()).build();

        hearings.add(h1);
        hearings.add(h2);
        hearings.add(expectedValue);
        hearings.add(h4);
        hearings.add(h5);
        hearings.add(h6);
        hearings.add(h7);

        SscsCaseData caseData = SscsCaseData.builder().hearings(hearings).build();

        // when
        Hearing actualValue = caseData.getLatestHearing();

        // then
        assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenRepresentative_thenIsThereARepresentativeIsTrue() {
        var representative = Representative.builder().hasRepresentative(YES.getValue()).build();
        var appeal = Appeal.builder().rep(representative).build();
        var caseData = SscsCaseData.builder().appeal(appeal).build();

        assertTrue(caseData.isThereARepresentative());
    }

    @Test
    public void givenNoRepresentative_thenIsThereARepresentativeIsFalse() {
        var appeal = Appeal.builder().build();
        var caseData = SscsCaseData.builder().appeal(appeal).build();

        assertFalse(caseData.isThereARepresentative());
    }

    @Test
    public void givenHasRepresentativeIsNo_thenIsThereARepresentativeIsFalse() {
        var representative = Representative.builder().hasRepresentative(NO.getValue()).build();
        var appeal = Appeal.builder().rep(representative).build();
        var caseData = SscsCaseData.builder().appeal(appeal).build();

        assertFalse(caseData.isThereARepresentative());
    }

    @Test
    public void givenHasJointPartyIsYes_thenIsThereAJointPartyIsTrue() {
        var jointParty = JointParty.builder().hasJointParty(YES).build();
        var caseData = SscsCaseData.builder().jointParty(jointParty).build();

        assertTrue(caseData.isThereAJointParty());
    }

    @Test
    public void givenHasJointPartyIsNo_thenIsThereAJointPartyIsFalse() {
        var jointParty = JointParty.builder().hasJointParty(NO).build();
        var caseData = SscsCaseData.builder().jointParty(jointParty).build();

        assertFalse(caseData.isThereAJointParty());
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.CHILD_BENEFIT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.CHILD_SUPPORT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.INFECTED_BLOOD_COMPENSATION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.NO;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.YES;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType;
import uk.gov.hmcts.reform.sscs.ccd.callback.DwpDocumentType;

class SscsCaseDataTest {

    private final LocalDate now = LocalDate.now();
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {

        Jackson2ObjectMapperBuilder objectMapperBuilder =
            new Jackson2ObjectMapperBuilder()
                .featuresToEnable(READ_ENUMS_USING_TO_STRING)
                .featuresToEnable(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .featuresToEnable(WRITE_ENUMS_USING_TO_STRING)
                .serializationInclusion(JsonInclude.Include.NON_ABSENT);

        mapper = objectMapperBuilder.createXmlMapper(false).build();
        mapper.findAndRegisterModules();
    }

    @Test
    void sortSscsDocuments(){

        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("sscsDocumentSorting.json")).getFile();
        String json;
        try {
            json = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new AssertionError("IOException should not be thrown", e);
        }
        List<SscsDocument> newSscsDocument;
        try {
            newSscsDocument = mapper.readValue(json, new TypeReference<List<SscsDocument>>() {
            });
        } catch (IOException e) {
            throw new AssertionError("IOException should not be thrown", e);
        }
        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(newSscsDocument).build();
        sscsCaseData.sortCollections();
        assertThat(sscsCaseData.getSscsDocument()).isNotEmpty();
        
    }

    @Test
    void sortHearingsByDateWhenIdsAreBlank() {
        List<Hearing> hearings = new ArrayList<>();
        Hearing hearing1 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingDate("2019-01-01").time("12:00").build())
            .build();
        Hearing hearing2 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingDate("2019-03-01").time("12:00").build())
            .build();
        Hearing hearing3 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingDate("2019-02-01").time("12:00").build())
            .build();
        hearings.add(hearing1);
        hearings.add(hearing2);
        hearings.add(hearing3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().hearings(hearings).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getHearings().get(0).getValue().getHearingDate()).isEqualTo("2019-03-01");
        assertThat(sscsCaseData.getHearings().get(1).getValue().getHearingDate()).isEqualTo("2019-02-01");
        assertThat(sscsCaseData.getHearings().get(2).getValue().getHearingDate()).isEqualTo("2019-01-01");
    }

    @Test
    void sortHearingsByIdFirstThenDate() {
        List<Hearing> hearings = new ArrayList<>();
        Hearing hearing1 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingId("2").hearingDate("2019-04-01").time("12:00").build())
            .build();
        Hearing hearing2 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingId("20").hearingDate("2019-03-01").time("12:00").build())
            .build();
        Hearing hearing3 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingId("20").hearingDate("2019-02-01").time("12:00").build())
            .build();
        Hearing hearing4 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingId("3").hearingDate("2019-02-01").time("12:00").build())
            .build();

        hearings.add(hearing1);
        hearings.add(hearing2);
        hearings.add(hearing3);
        hearings.add(hearing4);

        SscsCaseData sscsCaseData = SscsCaseData.builder().hearings(hearings).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getHearings().get(0).getValue().getHearingDate()).isEqualTo("2019-03-01");
        assertThat(sscsCaseData.getHearings().get(0).getValue().getHearingId()).isEqualTo("20");
        assertThat(sscsCaseData.getHearings().get(1).getValue().getHearingDate()).isEqualTo("2019-02-01");
        assertThat(sscsCaseData.getHearings().get(1).getValue().getHearingId()).isEqualTo("20");
        assertThat(sscsCaseData.getHearings().get(2).getValue().getHearingDate()).isEqualTo("2019-02-01");
        assertThat(sscsCaseData.getHearings().get(2).getValue().getHearingId()).isEqualTo("3");
        assertThat(sscsCaseData.getHearings().get(3).getValue().getHearingDate()).isEqualTo("2019-04-01");
        assertThat(sscsCaseData.getHearings().get(3).getValue().getHearingId()).isEqualTo("2");
    }

    @Test
    void sortHearingsByIdWhenFewHearingsHaveBlankId() {
        List<Hearing> hearings = new ArrayList<>();
        Hearing hearing1 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingDate("2019-04-01").time("12:00").build())
            .build();
        Hearing hearing2 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingDate("2019-05-01").time("12:00").build())
            .build();
        Hearing hearing3 = Hearing
            .builder()
            .value(HearingDetails.builder().hearingId("1").hearingDate("2019-02-01").time("12:00").build())
            .build();
        hearings.add(hearing1);
        hearings.add(hearing2);
        hearings.add(hearing3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().hearings(hearings).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getHearings().get(0).getValue().getHearingDate()).isEqualTo("2019-02-01");
        assertThat(sscsCaseData.getHearings().get(0).getValue().getHearingId()).isEqualTo("1");
        assertThat(sscsCaseData.getHearings().get(1).getValue().getHearingDate()).isEqualTo("2019-05-01");
        assertThat(sscsCaseData.getHearings().get(1).getValue().getHearingId()).isNull();
        assertThat(sscsCaseData.getHearings().get(2).getValue().getHearingDate()).isEqualTo("2019-04-01");
        assertThat(sscsCaseData.getHearings().get(2).getValue().getHearingId()).isNull();
    }

    @Test
    void sortEventsByDate() {
        List<Event> events = new ArrayList<>();
        Event event1 = Event.builder().value(EventDetails.builder().date("2019-01-01").build()).build();
        Event event2 = Event.builder().value(EventDetails.builder().date("2019-03-01").build()).build();
        Event event3 = Event.builder().value(EventDetails.builder().date("2019-02-01").build()).build();
        events.add(event1);
        events.add(event2);
        events.add(event3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().events(events).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getEvents().get(0).getValue().getDate()).isEqualTo("2019-03-01");
        assertThat(sscsCaseData.getEvents().get(1).getValue().getDate()).isEqualTo("2019-02-01");
        assertThat(sscsCaseData.getEvents().get(2).getValue().getDate()).isEqualTo("2019-01-01");
    }

    @Test
    void sortEvidenceByDate() {
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

        assertThat(sscsCaseData.getEvidence().getDocuments().get(0).getValue().getDateReceived()).isEqualTo("2019-03-01");
        assertThat(sscsCaseData.getEvidence().getDocuments().get(1).getValue().getDateReceived()).isEqualTo("2019-02-01");
        assertThat(sscsCaseData.getEvidence().getDocuments().get(2).getValue().getDateReceived()).isEqualTo("2019-01-01");
        assertThat(sscsCaseData.getEvidence().getDocuments().get(3).getValue().getDateReceived()).isNull();
        assertThat(sscsCaseData.getEvidence().getDocuments().get(4).getValue().getDateReceived()).isEqualTo("NaN");
    }


    @Test
    void sortCorrespondenceByDateAndTime() {
        List<Correspondence> correspondence = new ArrayList<>();
        Correspondence correspondence1 = Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().sentOn("1 Feb 2019 11:22").build())
            .build();
        Correspondence correspondence2 = Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().sentOn("1 Jan 2019 11:22").build())
            .build();
        Correspondence correspondence3 = Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().sentOn("1 Jan 2019 11:23").build())
            .build();

        correspondence.add(correspondence1);
        correspondence.add(correspondence2);
        correspondence.add(correspondence3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().correspondence(correspondence).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getCorrespondence().get(0).getValue().getSentOn()).isEqualTo("1 Feb 2019 11:22");
        assertThat(sscsCaseData.getCorrespondence().get(1).getValue().getSentOn()).isEqualTo("1 Jan 2019 11:23");
        assertThat(sscsCaseData.getCorrespondence().get(2).getValue().getSentOn()).isEqualTo("1 Jan 2019 11:22");
    }

    @Test
    void sortSscsDocumentsByDateAdded() {
        List<SscsDocument> sscsDocuments = new ArrayList<>();
        SscsDocument sscsDocument1 = SscsDocument
            .builder()
            .value(SscsDocumentDetails.builder().documentDateAdded("2019-01-01").build())
            .build();
        SscsDocument sscsDocument2 = SscsDocument
            .builder()
            .value(SscsDocumentDetails.builder().documentDateAdded("2019-03-01").build())
            .build();
        SscsDocument sscsDocument3 = SscsDocument
            .builder()
            .value(SscsDocumentDetails.builder().documentDateAdded("2019-01-01").build())
            .build();
        sscsDocuments.add(sscsDocument1);
        sscsDocuments.add(sscsDocument2);
        sscsDocuments.add(sscsDocument3);

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(sscsDocuments).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getSscsDocument().get(0).getValue().getDocumentDateAdded()).isEqualTo("2019-01-01");
        assertThat(sscsCaseData.getSscsDocument().get(1).getValue().getDocumentDateAdded()).isEqualTo("2019-01-01");
        assertThat(sscsCaseData.getSscsDocument().get(2).getValue().getDocumentDateAdded()).isEqualTo("2019-03-01");
    }

    @Test
    void shouldCreateInfoRequest() throws IOException {
        String expectedValue = "{\"appellantInfoRequestCollection\":[{\"value\":{\"appellantInfoParagraph\"" +
            ":\"Par1\",\"appellantInfoRequestDate\":\"date1\"},\"id\":null}]}";
        List<AppellantInfoRequest> appellantInfoRequests = new ArrayList<>();
        AppellantInfoRequest appellantInfoRequest1 = AppellantInfoRequest.builder()
                                                                         .appellantInfo(AppellantInfo
                                                                             .builder()
                                                                             .paragraph("Par1")
                                                                             .requestDate("date1")
                                                                             .build()).build();

        appellantInfoRequests.add(appellantInfoRequest1);

        SscsCaseData sscsCaseData = SscsCaseData.builder().infoRequests(InfoRequests
            .builder()
            .appellantInfoRequest(appellantInfoRequests)
            .build()).build();

        ObjectMapper ob = new ObjectMapper();
        String infoRequestValue = ob.writeValueAsString(sscsCaseData.getInfoRequests());

        assertThat(infoRequestValue).isEqualTo(expectedValue);
    }

    @Test
    void givenACaseHasOneDocument_thenSelectThisDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result.getValue().getDocumentLink().getDocumentUrl()).isEqualTo("testUrl");
    }

    @Test
    void givenACaseHasMultipleDocumentsOfSameType_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("oldTestUrl", DocumentType.DECISION_NOTICE, now.minusDays(2).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result.getValue().getDocumentLink().getDocumentUrl()).isEqualTo("latestTestUrl");
    }

    @Test
    void givenACaseHasMultipleDocumentsOfSameTypeWithBundleFieldPopulated_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString(), "A", null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), "B", null));
        documents.add(buildSscsDocument("oldTestUrl", DocumentType.DECISION_NOTICE, now.minusDays(2).toString(), "C", null));
        documents.add(buildSscsDocument("noDateOldTestUrl", DocumentType.DECISION_NOTICE, null, "D", null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result.getValue().getDocumentLink().getDocumentUrl()).isEqualTo("latestTestUrl");
    }

    @Test
    void givenACaseHasMultipleDocumentsOfSameTypeWithTwoOnSameDay_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result.getValue().getDocumentLink().getDocumentUrl()).isEqualTo("latestTestUrl");
    }

    @Test
    void givenACaseHasMultipleDocumentsOfDifferentTypes_thenSelectTheLatestDocumentForDocumentTypeEntered() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(
            buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(
            buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DIRECTION_NOTICE);

        assertThat(result.getValue().getDocumentLink().getDocumentUrl()).isEqualTo("latestTestUrl");
    }

    @Test
    void givenACaseWithMultipleDocuments_thenSortByDateAdded() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("otherDoc");
        assertThat(sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("testUrl");
        assertThat(sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("otherDoc2");
        assertThat(sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl()).isEqualTo(
            "anotherTestUrl");
    }

    @Test
    void givenACaseWithMultipleDocumentsAndOneDocAddedDateIsEmpty_thenSortByDateAddedAndPutEmptyDocumentLast() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("emptyDateAddedDoc", DocumentType.OTHER_DOCUMENT, null, null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("otherDoc");
        assertThat(sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("testUrl");
        assertThat(sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("otherDoc2");
        assertThat(sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl()).isEqualTo(
            "anotherTestUrl");
        assertThat(sscsCaseData.getSscsDocument().get(4).getValue().getDocumentLink().getDocumentUrl()).isEqualTo(
            "emptyDateAddedDoc");

    }

    @Test
    void givenACaseHasMultipleDocumentsOfSameTypeOnSameDayWithBundleAdditions_thenSortByBundleLetter() {
        List<SscsDocument> documents = new ArrayList<>();

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

        assertThat(sscsCaseData.getSscsDocument().get(0).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("A");
        assertThat(sscsCaseData.getSscsDocument().get(1).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("B");
        assertThat(sscsCaseData.getSscsDocument().get(2).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("C");
        assertThat(sscsCaseData.getSscsDocument().get(3).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("D");
        assertThat(sscsCaseData.getSscsDocument().get(4).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("Z");
        assertThat(sscsCaseData.getSscsDocument().get(5).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("Z1");
        assertThat(sscsCaseData.getSscsDocument().get(6).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("Z2");
        assertThat(sscsCaseData.getSscsDocument().get(7).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("Z11");
        assertThat(sscsCaseData.getSscsDocument().get(8).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("Z19");
        assertThat(sscsCaseData.getSscsDocument().get(9).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("Z20");
    }

    @Test
    void givenACaseHasScannedMultipleDocumentsOfSameDayWithControlNumber_thenSortByControlNumber() {
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

        assertThat(sscsCaseData.getScannedDocuments().get(0).getValue().getUrl().getDocumentUrl()).isEqualTo("3000");
        assertThat(sscsCaseData.getScannedDocuments().get(1).getValue().getUrl().getDocumentUrl()).isEqualTo("1000");
        assertThat(sscsCaseData.getScannedDocuments().get(2).getValue().getUrl().getDocumentUrl()).isEqualTo("2000");
        assertThat(sscsCaseData.getScannedDocuments().get(3).getValue().getUrl().getDocumentUrl()).isEqualTo("4000");
        assertThat(sscsCaseData.getScannedDocuments().get(4).getValue().getUrl().getDocumentUrl()).isEqualTo("5000");
        assertThat(sscsCaseData.getScannedDocuments().get(5).getValue().getUrl().getDocumentUrl()).isEqualTo("6000");
        assertThat(sscsCaseData.getScannedDocuments().get(6).getValue().getUrl().getDocumentUrl()).isEqualTo("7000");
        assertThat(sscsCaseData.getScannedDocuments().get(7).getValue().getUrl().getDocumentUrl()).isEqualTo("8000");
        assertThat(sscsCaseData.getScannedDocuments().get(8).getValue().getUrl().getDocumentUrl()).isEqualTo("dummyString");
    }

    @Test
    void givenACaseHasMultipleDocumentsWithNoDate_thenSelectTheLastOneItFinds() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, null, null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result.getValue().getDocumentLink().getDocumentUrl()).isEqualTo("latestTestUrl");
    }

    @Test
    void givenACaseWithMultipleDwpDocuments_thenSortByDateAdded() {
        List<DwpDocument> documents = new ArrayList<>();
        documents.add(buildDwpDocument("testUrl", DwpDocumentType.UCB, now.minusDays(1).toString()));
        documents.add(buildDwpDocument("anotherTestUrl", DwpDocumentType.UCB, now.toString()));
        documents.add(buildDwpDocument("otherDoc", DwpDocumentType.APPENDIX_12, now.minusDays(2).toString()));
        documents.add(buildDwpDocument("otherDoc2", DwpDocumentType.APPENDIX_12, now.minusDays(1).toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().dwpDocuments(documents).build();
        sscsCaseData.sortCollections();

        assertThat(sscsCaseData.getDwpDocuments().get(0).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("otherDoc");
        assertThat(sscsCaseData.getDwpDocuments().get(1).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("testUrl");
        assertThat(sscsCaseData.getDwpDocuments().get(2).getValue().getDocumentLink().getDocumentUrl()).isEqualTo("otherDoc2");
        assertThat(sscsCaseData.getDwpDocuments().get(3).getValue().getDocumentLink().getDocumentUrl()).isEqualTo(
            "anotherTestUrl");
    }

    @Test
    void givenADocumentTypeIsNull_thenHandleCorrectly() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", null, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("testUrl2", DocumentType.DECISION_NOTICE, now.minusDays(2).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        SscsDocument result = sscsCaseData.getLatestDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result.getValue().getDocumentLink().getDocumentUrl()).isEqualTo("testUrl2");
    }


    @Test
    void givenACaseHasOneWelshDocument_thenSelectThisDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result).isPresent();
        assertThat(result.get().getValue().getDocumentLink().getDocumentUrl()).isEqualTo("testUrl");
    }

    @Test
    void givenACaseHasMultipleWelshDocumentsOfSameType_thenSelectTheEarliestDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("earliestTestUrl", DocumentType.DECISION_NOTICE, now.minusDays(2).toString()));
        documents.add(buildWelshSscsDocument("newTestUrl", DocumentType.DECISION_NOTICE, now.toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result).isPresent();
        assertThat(result.get().getValue().getDocumentLink().getDocumentUrl()).isEqualTo("earliestTestUrl");
    }

    @Test
    void givenACaseHasMultipleWelshDocumentsOfSameTypeWithTwoOnSameDay_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("latestTestUrl", DocumentType.DECISION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl", DocumentType.DECISION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl2", DocumentType.DECISION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DECISION_NOTICE, now.toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result).isPresent();
        assertThat(result.get().getValue().getDocumentLink().getDocumentUrl()).isEqualTo("latestTestUrl");
    }

    @Test
    void givenACaseHasNoWelshDocuments_thenSelectTheLatestDocumentWhenDocumentTypeEntered() {

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(null).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DECISION_NOTICE);

        assertThat(result).isEmpty();
    }

    @Test
    void givenACaseHasMultipleWelshDocumentsOfDifferentTypes_thenSelectTheEarliestDocumentForDocumentTypeEntered() {
        List<SscsWelshDocument> documents = new ArrayList<>();
        documents.add(buildWelshSscsDocument("earliestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("testUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("newUrl", DocumentType.DIRECTION_NOTICE, now.toString()));
        documents.add(buildWelshSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString()));
        documents.add(buildWelshSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString()));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsWelshDocuments(documents).build();
        Optional<SscsWelshDocument> result = sscsCaseData.getLatestWelshDocumentForDocumentType(DocumentType.DIRECTION_NOTICE);

        assertThat(result).isPresent();
        assertThat(result.get().getValue().getDocumentLink().getDocumentUrl()).isEqualTo("earliestTestUrl");
    }


    @Test
    void givenACaseHasMultipleSscsDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null,
            SscsDocumentTranslationStatus.TRANSLATION_REQUESTED));
        documents.add(
            buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        assertThat(sscsCaseData.getTranslationWorkOutstanding()).isEqualTo("Yes");
    }

    @Test
    void givenACaseHasMultipleSscsDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly2() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(
            buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(
            buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null, null));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null, null));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null, null));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null, null));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        assertThat(sscsCaseData.getTranslationWorkOutstanding()).isEqualTo("No");
    }

    @Test
    void givenACaseHasMultipleSscsDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly3() {
        List<SscsDocument> documents = new ArrayList<>();
        documents.add(buildSscsDocument("testUrl", DocumentType.DECISION_NOTICE, null, null, null));
        documents.add(buildSscsDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null,
            SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null,
            SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("latestTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(), null,
            SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("oldUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(2).toString(), null,
            SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("otherDoc", DocumentType.OTHER_DOCUMENT, now.toString(), null,
            SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildSscsDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), null,
            SscsDocumentTranslationStatus.TRANSLATION_REQUIRED));

        SscsCaseData sscsCaseData = SscsCaseData.builder().sscsDocument(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        assertThat(sscsCaseData.getTranslationWorkOutstanding()).isEqualTo("Yes");
    }

    @ParameterizedTest
    @ValueSource(strings = {"TRANSLATION_REQUESTED", "TRANSLATION_REQUIRED", "null"})
    void givenACaseHasMultipleDwpDocumentsShouldUpdateTheTranslationWorkOutstandingFlagCorrectly3(
        String translationStatusString) {
        SscsDocumentTranslationStatus translationStatus = "null".equals(
            translationStatusString) ? null : SscsDocumentTranslationStatus.valueOf(translationStatusString);

        List<DwpDocument> documents = new ArrayList<>();
        documents.add(buildDwpDocument("testUrl", DocumentType.DECISION_NOTICE, null, null));
        documents.add(buildDwpDocument("anotherTestUrl", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(),
            SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(buildDwpDocument("anotherTestUrl2", DocumentType.DIRECTION_NOTICE, now.minusDays(1).toString(),
            SscsDocumentTranslationStatus.TRANSLATION_COMPLETE));
        documents.add(
            buildDwpDocument("otherDoc2", DocumentType.OTHER_DOCUMENT, now.minusDays(1).toString(), translationStatus));

        SscsCaseData sscsCaseData = SscsCaseData.builder().dwpDocuments(documents).build();
        sscsCaseData.updateTranslationWorkOutstandingFlag();
        if (translationStatus == null) {
            assertThat(sscsCaseData.getTranslationWorkOutstanding()).isEqualTo(NO.getValue());
        } else {
            assertThat(sscsCaseData.getTranslationWorkOutstanding()).isEqualTo(YES.getValue());
        }
    }

    private SscsDocument buildSscsDocument(String documentUrl, DocumentType documentType, String date, String bundleAddition,
        SscsDocumentTranslationStatus translationStatus) {
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

    private DwpDocument buildDwpDocument(String documentUrl, DocumentType documentType, String date,
        SscsDocumentTranslationStatus translationStatus) {
        String docType = documentType == null ? null : documentType.getValue();
        return DwpDocument.builder().value(
            DwpDocumentDetails.builder().documentType(docType)
                              .documentLink(DocumentLink.builder().documentUrl(documentUrl).build())
                              .documentDateAdded(date)
                              .bundleAddition(null)
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

    private ScannedDocument buildScannedDocument(String documentUrl, DocumentType documentType, String date,
        String controlNumber) {
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
    void givenLanguagePreferenceWelshIsNull_thenIsLanguagePreferenceWelshShouldReturnFalse() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh(null).build();
        assertThat(sscsCaseData.isLanguagePreferenceWelsh()).isFalse();
    }

    @Test
    void givenLanguagePreferenceWelshIsYes_thenIsLanguagePreferenceWelshShouldReturnTrue() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("Yes").build();
        assertThat(sscsCaseData.isLanguagePreferenceWelsh()).isTrue();
    }

    @Test
    void givenLanguagePreferenceWelshIsNo_thenIsLanguagePreferenceWelshShouldReturnFalse() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("No").build();
        assertThat(sscsCaseData.isLanguagePreferenceWelsh()).isFalse();
    }

    @Test
    void givenLanguagePreferenceWelshIsNull_thenIsLanguagePreferenceWelshShouldReturnEnglish() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh(null).build();
        assertThat(sscsCaseData.getLanguagePreference()).isEqualTo(LanguagePreference.ENGLISH);
    }

    @Test
    void givenLanguagePreferenceWelshIsNo_thenIsLanguagePreferenceWelshShouldReturnEnglish() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("No").build();
        assertThat(sscsCaseData.getLanguagePreference()).isEqualTo(LanguagePreference.ENGLISH);
    }

    @Test
    void givenLanguagePreferenceWelshIsYes_thenIsLanguagePreferenceWelshShouldReturnWelsh() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().languagePreferenceWelsh("Yes").build();
        assertThat(sscsCaseData.getLanguagePreference()).isEqualTo(LanguagePreference.WELSH);
    }

    @Test
    void givenUrgentHearingInfo_thenShouldReturnUrgentHearingInfo() {
        String todaysDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        String expectedUrgentHearingOutcome = "In progress";
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .urgentCase("Yes")
            .urgentHearingRegistered(todaysDate)
            .urgentHearingOutcome("In progress")
            .build();
        assertThat(sscsCaseData.getUrgentCase()).isEqualTo("Yes");
        assertThat(sscsCaseData.getUrgentHearingRegistered()).isEqualTo(todaysDate);
        assertThat(sscsCaseData.getUrgentHearingOutcome()).isEqualTo(expectedUrgentHearingOutcome);
    }

    @Test
    void givenBenefitTypeEsaUpperCase_thenShouldReturnBenefitTypeEsa() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("ESA").build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEqualTo(Optional.of(Benefit.ESA));
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isTrue();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isFalse();
    }

    @Test
    void givenBenefitTypeEsaLowerCase_thenShouldReturnBenefitTypeEsa() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("esa").build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEqualTo(Optional.of(Benefit.ESA));
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isTrue();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isFalse();
    }

    @Test
    void givenBenefitTypePipUpperCase_thenShouldReturnBenefitTypePip() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("PIP").build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEqualTo(Optional.of(Benefit.PIP));
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isTrue();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isFalse();
    }

    @Test
    void givenBenefitTypePipLowerCase_thenShouldReturnBenefitTypePip() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("pip").build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEqualTo(Optional.of(Benefit.PIP));
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isTrue();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isFalse();
    }

    @Test
    void givenBenefitTypeUcUpperCase_thenShouldReturnBenefitTypeUc() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("UC").build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEqualTo(Optional.of(Benefit.UC));
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isTrue();
    }

    @Test
    void givenBenefitTypeUcLowerCase_thenShouldReturnBenefitTypeUc() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("uc").build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEqualTo(Optional.of(Benefit.UC));
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isTrue();
    }

    @Test
    void givenBenefitTypeCarersAllowanceCase_thenShouldReturnBenefitTypeCarersAllowance() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().code("carersAllowance").build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEqualTo(Optional.of(Benefit.CARERS_ALLOWANCE));
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.CARERS_ALLOWANCE)).isTrue();
    }

    @Test
    void givenNoBenefitTypeCode_thenShouldReturnEmptyOptional() {
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .appeal(Appeal.builder().benefitType(BenefitType.builder().build()).build())
            .build();
        assertThat(sscsCaseData.getBenefitType()).isEmpty();
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isFalse();
    }

    @Test
    void givenNoBenefitType_thenShouldReturnEmptyOptional() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().appeal(Appeal.builder().build()).build();
        assertThat(sscsCaseData.getBenefitType()).isEmpty();
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isFalse();
    }

    @Test
    void givenNoAppeal_thenShouldReturnEmptyOptional() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().build();
        assertThat(sscsCaseData.getBenefitType()).isEmpty();
        assertThat(sscsCaseData.isBenefitType(Benefit.ESA)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.PIP)).isFalse();
        assertThat(sscsCaseData.isBenefitType(Benefit.UC)).isFalse();
    }

    @Test
    void givenACaseDoesNotHaveReasonableAdjustmentLetters_ThenFlagIsNo() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().reasonableAdjustmentsLetters(null).build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertThat(sscsCaseData.getReasonableAdjustmentsOutstanding()).isEqualTo(NO);
    }

    @Test
    void givenACaseHasReasonableAdjustmentsLettersRequired_ThenFlagIsYes() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.REQUIRED).build())
            .build());

        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters).build())
            .build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertThat(sscsCaseData.getReasonableAdjustmentsOutstanding()).isEqualTo(YES);
    }

    @Test
    void givenACaseHasReasonableAdjustmentsLettersStatusIsNull_ThenFlagIsYes() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(
            Correspondence.builder().value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(null).build()).build());

        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters).build())
            .build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertThat(sscsCaseData.getReasonableAdjustmentsOutstanding()).isEqualTo(YES);
    }

    @Test
    void givenACaseHasNoReasonableAdjustmentsLettersRequired_ThenFlagIsNo() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build())
            .build());

        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters).build())
            .build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertThat(sscsCaseData.getReasonableAdjustmentsOutstanding()).isEqualTo(NO);
    }

    @Test
    void givenACaseHasWithReasonableAdjustmentsLettersForMultipleParties_ThenFlagIsYes() {
        List<Correspondence> letters1 = new ArrayList<>();
        letters1.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build())
            .build());

        List<Correspondence> letters2 = new ArrayList<>();
        letters2.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.REQUIRED).build())
            .build());

        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().appellant(letters1).jointParty(letters2).build())
            .build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertThat(sscsCaseData.getReasonableAdjustmentsOutstanding()).isEqualTo(YES);
    }

    @Test
    void givenACaseWithRequiredReasonableAdjustmentsLettersForOtherParties_ThenFlagIsYes() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build())
            .build());
        letters.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.REQUIRED).build())
            .build());

        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().otherParty(letters).build())
            .build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertThat(sscsCaseData.getReasonableAdjustmentsOutstanding()).isEqualTo(YES);
    }

    @Test
    void givenACaseWithActionedReasonableAdjustmentsLettersForOtherParties_ThenFlagIsNo() {
        List<Correspondence> letters = new ArrayList<>();
        letters.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build())
            .build());
        letters.add(Correspondence
            .builder()
            .value(CorrespondenceDetails.builder().reasonableAdjustmentStatus(ReasonableAdjustmentStatus.ACTIONED).build())
            .build());

        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .reasonableAdjustmentsLetters(ReasonableAdjustmentsLetters.builder().otherParty(letters).build())
            .build();
        sscsCaseData.updateReasonableAdjustmentsOutstanding();
        assertThat(sscsCaseData.getReasonableAdjustmentsOutstanding()).isEqualTo(NO);
    }

    @Test
    void givenNoDateSentToGapsOrDateTimeReturnNone() {
        SscsCaseData sscsCaseData = SscsCaseData.builder().dateCaseSentToGaps(null).dateTimeCaseSentToGaps(null).build();
        assertThat(sscsCaseData.getDateTimeSentToGaps()).isEmpty();
    }

    @Test
    void givenDateSentToGapsAndNoDateTimeReturnEmpty() {

        LocalDate today = LocalDate.now();

        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .dateCaseSentToGaps(today.toString())
            .dateTimeCaseSentToGaps(null)
            .build();

        assertThat(sscsCaseData.getDateTimeSentToGaps()).isEmpty();
    }

    @Test
    void givenNoDateSentToGapsAndDateTimeReturnDateTime() {
        LocalDateTime dateTimeNow = LocalDateTime.parse("2020-05-22 20:30:23", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .dateCaseSentToGaps(null)
            .dateTimeCaseSentToGaps(dateTimeNow.toString())
            .build();
        assertThat(sscsCaseData.getDateTimeSentToGaps()).contains(dateTimeNow);
    }

    @Test
    void givenDateSentToGapsAndDateTimeReturnDateTime() {
        LocalDate today = LocalDate.now();
        LocalDateTime dateTimeNow = LocalDateTime.parse("2020-05-22 20:30:23", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SscsCaseData sscsCaseData = SscsCaseData
            .builder()
            .dateCaseSentToGaps(today.toString())
            .dateTimeCaseSentToGaps(dateTimeNow.toString())
            .build();
        assertThat(sscsCaseData.getDateTimeSentToGaps()).contains(dateTimeNow);
    }

    @Test
    void givenMultipleHearingsAvailable_ThenEnsureGetLatestHearingSortsByHearingIdThenHearingDateTimeThenHearingRequested() {
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
                                                                      .hearingRequested(
                                                                          LocalDateTime.parse("2022-09-07 10:30:00", pattern))
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
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void givenRepresentative_thenIsThereARepresentativeIsTrue() {
        var representative = Representative.builder().hasRepresentative(YES.getValue()).build();
        var appeal = Appeal.builder().rep(representative).build();
        var caseData = SscsCaseData.builder().appeal(appeal).build();

        assertThat(caseData.isThereARepresentative()).isTrue();
    }

    @Test
    void givenNoRepresentative_thenIsThereARepresentativeIsFalse() {
        var appeal = Appeal.builder().build();
        var caseData = SscsCaseData.builder().appeal(appeal).build();

        assertThat(caseData.isThereARepresentative()).isFalse();
    }

    @Test
    void givenHasRepresentativeIsNo_thenIsThereARepresentativeIsFalse() {
        var representative = Representative.builder().hasRepresentative(NO.getValue()).build();
        var appeal = Appeal.builder().rep(representative).build();
        var caseData = SscsCaseData.builder().appeal(appeal).build();

        assertThat(caseData.isThereARepresentative()).isFalse();
    }

    @Test
    void givenNullAppeal_thenGetAppellantOptionalReturnsEmpty() {
        var caseData = SscsCaseData.builder().build();

        assertThat(caseData.getAppellant()).isEmpty();
    }

    @Test
    void givenNullAppellant_thenGetAppellantOptionalReturnsEmpty() {
        var caseData = SscsCaseData.builder().appeal(Appeal.builder().build()).build();

        assertThat(caseData.getAppellant()).isEmpty();
    }

    @Test
    void givenAppellant_thenGetAppellantOptionalReturnsAppellant() {
        var appellant = Appellant.builder().build();
        var caseData = SscsCaseData.builder().appeal(Appeal.builder().appellant(appellant).build()).build();

        assertThat(caseData.getAppellant()).contains(appellant);
    }

    @Test
    void givenNullAppeal_thenGetAppellantConfidentialityRequiredReturnsEmpty() {
        var caseData = SscsCaseData.builder().build();

        assertThat(caseData.getAppellantConfidentialityRequired()).isEmpty();
    }

    @Test
    void givenAppellantConfidentialityRequired_thenGetAppellantConfidentialityRequiredReturnsValue() {
        var appellant = Appellant.builder().confidentialityRequirement(new DynamicList(new DynamicListItem(YesNoUnknown.YES.name(), YesNoUnknown.YES.toString()), List.of(new DynamicListItem(YesNoUnknown.YES.name(), YesNoUnknown.YES.toString())))).build();
        var caseData = SscsCaseData.builder().appeal(Appeal.builder().appellant(appellant).build()).build();

        assertThat(caseData.getAppellantConfidentialityRequired()).contains(YesNoUnknown.YES);
    }

    @Test
    void givenConfidentialCaseStatusIsSet_thenGetConfidentialCaseStatusReturnsValue() {
        var caseData = SscsCaseData.builder().build();

        caseData.setConfidentialCaseStatus(YesNoUnknown.YES);

        assertThat(caseData.getConfidentialCaseStatus()).isEqualTo(YesNoUnknown.YES);
    }

    @Test
    void givenConfidentialCaseStatusNotSet_thenGetConfidentialCaseStatusReturnsNull() {
        var caseData = SscsCaseData.builder().build();

        assertThat(caseData.getConfidentialCaseStatus()).isNull();
    }

    @Test
    void givenHasJointPartyIsYes_thenIsThereAJointPartyIsTrue() {
        var jointParty = JointParty.builder().hasJointParty(YES).build();
        var caseData = SscsCaseData.builder().jointParty(jointParty).build();

        assertThat(caseData.isThereAJointParty()).isTrue();
    }

    @Test
    void givenHasJointPartyIsNo_thenIsThereAJointPartyIsFalse() {
        var jointParty = JointParty.builder().hasJointParty(NO).build();
        var caseData = SscsCaseData.builder().jointParty(jointParty).build();

        assertThat(caseData.isThereAJointParty()).isFalse();
    }

    @Test
    void givenBenefitCodeIsSetIba_thenIsIbcIsTrue() {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                                                .benefitCode(INFECTED_BLOOD_COMPENSATION.getBenefitCode()).build();

        assertThat(sscsCaseData.isIbcCase()).isTrue();
    }

    @Test
    void givenAppealBenefitCodeIsSetIba_thenIsIbcIsTrue() {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                                                .appeal(Appeal
                                                    .builder()
                                                    .benefitType(BenefitType
                                                        .builder()
                                                        .code(INFECTED_BLOOD_COMPENSATION.getShortName())
                                                        .build())
                                                    .build()).build();

        assertThat(sscsCaseData.isIbcCase()).isTrue();
    }

    @Test
    void givenAppealBenefitCodeIsSetMidCaseCreateIba_thenIsIbcIsTrue() {
        DynamicList expectedList = new DynamicList(
            new DynamicListItem(INFECTED_BLOOD_COMPENSATION.getBenefitCode(), INFECTED_BLOOD_COMPENSATION.getShortName()),
            new ArrayList<>());

        SscsCaseData sscsCaseData = SscsCaseData.builder()
                                                .appeal(Appeal
                                                    .builder()
                                                    .benefitType(BenefitType.builder().descriptionSelection(expectedList).build())
                                                    .build()).build();

        assertThat(sscsCaseData.isIbcCase()).isTrue();
    }

    @Test
    void givenAppealBenefitCodeIsSetOnlyNonIba_thenIsIbcIsFalse() {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                                                .appeal(Appeal
                                                    .builder()
                                                    .benefitType(BenefitType.builder().code(CHILD_BENEFIT.getShortName()).build())
                                                    .build()).build();

        assertThat(sscsCaseData.isIbcCase()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"030", "034", "016"})
    void givenBenefitCodeIsSetOnlyNonIba_thenIsIbcIsFalse(String benefitCode) {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                                                .benefitCode(benefitCode).build();

        assertThat(sscsCaseData.isIbcCase()).isFalse();
    }

    @Test
    void givenElementsDisputedNull_thenGetIssueCodesForAllElementsDisputedReturnsEmptyList() {
        SscsCaseData caseData = SscsCaseData.builder().build();

        List<String> result = caseData.getIssueCodesForAllElementsDisputed();

        assertThat(result).isEmpty();
    }

    @Test
    void givenElementsDisputed_thenGetIssueCodesForAllElementsDisputedReturnsListOfElements() {
        ElementDisputed elementDisputed = ElementDisputed.builder()
                                                         .value(ElementDisputedDetails.builder()
                                                                                      .issueCode("WC")
                                                                                      .outcome("Test")
                                                                                      .build())
                                                         .build();
        SscsCaseData caseData = SscsCaseData.builder()
                                            .elementsDisputedGeneral(List.of(elementDisputed))
                                            .elementsDisputedSanctions(List.of(elementDisputed))
                                            .elementsDisputedOverpayment(List.of(elementDisputed))
                                            .elementsDisputedHousing(List.of(elementDisputed))
                                            .elementsDisputedChildCare(List.of(elementDisputed))
                                            .elementsDisputedCare(List.of(elementDisputed))
                                            .elementsDisputedChildElement(List.of(elementDisputed))
                                            .elementsDisputedChildDisabled(List.of(elementDisputed))
                                            .elementsDisputedLimitedWork(List.of(elementDisputed))
                                            .build();
        List<String> result = caseData.getIssueCodesForAllElementsDisputed();

        assertThat(result)
            .hasSize(9)
            .containsOnly("WC");
    }

    @ParameterizedTest
    @CsvSource({
        ", NO",
        "NO, NO",
        "YES, YES"
    })
    void showConfidentialityTabReturnsCorrectValue(YesNo showConfidentialityTab, YesNo expected) {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                                                .extendedSscsCaseData(ExtendedSscsCaseData.builder()
                                                                                          .showConfidentialityTab(
                                                                                              showConfidentialityTab)
                                                                                          .build())
                                                .build();

        assertThat(sscsCaseData.showConfidentialityTab()).isEqualTo(expected);
    }

    @Test
    void showConfidentialityTabReturnsNoWhenExtendedSscsCaseDataIsNull() {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                                                .extendedSscsCaseData(null)
                                                .build();

        assertThat(sscsCaseData.showConfidentialityTab()).isEqualTo(NO);
    }

    @Test
    void clearNotificationFieldsShouldResetFlagsAndClearSelectionLists() {
        List<CcdValue<DocumentSelectionDetails>> documentSelection = new ArrayList<>();
        documentSelection.add(CcdValue.<DocumentSelectionDetails>builder()
                                      .value(DocumentSelectionDetails.builder()
                                                                     .documentsList(new DynamicList(
                                                                         new DynamicListItem("doc1", "Document 1"),
                                                                         new ArrayList<>()))
                                                                     .build())
                                      .build());

        List<CcdValue<OtherPartySelectionDetails>> otherPartySelection = new ArrayList<>();
        otherPartySelection.add(CcdValue.<OtherPartySelectionDetails>builder()
                                        .value(OtherPartySelectionDetails.builder()
                                                                         .otherPartiesList(new DynamicList(
                                                                             new DynamicListItem("party1", "Party 1"),
                                                                             new ArrayList<>()))
                                                                         .build())
                                        .build());

        SscsCaseData caseData = SscsCaseData.builder()
                                            .genericLetterText("Some letter text")
                                            .sendToAllParties(YES)
                                            .sendToApellant(YES)
                                            .sendToJointParty(YES)
                                            .sendToOtherParties(YES)
                                            .sendToRepresentative(YES)
                                            .addDocuments(YES)
                                            .documentSelection(documentSelection)
                                            .otherPartySelection(otherPartySelection)
                                            .build();

        caseData.clearNotificationFields();

        assertThat(caseData.getGenericLetterText()).isEmpty();
        assertThat(caseData.getSendToAllParties()).isNull();
        assertThat(caseData.getSendToApellant()).isNull();
        assertThat(caseData.getSendToJointParty()).isNull();
        assertThat(caseData.getSendToOtherParties()).isNull();
        assertThat(caseData.getSendToRepresentative()).isNull();
        assertThat(caseData.getAddDocuments()).isNull();
        assertThat(caseData.getDocumentSelection()).isEmpty();
        assertThat(caseData.getOtherPartySelection()).isEmpty();
    }

    @Test
    void getConfidentialityTabShouldReturnTableWhenBenefitIsChildSupport() {
        final Appellant appellant = Appellant.builder()
                                             .name(Name.builder().firstName("John").lastName("Doe").build())
                                             .confidentialityRequirement(new DynamicList(new DynamicListItem(YesNoUnknown.YES.name(), YesNoUnknown.YES.toString()), List.of(new DynamicListItem(YesNoUnknown.YES.name(), YesNoUnknown.YES.toString()))))
                                             .build();
        final SscsCaseData caseData = SscsCaseData.builder()
                                                  .appeal(Appeal.builder()
                                                                .benefitType(BenefitType
                                                                    .builder()
                                                                    .code(CHILD_SUPPORT.getShortName())
                                                                    .build())
                                                                .appellant(appellant)
                                                                .build())
                                                  .build();

        assertThat(caseData.getConfidentialityTab()).isEqualToIgnoringWhitespace("""
            Party | Name | Confidentiality Status | Confidentiality Status Confirmed
            -|-|-|-
            Appellant | John Doe | Yes |
            """);
    }

}
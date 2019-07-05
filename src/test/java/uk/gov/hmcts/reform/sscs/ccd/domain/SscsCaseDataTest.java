package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SscsCaseDataTest {

    @Test
    public void sortHearingsByDate() {
        List<Hearing> hearings = new ArrayList();
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
    public void sortEventsByDate() {
        List<Event> events = new ArrayList();
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
        List<Document> documents = new ArrayList();
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
    public void shouldParseInformationFromAppellant() throws JsonParseException, IOException {
        String expectedValue = "\"Yes\"";

        SscsCaseData sscsCaseData = SscsCaseData.builder().informationFromAppellant("Yes").build();

        ObjectMapper mapper = new ObjectMapper();
        String informationFromAppellant = mapper.writeValueAsString(sscsCaseData.getInformationFromAppellant());

        assertEquals(expectedValue, informationFromAppellant);
    }
}

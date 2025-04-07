package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.*;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

public class SscsQueryBuilderTest {



    @Test
    public void givenFieldNameAndValue_thenBuildQueryForSingleField() {
        SearchSourceBuilder result = findCaseBySingleField("test1", "test2");

        assertEquals("test1", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).fieldName());
        assertEquals("test2", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).value());
    }

    @Test
    public void givenFieldNameAndValue_thenBuildQueryForSingleFieldWithSpecialCharValue() {
        SearchSourceBuilder result = findCaseBySingleField("data.caseReference", "SC001/19/00365");

        assertEquals("data.caseReference", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).fieldName());
        assertEquals("SC001\\\\/19\\\\/00365", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).value());
    }

    @Test
    public void givenValue_thenBuildQueryForTyaNumber() {
        SearchSourceBuilder result = findCaseByTyaNumberQuery("tya123abc");

        assertEquals("data.subscriptions.appellantSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).value());
        assertEquals("data.subscriptions.appointeeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).value());
        assertEquals("data.subscriptions.representativeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).value());
        assertEquals("data.subscriptions.jointPartySubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).value());
        assertEquals("data.otherParties.value.otherPartySubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(4)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(4)).value());
        assertEquals("data.otherParties.value.otherPartyAppointeeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(5)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(5)).value());
        assertEquals("data.otherParties.value.otherPartyRepresentativeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(6)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(6)).value());
    }

    @Test
    public void givenValue_thenBuildQueryForTyaNumberWithOtherParty() {
        SearchSourceBuilder result = findCaseByTyaNumberQueryWithOtherParty("tya123abc");

        assertEquals("data.subscriptions.appellantSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).value());
        assertEquals("data.subscriptions.appointeeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).value());
        assertEquals("data.subscriptions.representativeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).value());
        assertEquals("data.subscriptions.jointPartySubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).value());
        assertEquals("data.otherParties.value.otherPartySubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(4)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(4)).value());
        assertEquals("data.otherParties.value.otherPartyAppointeeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(5)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(5)).value());
        assertEquals("data.otherParties.value.otherPartyRepresentativeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(6)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(6)).value());
    }

    @Test
    public void givenNinoBenefitTypeAndMrnDate_thenBuildQuery() {
        SearchSourceBuilder result = findCcdCaseByNinoAndBenefitTypeAndMrnDateQuery("BB000000B", "PIP", "2020-10-10");

        assertEquals("data.appeal.appellant.identity.nino", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).fieldName());
        assertEquals("BB000000B", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).value());
        assertEquals("data.appeal.benefitType.code", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).fieldName());
        assertEquals("PIP", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).value());
        assertEquals("data.appeal.mrnDetails.mrnDate", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).fieldName());
        assertEquals("2020-10-10", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).value());
    }

    @Test
    public void givenDate_findCaseByResponseReceivedStateAndNoDwpFurtherInfoAndLastModifiedDateQueryQuery() {
        SearchSourceBuilder result = findCaseByResponseReceivedStateAndNoDwpFurtherInfoAndLastModifiedDateQuery("2020-10-10");

        assertEquals("state", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).fieldName());
        assertEquals(State.RESPONSE_RECEIVED.getId(), ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).value());
        assertEquals("data.dwpFurtherInfo", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).fieldName());
        assertEquals("No", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).value());
        assertEquals("last_state_modified_date", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).fieldName());
        assertEquals("2020-10-10", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).value());
    }
}
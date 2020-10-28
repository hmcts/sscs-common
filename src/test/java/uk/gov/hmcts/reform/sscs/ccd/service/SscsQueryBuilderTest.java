package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.*;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

public class SscsQueryBuilderTest {

    @Test
    public void givenFieldNameAndValue_thenBuildQueryForSingleField() {
        SearchSourceBuilder result = findCaseBySingleField("test1", "test2");

        assertEquals("test1", ((MatchQueryBuilder) result.query()).fieldName());
        assertEquals("test2", ((MatchQueryBuilder) result.query()).value());
    }

    @Test
    public void givenValue_thenBuildQueryForTyaNumber() {
        SearchSourceBuilder result = findCaseByTyaNumberQuery("tya123abc");

        assertEquals("case.subscriptions.appellantSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).value());
        assertEquals("case.subscriptions.appointeeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).value());
        assertEquals("case.subscriptions.representativeSubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).value());
        assertEquals("case.subscriptions.jointPartySubscription.tya", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).fieldName());
        assertEquals("tya123abc", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).value());
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
        assertEquals("case.dwpFurtherInfo", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).fieldName());
        assertEquals("No", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).value());
        assertEquals("last_state_modified_date", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).fieldName());
        assertEquals("2020-10-10", ((MatchQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).value());
    }
}
package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsQueryBuilder.*;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
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

        assertEquals("case.subscriptions.appellantSubscription.tya", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).fieldName());
        assertEquals("tya123abc", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(0)).value());
        assertEquals("case.subscriptions.appointeeSubscription.tya", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).fieldName());
        assertEquals("tya123abc", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(1)).value());
        assertEquals("case.subscriptions.representativeSubscription.tya", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).fieldName());
        assertEquals("tya123abc", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(2)).value());
        assertEquals("case.subscriptions.jointPartySubscription.tya", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).fieldName());
        assertEquals("tya123abc", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).should().get(3)).value());
    }

    @Test
    public void givenNinoBenefitTypeAndMrnDate_thenBuildQuery() {
        SearchSourceBuilder result = findCcdCaseByNinoAndBenefitTypeAndMrnDateQuery("BB000000B", "PIP", "2020-10-10");

        assertEquals("case.appeal.appellant.identity.nino", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).fieldName());
        assertEquals("BB000000B", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).value());
        assertEquals("case.appeal.benefitType.code", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).fieldName());
        assertEquals("PIP", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).value());
        assertEquals("case.appeal.mrnDetails.mrnDate", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).fieldName());
        assertEquals("2020-10-10", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).value());
    }

    @Test
    public void givenDate_findCaseByResponseReceivedStateAndNoDwpFurtherInfoAndLastModifiedDateQueryQuery() {
        SearchSourceBuilder result = findCaseByResponseReceivedStateAndNoDwpFurtherInfoAndLastModifiedDateQuery("2020-10-10");

        assertEquals("state", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).fieldName());
        assertEquals(State.RESPONSE_RECEIVED.getId(), ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(0)).value());
        assertEquals("case.dwpFurtherInfo", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).fieldName());
        assertEquals("No", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(1)).value());
        assertEquals("last_state_modified_date", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).fieldName());
        assertEquals("2020-10-10", ((TermQueryBuilder) ((BoolQueryBuilder) result.query()).must().get(2)).value());
    }
}
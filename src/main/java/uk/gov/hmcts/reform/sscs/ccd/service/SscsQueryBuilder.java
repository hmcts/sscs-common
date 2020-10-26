package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

public class SscsQueryBuilder {

    private SscsQueryBuilder() {
    }

    public static SearchSourceBuilder findCaseBySingleField(String fieldName, String value) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(QueryBuilders.matchQuery(fieldName, value));

        return searchBuilder;
    }

    public static SearchSourceBuilder findCaseByTyaNumberQuery(String value) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(QueryBuilders
                .boolQuery()
                .should(termQuery("case.subscriptions.appellantSubscription.tya", value))
                .should(termQuery("case.subscriptions.appointeeSubscription.tya", value))
                .should(termQuery("case.subscriptions.representativeSubscription.tya", value))
                .should(termQuery("case.subscriptions.jointPartySubscription.tya", value)));

        return searchBuilder;
    }

    public static SearchSourceBuilder findCcdCaseByNinoAndBenefitTypeAndMrnDateQuery(String nino, String benefitType, String mrnDate) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(
                        "case.appeal.appellant.identity.nino", nino))
                .must(QueryBuilders.termQuery(
                        "case.appeal.benefitType.code", benefitType))
                .must(QueryBuilders.termQuery(
                        "case.appeal.mrnDetails.mrnDate", mrnDate)));

        return searchBuilder;
    }

    public static SearchSourceBuilder findCaseByResponseReceivedStateAndNoDwpFurtherInfoAndLastModifiedDateQuery(String date) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(
                        "state", State.RESPONSE_RECEIVED.getId()))
                .must(QueryBuilders.termQuery(
                        "case.dwpFurtherInfo", "No"))
                .must(QueryBuilders.termQuery(
                        "last_state_modified_date", date)));

        return searchBuilder;
    }
}
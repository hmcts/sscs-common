package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

public class SscsQueryBuilder {

    private SscsQueryBuilder() {
    }

    public static SearchSourceBuilder findCaseBySingleField(String fieldName, String value) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(matchQuery(fieldName, value));

        return searchBuilder;
    }

    public static SearchSourceBuilder findCaseByTyaNumberQuery(String value) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(QueryBuilders
                .boolQuery()
                .should(matchQuery("data.subscriptions.appellantSubscription.tya", value))
                .should(matchQuery("data.subscriptions.appointeeSubscription.tya", value))
                .should(matchQuery("data.subscriptions.representativeSubscription.tya", value))
                .should(matchQuery("data.subscriptions.jointPartySubscription.tya", value)));

        return searchBuilder;
    }

    public static SearchSourceBuilder findCcdCaseByNinoAndBenefitTypeAndMrnDateQuery(String nino, String benefitType, String mrnDate) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(QueryBuilders.boolQuery()
                .must(matchQuery(
                        "data.appeal.appellant.identity.nino", nino))
                .must(matchQuery(
                        "data.appeal.benefitType.code", benefitType))
                .must(matchQuery(
                        "data.appeal.mrnDetails.mrnDate", mrnDate)));

        return searchBuilder;
    }

    public static SearchSourceBuilder findCaseByResponseReceivedStateAndNoDwpFurtherInfoAndLastModifiedDateQuery(String date) {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();

        searchBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery(
                        "state", State.RESPONSE_RECEIVED.getId()))
                .must(QueryBuilders.matchQuery(
                        "data.dwpFurtherInfo", "No"))
                .must(QueryBuilders.matchQuery(
                        "last_state_modified_date", date)));

        return searchBuilder;
    }
}
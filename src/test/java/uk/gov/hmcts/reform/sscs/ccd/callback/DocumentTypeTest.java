package uk.gov.hmcts.reform.sscs.ccd.callback;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DocumentTypeTest {

    @ParameterizedTest
    @CsvSource({
        "DIRECTION_NOTICE, Direction Notice",
        "DECISION_NOTICE, Decision Notice",
        "APPELLANT_EVIDENCE, appellantEvidence",
        "JOINT_PARTY_EVIDENCE, jointPartyEvidence",
        "URGENT_HEARING_REQUEST, urgentHearingRequest"
    })
    public void shouldGetDocumentTypeFromLabel(DocumentType documentType, String label) {
        assertThat(DocumentType.fromValue(label), is(documentType));
    }

}

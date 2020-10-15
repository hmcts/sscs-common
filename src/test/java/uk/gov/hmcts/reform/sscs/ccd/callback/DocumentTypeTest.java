package uk.gov.hmcts.reform.sscs.ccd.callback;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DocumentTypeTest {

    @Test
    @Parameters({
            "DIRECTION_NOTICE, Direction Notice",
            "DECISION_NOTICE, Decision Notice",
            "APPELLANT_EVIDENCE, appellantEvidence",
            "JOINT_PARTY_EVIDENCE, jointPartyEvidence"
    })
    public void shouldGetDocumentTypeFromLabel(DocumentType documentType, String label) {
        assertThat(DocumentType.fromValue(label), is(documentType));
    }

}

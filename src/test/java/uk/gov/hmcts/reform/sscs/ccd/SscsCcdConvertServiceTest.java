package uk.gov.hmcts.reform.sscs.ccd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import org.junit.Test;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;

public class SscsCcdConvertServiceTest {

    @Test
    public void canBuildCaseDetails() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        HashMap<String, Object> appealMap = new HashMap<>();
        String signer = "signerName";
        appealMap.put("signer", signer);
        data.put("appeal", appealMap);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
                .id(id)
                .data(data)
                .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertThat(caseDetails.getId(), is(id));
        assertThat(caseDetails.getData().getCaseReference(), is(caseReference));
        assertThat(caseDetails.getData().getAppeal().getSigner(), is(signer));
    }
}

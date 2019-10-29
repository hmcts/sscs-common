package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService.hasAppellantIdentify;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;

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

    @Test
    public void hasAppellantIdentify_shouldReturnFalseIfCaseDataNull() {
        assertFalse(hasAppellantIdentify(null));
    }

    @Test
    public void hasAppellantIdentify_shouldReturnFalseIfCaseDataEmpty() {
        assertFalse(hasAppellantIdentify(SscsCaseData.builder().build()));
    }

    @Test
    public void hasAppellantIdentify_shouldReturnFalseIfAppealNull() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
            .id(id)
            .data(data)
            .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertFalse(hasAppellantIdentify(caseDetails.getData()));
    }

    @Test
    public void hasAppellantIdentify_shouldReturnFalseIfAppealEmpty() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        HashMap<String, Object> appealMap = new HashMap<>();
        data.put("appeal", appealMap);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
            .id(id)
            .data(data)
            .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertFalse(hasAppellantIdentify(caseDetails.getData()));
    }

    @Test
    public void hasAppellantIdentify_shouldReturnFalseIfAppellantNull() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        HashMap<String, Object> appealMap = new HashMap<>();
        String signer = "signerName";
        appealMap.put("signer", signer);
        appealMap.put("appellant", null);
        data.put("appeal", appealMap);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
            .id(id)
            .data(data)
            .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertFalse(hasAppellantIdentify(caseDetails.getData()));
    }

    @Test
    public void hasAppellantIdentify_shouldReturnFalseIfAppellantEmpty() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        HashMap<String, Object> appealMap = new HashMap<>();
        String signer = "signerName";
        appealMap.put("signer", signer);
        Appellant appellant = Appellant.builder().build();
        appealMap.put("appellant", appellant);
        data.put("appeal", appealMap);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
            .id(id)
            .data(data)
            .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertFalse(hasAppellantIdentify(caseDetails.getData()));
    }

    @Test
    public void hasAppellantIdentify_shouldReturnFalseIfIdentityNull() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        HashMap<String, Object> appealMap = new HashMap<>();
        String signer = "signerName";
        appealMap.put("signer", signer);
        Appellant appellant = Appellant.builder().build();
        appealMap.put("appellant", appellant);
        data.put("appeal", appealMap);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
            .id(id)
            .data(data)
            .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertFalse(hasAppellantIdentify(caseDetails.getData()));
    }

    @Test
    public void hasAppellantIdentify_shouldReturnTrueIfIdentityPresent() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        HashMap<String, Object> appealMap = new HashMap<>();
        String signer = "signerName";
        appealMap.put("signer", signer);
        Appellant appellant = Appellant.builder().identity(Identity.builder().nino("AB 12 34 56 C").build()).build();
        appealMap.put("appellant", appellant);
        data.put("appeal", appealMap);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
            .id(id)
            .data(data)
            .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertTrue(hasAppellantIdentify(caseDetails.getData()));
    }

    @Test
    public void testBuilderMissingCaseId() {
        Long caseId = 1001L;

        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails = CaseDetails.builder().id(caseId)
                .data(new HashMap<>())
                .build();

        assertEquals(caseId.toString(), new SscsCcdConvertService().getCaseDetails(caseDetails).getData().getCcdCaseId());
    }

    @Test
    public void testBuilderNullCaseId() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("caseReference", "SC924/39/23210");

        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails = CaseDetails.builder()
                .data(new HashMap<>())
                .build();

        assertNull(new SscsCcdConvertService().getCaseDetails(caseDetails).getData().getCcdCaseId());
    }

    @Test
    public void testBuilderWithNullData() {
        Long caseId = 1001L;

        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails = CaseDetails.builder().id(caseId)
                .data(null)
                .build();

        assertEquals(caseId.toString(), new SscsCcdConvertService().getCaseDetails(caseDetails).getData().getCcdCaseId());
    }

    @Test
    public void canTranslateADateField() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        HashMap<String, Object> appealMap = new HashMap<>();
        String signer = "signerName";
        appealMap.put("signer", signer);
        Appellant appellant = Appellant.builder().identity(Identity.builder().nino("AB 12 34 56 C").build()).build();
        appealMap.put("appellant", appellant);
        data.put("appeal", appealMap);
        HashMap<String, Object> doc = new HashMap<>();
        doc.put("documentType", "blah");
        doc.put("documentFileName", "blah.pdf");
        doc.put("documentDateAdded", "2019-07-04");

        data.put("sscsInterlocDirectionDocument", doc);
        long id = 123L;
        CaseDetails build = CaseDetails.builder()
                .id(id)
                .data(data)
                .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertTrue(hasAppellantIdentify(caseDetails.getData()));

        assertEquals(LocalDate.of(2019, 07, 04), caseDetails.getData().getSscsInterlocDirectionDocument().getDocumentDateAdded());
    }

    @Test
    public void canTranslateAnEnum() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        data.put("directionType", DirectionType.APPEAL_TO_PROCEED);

        long id = 123L;
        CaseDetails build = CaseDetails.builder()
                .id(id)
                .data(data)
                .build();
        SscsCaseDetails caseDetails = new SscsCcdConvertService().getCaseDetails(build);

        assertEquals(DirectionType.APPEAL_TO_PROCEED, caseDetails.getData().getDirectionType());
    }
}

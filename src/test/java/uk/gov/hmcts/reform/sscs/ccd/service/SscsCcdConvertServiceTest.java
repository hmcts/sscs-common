package uk.gov.hmcts.reform.sscs.ccd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService.hasAppellantIdentify;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.DirectionType;
import uk.gov.hmcts.reform.sscs.ccd.domain.Identity;
import uk.gov.hmcts.reform.sscs.ccd.domain.RequestOutcome;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.YesNo;

public class SscsCcdConvertServiceTest {
    
    private final SscsCcdConvertService underTest = new SscsCcdConvertService();

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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

        assertEquals(id, caseDetails.getId());
        assertEquals(caseReference, caseDetails.getData().getCaseReference());
        assertEquals(signer, caseDetails.getData().getAppeal().getSigner());
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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

        assertTrue(hasAppellantIdentify(caseDetails.getData()));
    }

    @Test
    public void testBuilderMissingCaseId() {
        Long caseId = 1001L;

        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails = CaseDetails.builder().id(caseId)
                .data(new HashMap<>())
                .build();

        assertEquals(caseId.toString(), underTest.getCaseDetails(caseDetails).getData().getCcdCaseId());
    }

    @Test
    public void testBuilderNullCaseId() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("caseReference", "SC924/39/23210");

        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails = CaseDetails.builder()
                .data(new HashMap<>())
                .build();

        assertNull(underTest.getCaseDetails(caseDetails).getData().getCcdCaseId());
    }

    @Test
    public void testBuilderWithNullData() {
        Long caseId = 1001L;

        uk.gov.hmcts.reform.ccd.client.model.CaseDetails caseDetails = CaseDetails.builder().id(caseId)
                .data(null)
                .build();

        assertEquals(caseId.toString(), underTest.getCaseDetails(caseDetails).getData().getCcdCaseId());
    }

    @Test
    public void canTranslateADateField() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        data.put("reinstatementRegistered",  "2019-07-04");
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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

        assertTrue(hasAppellantIdentify(caseDetails.getData()));

        LocalDate expectedDate = LocalDate.of(2019, 7, 4);
        assertEquals(expectedDate, caseDetails.getData().getSscsInterlocDirectionDocument().getDocumentDateAdded());
        assertEquals(expectedDate, caseDetails.getData().getReinstatementRegistered());
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
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

        assertEquals(DirectionType.APPEAL_TO_PROCEED, caseDetails.getData().getDirectionType());
    }

    @Test
    public void canTranslateARequestOutcomeEnum() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        data.put("reinstatementOutcome", RequestOutcome.IN_PROGRESS);

        long id = 123L;
        CaseDetails build = CaseDetails.builder()
                .id(id)
                .data(data)
                .build();
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

        assertEquals(RequestOutcome.IN_PROGRESS, caseDetails.getData().getReinstatementOutcome());
    }

    @Test
    public void canTranslateAYesNoEnum() {
        String caseReference = "caseRef";
        HashMap<String, Object> data = new HashMap<>();
        data.put("caseReference", caseReference);
        data.put("showRegulation29Page", YesNo.YES);
        data.put("doesRegulation29Apply", YesNo.NO);

        long id = 123L;
        CaseDetails build = CaseDetails.builder()
                .id(id)
                .data(data)
                .build();
        SscsCaseDetails caseDetails = underTest.getCaseDetails(build);

        assertEquals(YesNo.YES, caseDetails.getData().getSscsEsaCaseData().getShowRegulation29Page());
        assertEquals(YesNo.NO, caseDetails.getData().getSscsEsaCaseData().getDoesRegulation29Apply());
        assertTrue(YesNo.YES.toBoolean());
        assertFalse(YesNo.NO.toBoolean());
    }

    @Test
    public void givenGetCaseDataContent_thenSetSupplementaryData() {
        CaseDataContent caseDataContent = underTest.getCaseDataContent(SscsCaseData.builder().build(), StartEventResponse.builder().build(), "Summary", "Description");

        assertEquals("BBA3", caseDataContent.getSupplementaryDataRequest().get("$set").get("HMCTSServiceId"));
    }

    @Test
    public void testGetCaseDetails() {
        SscsCaseData caseData = SscsCaseData.builder().caseReference("123").build();
        SscsCaseDetails sscsCaseDetails = SscsCaseDetails.builder()
                .id(123L)
                .jurisdiction("SSCS")
                .caseTypeId("Benefit")
                .data(caseData)
                .build();

        CaseDetails caseDetails = underTest.getCaseDetails(sscsCaseDetails);

        assertNotNull(caseDetails);
        assertEquals(Long.valueOf(123L), caseDetails.getId());
        assertEquals("SSCS", caseDetails.getJurisdiction());
        assertEquals("Benefit", caseDetails.getCaseTypeId());
        assertEquals("123", caseDetails.getData().get("caseReference"));
    }

    @Test
    public void testGetCaseDataMap() {
        SscsCaseData caseData = SscsCaseData.builder().caseReference("1234").build();

        Map<String, Object> caseDataMap = underTest.getCaseDataMap(caseData);

        assertNotNull(caseDataMap);
        assertEquals("1234", caseDataMap.get("caseReference"));
    }
}

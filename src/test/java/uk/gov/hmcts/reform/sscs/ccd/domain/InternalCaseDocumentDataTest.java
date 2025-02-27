package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class InternalCaseDocumentDataTest {

    private InternalCaseDocumentData internalCaseDocumentData;

    @Mock
    private DynamicMixedChoiceList mockList;
    @Mock
    private DynamicMixedChoiceList mockListTwo;

    @BeforeEach
    void setUp() {
        internalCaseDocumentData = new InternalCaseDocumentData();
    }
    @Test
    void setDynamicListIfInternal() {
        internalCaseDocumentData.setDynamicList(true, mockList);
        assertEquals(mockList, internalCaseDocumentData.getMoveDocumentToInternalDocumentsTabDL());
        assertNull(internalCaseDocumentData.getMoveDocumentToDocumentsTabDL());
    }

    @Test
    void setDynamicListIfNotInternal() {
        internalCaseDocumentData.setDynamicList(false, mockList);
        assertEquals(mockList, internalCaseDocumentData.getMoveDocumentToDocumentsTabDL());
        assertNull(internalCaseDocumentData.getMoveDocumentToInternalDocumentsTabDL());
    }

    @Test
    void getDynamicList() {
        internalCaseDocumentData.setMoveDocumentToDocumentsTabDL(mockList);
        internalCaseDocumentData.setMoveDocumentToInternalDocumentsTabDL(mockListTwo);
        assertEquals(mockList, internalCaseDocumentData.getDynamicList(false));
        assertEquals(mockListTwo, internalCaseDocumentData.getDynamicList(true));
    }
}
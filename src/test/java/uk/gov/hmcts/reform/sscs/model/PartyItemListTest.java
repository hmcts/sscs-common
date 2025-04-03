package uk.gov.hmcts.reform.sscs.model;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType.*;
import static uk.gov.hmcts.reform.sscs.model.PartyItemList.findPartyItemByCode;
import static uk.gov.hmcts.reform.sscs.model.PartyItemList.isOtherPartyItemType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PartyItemListTest {

    @Test
    public void givenAppellantPartyItem_thenEnsureCorrectDetailsAreReturned() {
        assertEquals("appellant", PartyItemList.APPELLANT.getCode());
        assertEquals(APPELLANT_EVIDENCE, PartyItemList.APPELLANT.getDocumentType());
    }

    @Test
    public void givenRepPartyItem_thenEnsureCorrectDetailsAreReturned() {
        assertEquals("representative", PartyItemList.REPRESENTATIVE.getCode());
        assertEquals(REPRESENTATIVE_EVIDENCE, PartyItemList.REPRESENTATIVE.getDocumentType());
    }

    @Test
    public void givenJointPartyItem_thenEnsureCorrectDetailsAreReturned() {
        assertEquals("jointParty", PartyItemList.JOINT_PARTY.getCode());
        assertEquals(JOINT_PARTY_EVIDENCE, PartyItemList.JOINT_PARTY.getDocumentType());
    }

    @Test
    public void givenDwpPartyItem_thenEnsureCorrectDetailsAreReturned() {
        assertEquals("dwp", PartyItemList.DWP.getCode());
        assertEquals(DWP_EVIDENCE, PartyItemList.DWP.getDocumentType());
    }

    @Test
    public void givenHmctsPartyItem_thenEnsureCorrectDetailsAreReturned() {
        assertEquals("hmcts", PartyItemList.HMCTS.getCode());
        assertEquals(HMCTS_EVIDENCE, PartyItemList.HMCTS.getDocumentType());
    }

    @ParameterizedTest
    @CsvSource({"APPELLANT, appellant", "REPRESENTATIVE, representative", "JOINT_PARTY, jointParty", "OTHER_PARTY, otherParty", "OTHER_PARTY_REPRESENTATIVE, otherPartyRep"})
    public void givenPartyItemCode_thenReturnPartyItemListValue(PartyItemList expectedPartyItem, String partyItemCode) {
        assertEquals(expectedPartyItem, findPartyItemByCode(partyItemCode));
    }

    @ParameterizedTest
    @CsvSource({"appellant, false", "representative, false", "jointParty, false", "otherParty, true", "otherPartyRep, true", "otherParty1, true", "otherPartyRep2, true"})
    public void givenPartyItemCode_thenCheckIfOtherPartyType(String expectedPartyItem, boolean isOtherParty) {
        assertEquals(isOtherParty, isOtherPartyItemType(expectedPartyItem));
    }

}
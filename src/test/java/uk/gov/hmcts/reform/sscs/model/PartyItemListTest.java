package uk.gov.hmcts.reform.sscs.model;

import static org.junit.Assert.*;
import static uk.gov.hmcts.reform.sscs.ccd.callback.DocumentType.*;

import org.junit.Test;

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

}
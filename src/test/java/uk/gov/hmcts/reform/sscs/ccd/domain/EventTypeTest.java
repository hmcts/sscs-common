package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

public class EventTypeTest {

    // copied from the CaseEvent TAB in the CCD Definition file
    private static final String ALL_EVENT_TYPES =
            "secondHearingHoldingReminder\n" +
            "thirdHearingHoldingReminder\n" +
            "abateCase\n" +
            "dwpActionDirection\n" +
            "actionFurtherEvidence\n" +
            "dwpActionRepAdded\n" +
            "actionStrikeOut\n" +
            "addSCnumber\n" +
            "addNote\n" +
            "addRepresentative\n" +
            "addHearing\n" +
            "adminRestoreCases\n" +
            "adminSendToAppealCreated\n" +
            "adminSendToDormantAppealState\n" +
            "adminSendToDraft\n" +
            "adminSendToDraftArchived\n" +
            "adminSendToHearing\n" +
            "adminSendToIncompleteApplication\n" +
            "adminSendToIncompleteApplicationInformationReqsted\n" +
            "adminSendToInterlocutoryReviewState\n" +
            "adminSendToReadyToList\n" +
            "adminSendTorRsponseReceived\n" +
            "adminSendToValidAppeal\n" +
            "adminSendToVoidState\n" +
            "adminSendToWithDwp\n" +
            "adminUpdateEvent\n" +
            "adminAppealWithdrawn\n" +
            "judgeDecisionAdmitAppeal\n" +
            "tcwDecisionAdmitAppeal\n" +
            "editBundle\n" +
            "amendDueDate\n" +
            "amendElementsIssues\n" +
            "interlocReviewStateAmend\n" +
            "appealCreated\n" +
            "appealDormant\n" +
            "appealLapsed\n" +
            "appealReceived\n" +
            "dwpAppealRegistered\n" +
            "appealToProceed\n" +
            "judgeDecisionAppealToProceed\n" +
            "tcwDecisionAppealToProceed\n" +
            "appealWithdrawn\n" +
            "draftArchived\n" +
            "assignToJudge\n" +
            "associateCase\n" +
            "attachRoboticsJson\n" +
            "attachScannedDocs\n" +
            "dwpCaseInProgress\n" +
            "CCD_ADMIN\n" +
            "dwpChallengeValidity\n" +
            "cloneBundle\n" +
            "confirmLapsed\n" +
            "cohAnswersSubmitted\n" +
            "cohQuestionDeadlineExtensionDenied\n" +
            "cohQuestionDeadlineExtensionGranted\n" +
            "cohDecisionRejected\n" +
            "cohContinuousOnlineHearingRelisted\n" +
            "cohDecisionIssued\n" +
            "cohQuestionDeadlineElapsed\n" +
            "cohQuestionDeadlineReminder\n" +
            "cohQuestionDeadlineExtended\n" +
            "cohQuestionRoundIssued\n" +
            "createBundle\n" +
            "createEditedBundle\n" +
            "createDraft\n" +
            "panelUpdate\n" +
            "deathOfAppellant\n" +
            "deathOfAppellantActioned\n" +
            "logDocsToDwp\n" +
            "draftToIncompleteApplication\n" +
            "draftToNonCompliant\n" +
            "draftToValidAppealCreated\n" +
            "dwpActionWithdrawal\n" +
            "dwpDirectionResponse\n" +
            "dwpRaiseException\n" +
            "dwpResponseLateReminder\n" +
            "evidenceReceived\n" +
            "evidenceReminder\n" +
            "furtherEvidenceHandledOffline\n" +
            "finalHearingHoldingReminder\n" +
            "feNoAction\n" +
            "dwpEvidenceInProgress\n" +
            "createAppealPDF\n" +
            "generateCoversheet\n" +
            "handleEvidence\n" +
            "hearingAdjourned\n" +
            "hearingBooked\n" +
            "hearingHoldingReminder\n" +
            "hearingPostponed\n" +
            "hearingReminder\n" +
            "incompleteApplicationReceived\n" +
            "interlocInformationReceivedActionFurtherEvidence\n" +
            "interlocInformationReceived\n" +
            "issueAdjournmentNotice\n" +
            "decisionIssued\n" +
            "judgeDirectionIssued\n" +
            "tcwDirectionIssued\n" +
            "directionIssued\n" +
            "issueFinalDecision\n" +
            "issueFurtherEvidence\n" +
            "requestForInformation\n" +
            "jointPartyAdded\n" +
            "dwpLapseCase\n" +
            "hmctsLapseCase\n" +
            "linkACase\n" +
            "sentToJudge\n" +
            "manageDwpDocuments\n" +
            "makeCaseUrgent\n" +
            "moveToAppealCreated\n" +
            "dwpNoAction\n" +
            "nonCompliant\n" +
            "dwpNotAbleToRegister\n" +
            "notListable\n" +
            "notificationSent\n" +
            "processAudioVideo\n" +
            "processReasonableAdjustment\n" +
            "provideAppointeeDetails\n" +
            "corDecision\n" +
            "reinstateAppeal\n" +
            "readyToList\n" +
            "reinstateVoidAppeal\n" +
            "reissueDocument\n" +
            "reissueFurtherEvidence\n" +
            "dwpRequestTimeExtension\n" +
            "removeLinkForCase\n" +
            "resendAppealCreated\n" +
            "resendCaseToGAPS2\n" +
            "resendToDwp\n" +
            "responseReceived\n" +
            "hmctsResponseReviewed\n" +
            "reviewConfidentialityRequest\n" +
            "reviewPhmeRequest\n" +
            "interlocSendToTcw\n" +
            "sendFurtherEvidenceError\n" +
            "sendToAdmin\n" +
            "sendToDwp\n" +
            "sendToDwpError\n" +
            "sendToDwpOffline\n" +
            "validSendToInterloc\n" +
            "nonCompliantSendToInterloc\n" +
            "tcwReferToJudge\n" +
            "sendToRoboticsError\n" +
            "sendValidCaseToInterloc\n" +
            "sentToDwp\n" +
            "asyncStitchingComplete\n" +
            "stopBulkPrintForReasonableAdjustment\n" +
            "judgeDecisionStrikeout\n" +
            "tcwDecisionStrikeOut\n" +
            "struckOut\n" +
            "dwpSupplementaryResponse\n" +
            "SYSTEM_MAINTENANCE\n" +
            "unassignToJudge\n" +
            "updateCaseOnly\n" +
            "updateDraft\n" +
            "eventsUpdates\n" +
            "updateHearingType\n" +
            "updateIncompleteApplication\n" +
            "updateListingRequirements\n" +
            "updateNotListable\n" +
            "updateReasonableAdjustment\n" +
            "subscriptionUpdated\n" +
            "caseUpdated\n" +
            "updateUCB\n" +
            "uploadCorDocument\n" +
            "uploadDocument\n" +
            "uploadDocumentFurtherEvidence\n" +
            "uploadDraftCorDocument\n" +
            "uploadDraftDocument\n" +
            "uploadFurtherEvidence\n" +
            "dwpUploadResponse\n" +
            "validAppealCreated\n" +
            "interlocValidAppeal\n" +
            "validAppeal\n" +
            "voidCase\n" +
            "voidIncompleteApplication\n" +
            "interlocVoidAppeal\n" +
            "adjournCase\n" +
            "writeFinalDecision\n" +
            "createWelshNotice\n" +
            "cancelTranslations\n" +
            "issueAdjournmentNoticeWelsh\n" +
            "decisionIssuedWelsh\n" +
            "directionIssuedWelsh\n" +
            "issueFinalDecisionWelsh\n" +
            "manageWelshDocuments\n" +
            "markDocumentsForTranslation\n" +
            "processAudioVideoWelsh\n" +
            "requestTranslationFromWLU\n" +
            "updateWelshPreference\n" +
            "uploadWelshDocument\n" +
            "createWithDwpTestCase\n" +
            "createResponseReceivedTestCase\n" +
            "createTestCase\n" +
            "uploadHearingRecording\n" +
            "actionHearingRecordingRequest\n" +
            "citizenRequestHearingRecording\n" +
            "postponementRequest\n" +
            "dwpRequestHearingRecording\n" +
            "sendToFirstTier\n" +
            "addHearingOucome\n" +
            "upperTribunalDecision";


    @Test
    @Ignore
    public void hasAllEventTypesDefinedInCcdDefinitionFile() {
        String[] allEventTypes = ALL_EVENT_TYPES.split("\n");

        for (String eventType : allEventTypes) {
            try {
                EventType.findValue(eventType);
            } catch (NoSuchElementException e) {
                fail(String.format("missing %s", eventType));
            }
        }
    }

    @Test
    public void eventNameIsCorrect() {
        List<EventType> exceptions = Arrays.asList(DWP_RESPOND, ADJOURNED,
            LAPSED_REVISED, WITHDRAWN, POSTPONED, DORMANT, CLOSED, DWP_RESPOND_OVERDUE,
            SYA_APPEAL_CREATED, FIRST_HEARING_HOLDING_REMINDER, FINAL_DECISION, COH_ONLINE_HEARING_RELISTED,
            SENT_TO_DWP_ERROR, REQUEST_FOR_INFORMATION, CREATE_APPEAL_PDF, RESEND_CASE_TO_GAPS2, ADD_SC_NUMBER,
            LINK_A_CASE, REMOVE_LINK_FOR_CASE, ACTION_STRIKE_OUT, UPLOAD_DOCUMENT_FURTHER_EVIDENCE,
            WRITE_FINAL_DECISION, UPLOAD_WELSH_DOCUMENT, REQUEST_TRANSLATION_FROM_WLU, UPDATE_WELSH_PREFERENCE,
            CANCEL_TRANSLATIONS, CREATE_WELSH_NOTICE, MARK_DOCS_FOR_TRANSATION, JOINT_PARTY_ADDED, UPDATE_UCB,
            STOP_BULK_PRINT_FOR_REASONABLE_ADJUSTMENT, CCD_ADMIN, SYSTEM_MAINTENANCE, UPLOAD_HEARING_RECORDING,
            DWP_REQUEST_HEARING_RECORDING, CITIZEN_REQUEST_HEARING_RECORDING, SET_ASIDE_REFUSED_SOR, SOR_EXTEND_TIME,
            SOR_REFUSED, SOR_WRITE, SOR_REQUEST);
        for (EventType eventType : EventType.values()) {
            try {
                if (!exceptions.contains(eventType)) {
                    assertEquals(StringUtils.upperCase(eventType.getCcdType().replaceAll("(.)([A-Z])", "$1_$2")), eventType.name());
                }
            } catch (NoSuchElementException e) {
                fail(String.format("missing %s", eventType));
            }
        }
    }
}

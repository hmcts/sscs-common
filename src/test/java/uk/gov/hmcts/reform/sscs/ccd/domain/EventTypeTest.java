package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class EventTypeTest {

    @Test
    @Parameters({
            "secondHearingHoldingReminder",
            "thirdHearingHoldingReminder",
            "abateCase",
            "dwpActionDirection",
            "actionFurtherEvidence",
            "dwpActionRepAdded",
            "actionStrikeOut",
            "addSCnumber",
            "addNote",
            "addRepresentative",
            "addHearing",
            "adminRestoreCases",
            "adminSendToAppealCreated",
            "adminSendToDormantAppealState",
            "adminSendToDraft",
            "adminSendToDraftArchived",
            "adminSendToHearing",
            "adminSendToIncompleteApplication",
            "adminSendToIncompleteApplicationInformationReqsted",
            "adminSendToInterlocutoryReviewState",
            "adminSendToReadyToList",
            "adminSendTorRsponseReceived",
            "adminSendToValidAppeal",
            "adminSendToVoidState",
            "adminSendToWithDwp",
            "adminUpdateEvent",
            "adminAppealWithdrawn",
            "judgeDecisionAdmitAppeal",
            "tcwDecisionAdmitAppeal",
            "editBundle",
            "amendDueDate",
            "amendElementsIssues",
            "interlocReviewStateAmend",
            "appealCreated",
            "appealDormant",
            "appealLapsed",
            "appealReceived",
            "dwpAppealRegistered",
            "appealToProceed",
            "judgeDecisionAppealToProceed",
            "tcwDecisionAppealToProceed",
            "appealWithdrawn",
            "draftArchived",
            "assignToJudge",
            "associateCase",
            "attachRoboticsJson",
            "attachScannedDocs",
            "dwpCaseInProgress",
            "CCD_ADMIN",
            "dwpChallengeValidity",
            "cloneBundle",
            "confirmLapsed",
            "cohAnswersSubmitted",
            "cohQuestionDeadlineExtensionDenied",
            "cohQuestionDeadlineExtensionGranted",
            "cohDecisionRejected",
            "cohContinuousOnlineHearingRelisted",
            "cohDecisionIssued",
            "cohQuestionDeadlineElapsed",
            "cohQuestionDeadlineReminder",
            "cohQuestionDeadlineExtended",
            "cohQuestionRoundIssued",
            "createBundle",
            "createEditedBundle",
            "createDraft",
            "panelUpdate",
            "deathOfAppellant",
            "deathOfAppellantActioned",
            "logDocsToDwp",
            "draftToIncompleteApplication",
            "draftToNonCompliant",
            "draftToValidAppealCreated",
            "dwpActionWithdrawal",
            "dwpDirectionResponse",
            "dwpRaiseException",
            "dwpResponseLateReminder",
            "evidenceReceived",
            "evidenceReminder",
            "furtherEvidenceHandledOffline",
            "finalHearingHoldingReminder",
            "feNoAction",
            "dwpEvidenceInProgress",
            "createAppealPDF",
            "generateCoversheet",
            "handleEvidence",
            "hearingAdjourned",
            "hearingBooked",
            "hearingHoldingReminder",
            "hearingPostponed",
            "hearingReminder",
            "incompleteApplicationReceived",
            "interlocInformationReceivedActionFurtherEvidence",
            "interlocInformationReceived",
            "issueAdjournmentNotice",
            "decisionIssued",
            "judgeDirectionIssued",
            "tcwDirectionIssued",
            "directionIssued",
            "issueFinalDecision",
            "issueFurtherEvidence",
            "requestForInformation",
            "jointPartyAdded",
            "dwpLapseCase",
            "hmctsLapseCase",
            "linkACase",
            "sentToJudge",
            "manageDwpDocuments",
            "makeCaseUrgent",
            "moveToAppealCreated",
            "dwpNoAction",
            "nonCompliant",
            "dwpNotAbleToRegister",
            "notListable",
            "notificationSent",
            "processAudioVideo",
            "processReasonableAdjustment",
            "provideAppointeeDetails",
            "corDecision",
            "reinstateAppeal",
            "readyToList",
            "reinstateVoidAppeal",
            "reissueDocument",
            "reissueFurtherEvidence",
            "dwpRequestTimeExtension",
            "removeLinkForCase",
            "resendAppealCreated",
            "resendCaseToGAPS2",
            "resendToDwp",
            "responseReceived",
            "hmctsResponseReviewed",
            "reviewConfidentialityRequest",
            "reviewPhmeRequest",
            "interlocSendToTcw",
            "sendFurtherEvidenceError",
            "sendToAdmin",
            "sendToDwp",
            "sendToDwpError",
            "sendToDwpOffline",
            "validSendToInterloc",
            "nonCompliantSendToInterloc",
            "tcwReferToJudge",
            "sendToRoboticsError",
            "sendValidCaseToInterloc",
            "sentToDwp",
            "asyncStitchingComplete",
            "stopBulkPrintForReasonableAdjustment",
            "judgeDecisionStrikeout",
            "tcwDecisionStrikeOut",
            "struckOut",
            "dwpSupplementaryResponse",
            "SYSTEM_MAINTENANCE",
            "unassignToJudge",
            "updateCaseOnly",
            "updateDraft",
            "eventsUpdates",
            "updateHearingType",
            "updateIncompleteApplication",
            "updateListingRequirements",
            "updateNotListable",
            "updateReasonableAdjustment",
            "subscriptionUpdated",
            "caseUpdated",
            "updateUCB",
            "uploadCorDocument",
            "uploadDocument",
            "uploadDocumentFurtherEvidence",
            "uploadDraftCorDocument",
            "uploadDraftDocument",
            "uploadFurtherEvidence",
            "dwpUploadResponse",
            "validAppealCreated",
            "interlocValidAppeal",
            "validAppeal",
            "voidCase",
            "voidIncompleteApplication",
            "interlocVoidAppeal",
            "adjournCase",
            "writeFinalDecision",
            "createWelshNotice",
            "cancelTranslations",
            "issueAdjournmentNoticeWelsh",
            "decisionIssuedWelsh",
            "directionIssuedWelsh",
            "issueFinalDecisionWelsh",
            "manageWelshDocuments",
            "markDocumentsForTranslation",
            "processAudioVideoWelsh",
            "requestTranslationFromWLU",
            "updateWelshPreference",
            "uploadWelshDocument",
            "createWithDwpTestCase",
            "createResponseReceivedTestCase",
            "createTestCase",
            "uploadHearingRecording",
            "actionHearingRecordingRequest",
            "citizenRequestHearingRecording",
            "postponementRequest",
            "dwpRequestHearingRecording",
            "sendToFirstTier",
            "upperTribunalDecision",
            "setHearingRoute"
    })
    public void hasAllEventTypesDefinedInCcdDefinitionFile(String eventType) {
        try {
            EventType.findValue(eventType);
        } catch (NoSuchElementException e) {
            fail(String.format("missing %s", eventType));
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

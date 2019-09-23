package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import org.junit.Test;

public class EventTypeTest {

    // copied from the CaseEvent TAB in the CCD Definition file
    private static final String ALL_EVENT_TYPES =
            "secondHearingHoldingReminder\n" +
            "thirdHearingHoldingReminder\n" +
            "actionFurtherEvidence\n" +
            "addSCnumber\n" +
            "addNote\n" +
            "addRepresentative\n" +
            "judgeDecisionAdmitAppeal\n" +
            "tcwDecisionAdmitAppeal\n" +
            "editBundle\n" +
            "appealCreated\n" +
            "appealDormant\n" +
            "appealLapsed\n" +
            "appealReceived\n" +
            "dwpAppealRegistered\n" +
            "judgeDecisionAppealToProceed\n" +
            "tcwDecisionAppealToProceed\n" +
            "appealWithdrawn\n" +
            "draftArchived\n" +
            "assignToJudge\n" +
            "associateCase\n" +
            "attachRoboticsJson\n" +
            "attachScannedDocs\n" +
            "dwpCaseInProgress\n" +
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
            "createDraft\n" +
            "panelUpdate\n" +
            "logDocsToDwp\n" +
            "dwpRaiseException\n" +
            "dwpResponseLateReminder\n" +
            "evidenceReceived\n" +
            "evidenceReminder\n" +
            "finalHearingHoldingReminder\n" +
            "dwpEvidenceInProgress\n" +
            "createAppealPDF\n" +
            "handleEvidence\n" +
            "hearingAdjourned\n" +
            "hearingBooked\n" +
            "hearingHoldingReminder\n" +
            "hearingPostponed\n" +
            "hearingReminder\n" +
            "incompleteApplicationReceived\n" +
            "interlocInformationReceived\n" +
            "directionIssued\n" +
            "judgeDirectionIssued\n" +
            "tcwDirectionIssued\n" +
            "issueFurtherEvidence\n" +
            "dwpLapseCase\n" +
            "hmctsLapseCase\n" +
            "linkACase\n" +
            "sentToJudge\n" +
            "makeCaseUrgent\n" +
            "moveToAppealCreated\n" +
            "dwpNoAction\n" +
            "nonCompliant\n" +
            "dwpNotAbleToRegister\n" +
            "corDecision\n" +
            "reinstateAppeal\n" +
            "reinstateVoidAppeal\n" +
            "reissueFurtherEvidence\n" +
            "requestInfoIncompleteApplication\n" +
            "resendAppealCreated\n" +
            "resendCaseToGAPS2\n" +
            "responseReceived\n" +
            "hmctsResponseReviewed\n" +
            "interlocSendToTcw\n" +
            "sendToDwp\n" +
            "sendToDwpError\n" +
            "sendToDwpOffline\n" +
            "nonCompliantSendToInterloc\n" +
            "tcwReferToJudge\n" +
            "sentToDwp\n" +
            "stitchBundle\n" +
            "judgeDecisionStrikeout\n" +
            "tcwDecisionStrikeOut\n" +
            "struckOut\n" +
            "dwpSupplementaryResponse\n" +
            "unassignToJudge\n" +
            "updateCaseOnly\n" +
            "updateDraft\n" +
            "eventsUpdates\n" +
            "updateHearingType\n" +
            "updateIncompleteApplication\n" +
            "caseUpdated\n" +
            "subscriptionUpdated\n" +
            "uploadCorDocument\n" +
            "uploadDocument\n" +
            "uploadDraftCorDocument\n" +
            "uploadDraftDocument\n" +
            "uploadFurtherEvidence\n" +
            "dwpUploadResponse\n" +
            "validAppealCreated\n" +
            "validAppeal\n" +
            "interlocValidAppeal\n" +
            "voidCase\n" +
            "voidIncompleteApplication\n" +
            "readyToList\n" +
            "interlocVoidAppeal";

    @Test
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
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import org.junit.Test;

public class EventTypeTest {

    // copied from the CaseEvent TAB in the CCD Definition file
    private static final String ALL_EVENT_TYPES = "appealCreated\n" +
            "appealLapsed\n" +
            "appealReceived\n" +
            "appealWithdrawn\n" +
            "evidenceReceived\n" +
            "hearingAdjourned\n" +
            "hearingBooked\n" +
            "hearingPostponed\n" +
            "responseReceived\n" +
            "subscriptionUpdated\n" +
            "appealDormant\n" +
            "assignToJudge\n" +
            "hearingReminder\n" +
            "evidenceReminder\n" +
            "eventsUpdates\n" +
            "addSCnumber\n" +
            "uploadDocument\n" +
            "hearingHoldingReminder\n" +
            "finalHearingHoldingReminder\n" +
            "caseUpdated\n" +
            "panelUpdate\n" +
            "dwpResponseLateReminder\n" +
            "secondHearingHoldingReminder\n" +
            "thirdHearingHoldingReminder\n" +
            "unassignToJudge\n" +
            "updateHearingType\n" +
            "uploadCorDocument\n" +
            "incompleteApplicationReceived\n" +
            "updateIncompleteApplication\n" +
            "requestInfoIncompleteApplication\n" +
            "voidIncompleteApplication\n" +
            "validAppeal\n" +
            "nonCompliant\n" +
            "addRepresentative\n" +
            "nonCompliantSendToInterloc\n" +
            "interlocValidAppeal\n" +
            "createAppealPDF\n" +
            "voidCase\n" +
            "resendCaseToGAPS2\n" +
            "struckOut\n" +
            "reinstateAppeal\n" +
            "corDecision\n" +
            "reinstateVoidAppeal\n" +
            "addNote\n" +
            "sentToJudge\n" +
            "resendAppealCreated\n" +
            "attachRoboticsJson\n" +
            "directionIssued\n" +
            "interlocVoidAppeal\n" +
            "cohQuestionRoundIssued\n" +
            "cohQuestionDeadlineElapsed\n" +
            "cohQuestionDeadlineExtended\n" +
            "cohQuestionDeadlineExtensionDenied\n" +
            "cohQuestionDeadlineExtensionGranted\n" +
            "cohQuestionDeadlineReminder\n" +
            "cohAnswersSubmitted\n" +
            "cohDecisionRejected\n" +
            "cohContinuousOnlineHearingRelisted\n" +
            "cohDecisionIssued\n" +
            "interlocSendToTcw\n" +
            "tcwDirectionIssued\n" +
            "interlocInformationReceived\n" +
            "tcwDecisionStrikeOut\n" +
            "interlocSentToJudge\n" +
            "judgeDecisionAdmitAppeal\n" +
            "tcwDecisionAdmitAppeal\n" +
            "judgeDirectionIssued\n" +
            "judgeDecisionStrikeout\n" +
            "tcwReferToJudge\n" +
            "sendToDwp\n" +
            "sentToDwp\n" +
            "createBundle\n" +
            "editBundle\n" +
            "stitchBundle\n" +
            "createDraft\n" +
            "updateDraft\n" +
            "draftArchived\n" +
            "sendToDwpOffline\n" +
            "validAppealCreated\n" +
            "moveToAppealCreated\n" +
            "uploadDraftDocument\n" +
            "uploadDraftCorDocument\n" +
            "attachScannedDocs\n" +
            "handleEvidence";


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

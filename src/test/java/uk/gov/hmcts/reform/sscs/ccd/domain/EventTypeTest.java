package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.ACTION_STRIKE_OUT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.ADD_SC_NUMBER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.ADJOURNED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CLOSED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.COH_ONLINE_HEARING_RELISTED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.CREATE_APPEAL_PDF;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.DORMANT;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.DWP_RESPOND;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.DWP_RESPOND_OVERDUE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.FINAL_DECISION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.FIRST_HEARING_HOLDING_REMINDER;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.ISSUE_DECISION;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.LAPSED_REVISED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.LINK_A_CASE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.POSTPONED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.REQUEST_INFO_INCOMPLETE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.RESEND_CASE_TO_GAPS2;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SENT_TO_DWP_ERROR;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.SYA_APPEAL_CREATED;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.UPLOAD_DOCUMENT_FURTHER_EVIDENCE;
import static uk.gov.hmcts.reform.sscs.ccd.domain.EventType.WITHDRAWN;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.lang.StringUtils;
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
            "dwpChallengeValidity\n" +
            "validSendToInterloc\n" +
            "dwpRequestTimeExtension\n" +
            "confirmLapsed\n" +
            "actionStrikeOut\n" +
            "sendFurtherEvidenceError\n" +
            "furtherEvidenceHandledOffline\n" +
            "interlocVoidAppeal\n" +
            "uploadDocumentFurtherEvidence\n" +
            "resendToDwp\n" + 
            "issueDecision";

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

    @Test
    public void eventNameIsCorrect() {
        List<EventType> exceptions = Arrays.asList(DWP_RESPOND, ADJOURNED,
            LAPSED_REVISED, WITHDRAWN, POSTPONED, DORMANT, CLOSED, DWP_RESPOND_OVERDUE,
            SYA_APPEAL_CREATED, FIRST_HEARING_HOLDING_REMINDER, FINAL_DECISION, COH_ONLINE_HEARING_RELISTED,
            SENT_TO_DWP_ERROR, REQUEST_INFO_INCOMPLETE, CREATE_APPEAL_PDF, RESEND_CASE_TO_GAPS2, ADD_SC_NUMBER,
            LINK_A_CASE, ACTION_STRIKE_OUT, UPLOAD_DOCUMENT_FURTHER_EVIDENCE, ISSUE_DECISION);
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

package uk.gov.hmcts.reform.sscs.ccd.event;

import java.util.Set;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.Permission;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;
import uk.gov.hmcts.reform.sscs.ccd.domain.UserRole;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionAllowedOrRefusedPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionAnythingElsePage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionAppropriatenessOfBehaviourPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionAppropriatenessOfBehaviourUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionAwardDatesPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionAwarenessOfHazardsPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionAwarenessOfHazardsUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionCommunicatingPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionCommunicationPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionCommunicationUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionConsciousnessPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionConsciousnessUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionCopingWithChangePage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionCopingWithChangeUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionDecisionDatePage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionDecisionNoticeOptionsPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionDetailsOfDecisionPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionDressingAndUndressingPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionDwpReassessTheAwardPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionEngagingWithOtherPeoplePage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionEsaDecisionNoticeOptionsPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionEsaSevereCriteriaPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionGettingAboutPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionGettingAboutUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionLearningTasksPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionLearningTasksUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionLossOfControlPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionLossOfControlUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionMakingBudgetingDecisionsPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionManagingTherapyPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionManagingToiletNeedsPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionManualDexterityPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionManualDexterityUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionMobilisingUnaidedPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionMobilisingUnaidedUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionMovingAroundPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionNavigationPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionNavigationUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPageSectionReferencePage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPanelMemberNamesPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPersonalActionPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPersonalActionUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPickingUpPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPickingUpUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPlanningAndFollowingJourneysPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPreparingFoodPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionPreviewDecisionNoticePage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionReachingPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionReachingUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionReadingAndUnderstandingPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionReasonsPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionRegulation29Page;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSchedule3ActivitiesPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSchedule6Page;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSchedule7ActivitiesPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSchedule8Paragraph4Page;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSelfUnderstoodPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSelfUnderstoodUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSevereCriteriaPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSocialEngagementPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionSocialEngagementUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionStandingAndSittingPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionStandingAndSittingUCPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionTakingNutritionPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionTypeOfAppealPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionTypeOfAwardPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionTypeOfHearingPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionWashingAndBathingPage;
import uk.gov.hmcts.reform.sscs.ccd.event.page.WriteFinalDecisionWorkCapabilityAssessmentPage;

/**
 * Generated by ccd-definition-converter from Benefit on migration; owned by this service team.
 *
 * <p>Event {@code writeFinalDecision} for case type {@code Benefit}.
 */
@Component
public class WriteFinalDecision implements CCDConfig<SscsCaseData, State, UserRole> {
    /**
     * The CCD event ID.
     */
    public static final String WRITE_FINAL_DECISION = "writeFinalDecision";

    /**
     * Registers event {@code writeFinalDecision}.
     *
     * @param builder the config builder
     */
    @Override
    public void configure(ConfigBuilder<SscsCaseData, State, UserRole> builder) {
        var fields = builder.event(WRITE_FINAL_DECISION)
            .forStates(State.INCOMPLETE_APPLICATION, State.INCOMPLETE_APPLICATION_INFORMATION_REQUESTED, State.INTERLOCUTORY_REVIEW_STATE, State.APPEAL_CREATED, State.DORMANT_APPEAL_STATE, State.POST_HEARING, State.WITH_UT, State.HEARING, State.READY_TO_LIST, State.RESPONSE_RECEIVED, State.VALID_APPEAL, State.WITH_DWP, State.NOT_LISTABLE, State.HANDLING_ERROR, State.AWAIT_CONFIDENTIALITY_REQUIREMENTS, State.AWAIT_OTHER_PARTY_DATA)
            .name("Write final decision")
            .description("Write a final decision notice for a case")
            .displayOrder(1850)
            .showSummary()
            .explicitGrants()
            .grant(Set.of(Permission.R), UserRole.CASEWORKER_SSCS)
            .grant(Permission.CRUD, UserRole.CASEWORKER_SSCS_JUDGE, UserRole.CASEWORKER_SSCS_SUPERUSER)
            .fields();
        WriteFinalDecisionTypeOfAppealPage.apply(fields);
        WriteFinalDecisionAllowedOrRefusedPage.apply(fields);
        WriteFinalDecisionTypeOfHearingPage.apply(fields);
        WriteFinalDecisionTypeOfAwardPage.apply(fields);
        WriteFinalDecisionAwardDatesPage.apply(fields);
        WriteFinalDecisionPanelMemberNamesPage.apply(fields);
        WriteFinalDecisionDecisionDatePage.apply(fields);
        WriteFinalDecisionWorkCapabilityAssessmentPage.apply(fields);
        WriteFinalDecisionSchedule6Page.apply(fields);
        WriteFinalDecisionEsaDecisionNoticeOptionsPage.apply(fields);
        WriteFinalDecisionDecisionNoticeOptionsPage.apply(fields);
        WriteFinalDecisionPreparingFoodPage.apply(fields);
        WriteFinalDecisionTakingNutritionPage.apply(fields);
        WriteFinalDecisionManagingTherapyPage.apply(fields);
        WriteFinalDecisionWashingAndBathingPage.apply(fields);
        WriteFinalDecisionManagingToiletNeedsPage.apply(fields);
        WriteFinalDecisionDressingAndUndressingPage.apply(fields);
        WriteFinalDecisionCommunicatingPage.apply(fields);
        WriteFinalDecisionReadingAndUnderstandingPage.apply(fields);
        WriteFinalDecisionEngagingWithOtherPeoplePage.apply(fields);
        WriteFinalDecisionMakingBudgetingDecisionsPage.apply(fields);
        WriteFinalDecisionPlanningAndFollowingJourneysPage.apply(fields);
        WriteFinalDecisionMovingAroundPage.apply(fields);
        WriteFinalDecisionMobilisingUnaidedPage.apply(fields);
        WriteFinalDecisionStandingAndSittingPage.apply(fields);
        WriteFinalDecisionReachingPage.apply(fields);
        WriteFinalDecisionPickingUpPage.apply(fields);
        WriteFinalDecisionManualDexterityPage.apply(fields);
        WriteFinalDecisionSelfUnderstoodPage.apply(fields);
        WriteFinalDecisionCommunicationPage.apply(fields);
        WriteFinalDecisionNavigationPage.apply(fields);
        WriteFinalDecisionLossOfControlPage.apply(fields);
        WriteFinalDecisionConsciousnessPage.apply(fields);
        WriteFinalDecisionLearningTasksPage.apply(fields);
        WriteFinalDecisionAwarenessOfHazardsPage.apply(fields);
        WriteFinalDecisionPersonalActionPage.apply(fields);
        WriteFinalDecisionCopingWithChangePage.apply(fields);
        WriteFinalDecisionGettingAboutPage.apply(fields);
        WriteFinalDecisionSocialEngagementPage.apply(fields);
        WriteFinalDecisionAppropriatenessOfBehaviourPage.apply(fields);
        WriteFinalDecisionMobilisingUnaidedUCPage.apply(fields);
        WriteFinalDecisionStandingAndSittingUCPage.apply(fields);
        WriteFinalDecisionReachingUCPage.apply(fields);
        WriteFinalDecisionPickingUpUCPage.apply(fields);
        WriteFinalDecisionManualDexterityUCPage.apply(fields);
        WriteFinalDecisionSelfUnderstoodUCPage.apply(fields);
        WriteFinalDecisionCommunicationUCPage.apply(fields);
        WriteFinalDecisionNavigationUCPage.apply(fields);
        WriteFinalDecisionLossOfControlUCPage.apply(fields);
        WriteFinalDecisionConsciousnessUCPage.apply(fields);
        WriteFinalDecisionLearningTasksUCPage.apply(fields);
        WriteFinalDecisionAwarenessOfHazardsUCPage.apply(fields);
        WriteFinalDecisionPersonalActionUCPage.apply(fields);
        WriteFinalDecisionCopingWithChangeUCPage.apply(fields);
        WriteFinalDecisionGettingAboutUCPage.apply(fields);
        WriteFinalDecisionSocialEngagementUCPage.apply(fields);
        WriteFinalDecisionAppropriatenessOfBehaviourUCPage.apply(fields);
        WriteFinalDecisionRegulation29Page.apply(fields);
        WriteFinalDecisionSchedule3ActivitiesPage.apply(fields);
        WriteFinalDecisionSchedule8Paragraph4Page.apply(fields);
        WriteFinalDecisionSchedule7ActivitiesPage.apply(fields);
        WriteFinalDecisionSevereCriteriaPage.apply(fields);
        WriteFinalDecisionEsaSevereCriteriaPage.apply(fields);
        WriteFinalDecisionPageSectionReferencePage.apply(fields);
        WriteFinalDecisionDetailsOfDecisionPage.apply(fields);
        WriteFinalDecisionDwpReassessTheAwardPage.apply(fields);
        WriteFinalDecisionReasonsPage.apply(fields);
        WriteFinalDecisionAnythingElsePage.apply(fields);
        WriteFinalDecisionPreviewDecisionNoticePage.apply(fields);
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsUcCaseData {

    private List<String> ucWriteFinalDecisionPhysicalDisabilitiesQuestion;
    private List<String> ucWriteFinalDecisionMentalAssessmentQuestion;
    private String ucWriteFinalDecisionMobilisingUnaidedQuestion;
    private String ucWriteFinalDecisionStandingAndSittingQuestion;
    private String ucWriteFinalDecisionReachingQuestion;
    private String ucWriteFinalDecisionPickingUpQuestion;
    private String ucWriteFinalDecisionManualDexterityQuestion;
    private String ucWriteFinalDecisionMakingSelfUnderstoodQuestion;
    private String ucWriteFinalDecisionCommunicationQuestion;
    private String ucWriteFinalDecisionNavigationQuestion;
    private String ucWriteFinalDecisionLossOfControlQuestion;
    private String ucWriteFinalDecisionConsciousnessQuestion;
    private String ucWriteFinalDecisionLearningTasksQuestion;
    private String ucWriteFinalDecisionAwarenessOfHazardsQuestion;
    private String ucWriteFinalDecisionPersonalActionQuestion;
    private String ucWriteFinalDecisionCopingWithChangeQuestion;
    private String ucWriteFinalDecisionGettingAboutQuestion;
    private String ucWriteFinalDecisionSocialEngagementQuestion;
    private String ucWriteFinalDecisionAppropriatenessOfBehaviourQuestion;
    private String ucWriteFinalDecisionSchedule3ActivitiesApply;
    private List<String> ucWriteFinalDecisionSchedule3ActivitiesQuestion;
    private String dwpReassessTheAward;
}

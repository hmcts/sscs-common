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
public class SscsEsaCaseData {

    private List<String> esaWriteFinalDecisionPhysicalDisabilitiesQuestion;
    private List<String> esaWriteFinalDecisionMentalAssessmentQuestion;
    private String esaWriteFinalDecisionMobilisingUnaidedQuestion;
    private String esaWriteFinalDecisionStandingAndSittingQuestion;
    private String esaWriteFinalDecisionReachingQuestion;
    private String esaWriteFinalDecisionPickingUpQuestion;
    private String esaWriteFinalDecisionManualDexterityQuestion;
    private String esaWriteFinalDecisionMakingSelfUnderstoodQuestion;
    private String esaWriteFinalDecisionCommunicationQuestion;
    private String esaWriteFinalDecisionNavigationQuestion;
    private String esaWriteFinalDecisionLossOfControlQuestion;
    private String esaWriteFinalDecisionConsciousnessQuestion;
    private String esaWriteFinalDecisionLearningTasksQuestion;
    private String esaWriteFinalDecisionAwarenessOfHazardsQuestion;
    private String esaWriteFinalDecisionPersonalActionQuestion;
    private String esaWriteFinalDecisionCopingWithChangeQuestion;
    private String esaWriteFinalDecisionGettingAboutQuestion;
    private String esaWriteFinalDecisionSocialEngagementQuestion;
    private String esaWriteFinalDecisionAppropriatenessOfBehaviourQuestion;
    private String esaWriteFinalDecisionSchedule3ActivitiesApply;
    private List<String> esaWriteFinalDecisionSchedule3ActivitiesQuestion;
    private String dwpReassessTheAward;
}

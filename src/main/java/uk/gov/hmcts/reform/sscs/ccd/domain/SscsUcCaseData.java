package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isNo;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
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
    private YesNo ucWriteFinalDecisionSchedule7ActivitiesApply;
    private List<String> ucWriteFinalDecisionSchedule7ActivitiesQuestion;
    private YesNo doesSchedule8Paragraph4Apply;
    private YesNo showSchedule8Paragraph4Page;
    private YesNo showSchedule7ActivitiesPage;
    private YesNo doesSchedule9Paragraph4Apply;

    @JsonIgnore
    public YesNo getSchedule9Paragraph4Selection() {
        if (isNo(ucWriteFinalDecisionSchedule7ActivitiesApply)) {
            return doesSchedule9Paragraph4Apply;
        }
        return null;
    }

    @JsonIgnore
    public List<String> getSchedule7Selections() {
        if (isYes(ucWriteFinalDecisionSchedule7ActivitiesApply)) {
            return ucWriteFinalDecisionSchedule7ActivitiesQuestion;
        } else if (isNo(ucWriteFinalDecisionSchedule7ActivitiesApply)) {
            return new ArrayList<>();
        }
        return null;
    }
}

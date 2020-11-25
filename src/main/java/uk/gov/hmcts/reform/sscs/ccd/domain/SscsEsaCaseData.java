package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private YesNo doesRegulation29Apply;
    private YesNo showRegulation29Page;
    private YesNo showSchedule3ActivitiesPage;
    private YesNo doesRegulation35Apply;
    @JsonProperty("wcaAppeal")
    private YesNo wcaAppeal;

    @JsonIgnore
    public YesNo getRegulation35Selection() {
        if ("No".equalsIgnoreCase(esaWriteFinalDecisionSchedule3ActivitiesApply)) {
            return doesRegulation35Apply;
        }
        return null;
    }

    @JsonIgnore
    public boolean isWcaAppeal() {
        return isYes(wcaAppeal);
    }

    @JsonIgnore
    public List<String> getSchedule3Selections() {
        if ("Yes".equalsIgnoreCase(esaWriteFinalDecisionSchedule3ActivitiesApply)) {
            return esaWriteFinalDecisionSchedule3ActivitiesQuestion;
        } else if ("No".equalsIgnoreCase(esaWriteFinalDecisionSchedule3ActivitiesApply)) {
            return new ArrayList<>();
        }
        return null;
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SscsPipCaseData {

    private List<String> pipWriteFinalDecisionDailyLivingActivitiesQuestion;
    private List<String> pipWriteFinalDecisionMobilityActivitiesQuestion;
    private String pipWriteFinalDecisionDailyLivingQuestion;
    @JsonProperty("pipWriteFinalDecisionComparedToDWPDailyLivingQuestion")
    private String pipWriteFinalDecisionComparedToDwpDailyLivingQuestion;
    private String pipWriteFinalDecisionMobilityQuestion;
    @JsonProperty("pipWriteFinalDecisionComparedToDWPMobilityQuestion")
    private String pipWriteFinalDecisionComparedToDwpMobilityQuestion;
    private String pipWriteFinalDecisionPreparingFoodQuestion;
    private String pipWriteFinalDecisionTakingNutritionQuestion;
    private String pipWriteFinalDecisionManagingTherapyQuestion;
    private String pipWriteFinalDecisionWashAndBatheQuestion;
    private String pipWriteFinalDecisionManagingToiletNeedsQuestion;
    private String pipWriteFinalDecisionDressingAndUndressingQuestion;
    private String pipWriteFinalDecisionCommunicatingQuestion;
    private String pipWriteFinalDecisionReadingUnderstandingQuestion;
    private String pipWriteFinalDecisionEngagingWithOthersQuestion;
    private String pipWriteFinalDecisionBudgetingDecisionsQuestion;
    private String pipWriteFinalDecisionPlanningAndFollowingQuestion;
    private String pipWriteFinalDecisionMovingAroundQuestion;
}

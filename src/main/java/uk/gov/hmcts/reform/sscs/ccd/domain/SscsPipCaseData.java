package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsPipCaseData {

    @CCD(
            label = "Daily living",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_dailyLivingActivities",
            access = {SscsCrudAccess.class}
    )
    private List<String> pipWriteFinalDecisionDailyLivingActivitiesQuestion;
    @CCD(
            label = "Mobility",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_mobilityActivities",
            access = {SscsCrudAccess.class}
    )
    private List<String> pipWriteFinalDecisionMobilityActivitiesQuestion;
    @CCD(
            label = "What are you considering awarding for daily living?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_rateAwarded",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionDailyLivingQuestion;
    @CCD(
            label = "How would this new award compare to the original FTA award for daily living?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_comparedToDWP",
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("pipWriteFinalDecisionComparedToDWPDailyLivingQuestion")
    private String pipWriteFinalDecisionComparedToDwpDailyLivingQuestion;
    @CCD(
            label = "What are you considering awarding for mobility?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_rateAwarded",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionMobilityQuestion;
    @CCD(
            label = "How would this new award compare to the original FTA award for mobility?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_comparedToDWP",
            access = {SscsCrudAccess.class}
    )
    @JsonProperty("pipWriteFinalDecisionComparedToDWPMobilityQuestion")
    private String pipWriteFinalDecisionComparedToDwpMobilityQuestion;
    @CCD(
            label = "Preparing food",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionPreparingFoodQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionPreparingFoodQuestion;
    @CCD(
            label = "Taking nutrition",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionTakingNutritionQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionTakingNutritionQuestion;
    @CCD(
            label = "Managing therapy or monitoring a health condition",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionManagingTherapyQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionManagingTherapyQuestion;
    @CCD(
            label = "Washing and bathing",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionWashingAndBathingQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionWashAndBatheQuestion;
    @CCD(
            label = "Managing toilet needs or incontinence",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionManagingToiletNeedsQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionManagingToiletNeedsQuestion;
    @CCD(
            label = "Dressing and undressing",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionDressingAndUndressingQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionDressingAndUndressingQuestion;
    @CCD(
            label = "Communicating",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionCommunicatingQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionCommunicatingQuestion;
    @CCD(
            label = "Reading and understanding signs, symbols and words",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionReadingUnderstandingQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionReadingUnderstandingQuestion;
    @CCD(
            label = "Engaging with other people face to face",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionEngagingWithOthersQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionEngagingWithOthersQuestion;
    @CCD(
            label = "Making budgeting decisions",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionBudgetingDecisionsQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionBudgetingDecisionsQuestion;
    @CCD(
            label = "Planning and following journeys",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionPlanningFollowingQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionPlanningAndFollowingQuestion;
    @CCD(
            label = "Moving around",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_pipWriteFinalDecisionMovingAroundQuestions",
            access = {SscsCrudAccess.class}
    )
    private String pipWriteFinalDecisionMovingAroundQuestion;
}

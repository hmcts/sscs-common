package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.ccd.validation.localdate.LocalDateMustNotBeInFuture;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCrudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SscsUcCaseData {

    @CCD(
            label = "Part 1: Physical Disabilities ",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_ucPhysicalDisabilities",
            access = {SscsCrudAccess.class}
    )
    private List<String> ucWriteFinalDecisionPhysicalDisabilitiesQuestion;
    @CCD(
            label = "Part 2: Mental, cognitive and intellectual function assessment ",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_ucMentalAssessment",
            access = {SscsCrudAccess.class}
    )
    private List<String> ucWriteFinalDecisionMentalAssessmentQuestion;
    @CCD(
            label = "1. Mobilising unaided by another person with or without a walking stick, manual wheelchair or other aid if such aid is normally or could reasonably be worn or used.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionMobilisingUnaidedQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionMobilisingUnaidedQuestion;
    @CCD(
            label = "2. Standing and sitting.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionStandingAndSittingQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionStandingAndSittingQuestion;
    @CCD(
            label = "3. Reaching.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionReachingQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionReachingQuestion;
    @CCD(
            label = "4. Picking up and moving or transferring by the use of the upper body and arms.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionPickingUpQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionPickingUpQuestion;
    @CCD(
            label = "5. Manual dexterity.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionManualDexterityQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionManualDexterityQuestion;
    @CCD(
            label = "6, Making self understood through speaking, writing, typing, or other means which are normally or could reasonably be used, unaided by another person.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionMakingSelfUnderstoodQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionMakingSelfUnderstoodQuestion;
    @CCD(
            label = "7. Understanding communication by: (i) verbal means (such as hearing or lip reading) alone; (ii) non-verbal means (such as reading 16 point print or Braille) alone;(iii) or a combination of sub-paragraphs (i) and (ii), using any aid that is normally or could reasonably be used, unaided by another person.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionCommunicationQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionCommunicationQuestion;
    @CCD(
            label = "8. Navigation and maintaining safety using a guide dog or other aid if either or both are normally used or could reasonably be used.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionNavigationQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionNavigationQuestion;
    @CCD(
            label = "9. Absence or loss of control whilst conscious leading to extensive evacuation of the bowel and/or bladder, other than enuresis (bed- wetting), despite the wearing or use of any aids or adaptations which are normally or could reasonably be worn or used.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionLossOfControlQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionLossOfControlQuestion;
    @CCD(
            label = "10. Consciousness during waking moments.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionConsciousnessQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionConsciousnessQuestion;
    @CCD(
            label = "11. Learning tasks.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionLearningTasksQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionLearningTasksQuestion;
    @CCD(
            label = "12. Awareness of everyday hazards (such as boiling water or sharp objects).",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionAwarenessOfHazardsQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionAwarenessOfHazardsQuestion;
    @CCD(
            label = "13. Initiating and completing personal action (which means planning, organisation, problem solving, prioritising or switching tasks).",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionPersonalActionQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionPersonalActionQuestion;
    @CCD(
            label = "14. Coping with change.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionCopingWithChangeQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionCopingWithChangeQuestion;
    @CCD(
            label = "15. Getting about.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionGettingAboutQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionGettingAboutQuestion;
    @CCD(
            label = "16. Coping with social engagement due to cognitive impairment or mental disorder.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionSocialEngagementQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionSocialEngagementQuestion;
    @CCD(
            label = "17. Appropriateness of behaviour with other people, due to cognitive impairment or mental disorder.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_ucWriteFinalDecisionBehaviourQuestion",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionAppropriatenessOfBehaviourQuestion;
    @CCD(
            label = "Do Schedule 7 Activities Apply?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_schedule7ActivitiesApply",
            access = {SscsCrudAccess.class}
    )
    private String ucWriteFinalDecisionSchedule7ActivitiesApply;
    @CCD(
            label = "Schedule 7 Activities ",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_schedule7Activities",
            access = {SscsCrudAccess.class}
    )
    private List<String> ucWriteFinalDecisionSchedule7ActivitiesQuestion;
    @CCD(
            label = "Does schedule 8 paragraph 4 apply?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo doesSchedule8Paragraph4Apply;
    @CCD(label = "Show Schedule 8 Paragraph 4 Page?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo showSchedule8Paragraph4Page;
    @CCD(
            label = "Show the Schedule 7 Activities Page?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo showSchedule7ActivitiesPage;
    @CCD(
            label = "Does Schedule 9 Paragraph 4 apply?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo doesSchedule9Paragraph4Apply;
    @CCD(
            label = "Do the severe conditions criteria apply?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo ucWriteFinalDecisionHasSVIssueCode;
    @CCD(
            label = "What is the start date of the award? ",
            typeOverride = FieldType.Date,
            access = {SscsCrudAccess.class}
    )
    @LocalDateMustNotBeInFuture(message = "Start date must not be in the future")
    private String ucWriteFinalDecisionWorkCapabilityAssessmentStartDate;

    @JsonIgnore
    public YesNo getSchedule9Paragraph4Selection() {
        if ("No".equalsIgnoreCase(ucWriteFinalDecisionSchedule7ActivitiesApply)) {
            return doesSchedule9Paragraph4Apply;
        }
        return null;
    }

    @JsonIgnore
    public List<String> getSchedule7Selections() {
        if ("Yes".equalsIgnoreCase(ucWriteFinalDecisionSchedule7ActivitiesApply)) {
            return ucWriteFinalDecisionSchedule7ActivitiesQuestion;
        } else if ("No".equalsIgnoreCase(ucWriteFinalDecisionSchedule7ActivitiesApply)) {
            return new ArrayList<>();
        }
        return null;
    }
}

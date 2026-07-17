package uk.gov.hmcts.reform.sscs.ccd.domain;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SscsEsaCaseData {

    @CCD(
            label = "Part 1: Physical Disabilities ",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_physicalDisabilities",
            access = {SscsCrudAccess.class}
    )
    private List<String> esaWriteFinalDecisionPhysicalDisabilitiesQuestion;
    @CCD(
            label = "Part 2: Mental, cognitive and intellectual function assessment ",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_mentalAssessment",
            access = {SscsCrudAccess.class}
    )
    private List<String> esaWriteFinalDecisionMentalAssessmentQuestion;
    @CCD(
            label = "1. Mobilising unaided by another person with or without a walking stick, manual wheelchair or other aid if such aid is normally or could reasonably be worn or used.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionMobilisingUnaidedQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionMobilisingUnaidedQuestion;
    @CCD(
            label = "2. Standing and sitting.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionStandingAndSittingQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionStandingAndSittingQuestion;
    @CCD(
            label = "3. Reaching.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionReachingQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionReachingQuestion;
    @CCD(
            label = "4. Picking up and moving or transferring by the use of the upper body and arms.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionPickingUpQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionPickingUpQuestion;
    @CCD(
            label = "5. Manual dexterity.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionManualDexterityQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionManualDexterityQuestion;
    @CCD(
            label = "6, Making self understood through speaking, writing, typing, or other means which are normally or could reasonably be used, unaided by another person.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionMakingSelfUnderstoodQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionMakingSelfUnderstoodQuestion;
    @CCD(
            label = "7. Understanding communication by: (i) verbal means (such as hearing or lip reading) alone; (ii) non-verbal means (such as reading 16 point print or Braille) alone;(iii) or a combination of sub-paragraphs (i) and (ii), using any aid that is normally or could reasonably be used, unaided by another person.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionCommunicationQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionCommunicationQuestion;
    @CCD(
            label = "8. Navigation and maintaining safety using a guide dog or other aid if either or both are normally used or could reasonably be used.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionNavigationQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionNavigationQuestion;
    @CCD(
            label = "9. Absence or loss of control whilst conscious leading to extensive evacuation of the bowel and/or bladder, other than enuresis (bed- wetting), despite the wearing or use of any aids or adaptations which are normally or could reasonably be worn or used.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionLossOfControlQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionLossOfControlQuestion;
    @CCD(
            label = "10. Consciousness during waking moments.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionConsciousnessQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionConsciousnessQuestion;
    @CCD(
            label = "11. Learning tasks.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionLearningTasksQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionLearningTasksQuestion;
    @CCD(
            label = "12. Awareness of everyday hazards (such as boiling water or sharp objects).",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionAwarenessOfHazardsQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionAwarenessOfHazardsQuestion;
    @CCD(
            label = "13. Initiating and completing personal action (which means planning, organisation, problem solving, prioritising or switching tasks).",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionPersonalActionQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionPersonalActionQuestion;
    @CCD(
            label = "14. Coping with change.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionCopingWithChangeQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionCopingWithChangeQuestion;
    @CCD(
            label = "15. Getting about.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionGettingAboutQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionGettingAboutQuestion;
    @CCD(
            label = "16. Coping with social engagement due to cognitive impairment or mental disorder.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionSocialEngagementQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionSocialEngagementQuestion;
    @CCD(
            label = "17. Appropriateness of behaviour with other people, due to cognitive impairment or mental disorder.",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaWriteFinalDecisionBehaviourQuestion",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionAppropriatenessOfBehaviourQuestion;
    @CCD(
            label = "Do Schedule 3 Activities Apply?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_schedule3ActivitiesApply",
            access = {SscsCrudAccess.class}
    )
    private String esaWriteFinalDecisionSchedule3ActivitiesApply;
    @CCD(
            label = "Schedule 3 Activities ",
            hint = "(Select all that apply)",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "FL_schedule3Activities",
            access = {SscsCrudAccess.class}
    )
    private List<String> esaWriteFinalDecisionSchedule3ActivitiesQuestion;
    @CCD(label = "Does regulation 29 apply?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo doesRegulation29Apply;
    @CCD(label = "Show the Regulation 29 Page?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo showRegulation29Page;
    @CCD(
            label = "Show the Schedule 3 Activities Page?",
            typeOverride = FieldType.YesOrNo,
            access = {SscsCrudAccess.class}
    )
    private YesNo showSchedule3ActivitiesPage;
    @CCD(label = "Does regulation 35 apply?", typeOverride = FieldType.YesOrNo, access = {SscsCrudAccess.class})
    private YesNo doesRegulation35Apply;
    @CCD(
            label = "Which ESA Regulations apply?",
            typeOverride = FieldType.FixedRadioList,
            typeParameterOverride = "FL_esaRegulationsYear",
            access = {SscsCrudAccess.class}
    )
    private String whichEsaRegulationsApply;

    @JsonIgnore
    public YesNo getRegulation35Selection() {
        if ("No".equalsIgnoreCase(esaWriteFinalDecisionSchedule3ActivitiesApply)) {
            return doesRegulation35Apply;
        }
        return null;
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

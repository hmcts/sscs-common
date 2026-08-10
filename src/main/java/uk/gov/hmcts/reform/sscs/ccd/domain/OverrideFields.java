package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.domain.AdjournCaseNextHearingDurationUnits;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingRoute;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingState;
import uk.gov.hmcts.ccd.sdk.api.ComplexType;

@ComplexType(name = "overrideFields", generate = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OverrideFields {
    @CCD(label = "Duration of the hearing")
    @JsonInclude
    private Integer duration;
    @CCD(label = "Hearing Interpreter")
    @JsonInclude
    private HearingInterpreter appellantInterpreter;
    @CCD(
            label = "Appellant's Hearing Channel",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_hearingChannel"
    )
    @JsonInclude
    private HearingChannel appellantHearingChannel;
    @CCD(label = "Hearing Window")
    @JsonInclude
    private HearingWindow hearingWindow;
    @CCD(label = "Should be Auto-listed", typeOverride = FieldType.YesOrNo)
    @JsonInclude
    private YesNo autoList;
    @CCD(
            label = "Hearing Venue Choices",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "hearingVenueEpimsId"
    )
    @JsonInclude
    private List<CcdValue<CcdValue<String>>> hearingVenueEpimsIds;
    @CCD(label = "Next hearing type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HmcHearingType hmcHearingType;

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isAllNull() {
        return isNull(duration)
            && isNull(appellantInterpreter)
            && isNull(appellantHearingChannel)
            && isNull(hearingWindow)
            && isNull(autoList)
            && isNull(hearingVenueEpimsIds)
            && isNull(hmcHearingType);
    }

  // ==== ccd-definition-converter: synthesised definition-only fields (retrofit) ====
  @CCD(label = "Duration length")
  private Integer adjournCaseNextHearingListingDuration;
  @CCD(
          label = "Minutes or sessions",
          typeOverride = FieldType.FixedList,
          typeParameterOverride = "FL_adjournCaseNextHearingDurationUnits"
  )
  private AdjournCaseNextHearingDurationUnits adjournCaseNextHearingListingDurationUnits;
  @CCD(label = "What language do they need to speak?", typeOverride = FieldType.DynamicList)
  private String adjournCaseInterpreterLanguageList;
  @CCD(label = "Hearing Route")
  private HearingRoute hearingRoute;
  @CCD(label = "Hearing State", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_hearingState")
  private HearingState hearingState;
  @CCD(
          label = "If an interpreter is needed for the next hearing, please add an extra 30 minutes to the duration.",
          showCondition = "updateListingRequirementsInterpreterDurationLabel=\"DUMMY_VALUE_TO_HIDE_FIELD\"",
          typeOverride = FieldType.Label
  )
  private String updateListingRequirementsInterpreterDurationLabel;
  // ==== end synthesised definition-only fields ====
}

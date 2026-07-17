package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingOptions {

    @CCD(label = "Wants To Attend", typeOverride = FieldType.YesOrNo)
    private String wantsToAttend;
    @CCD(label = "Wants Support", showCondition = "wantsToAttend=\"Yes\"", typeOverride = FieldType.YesOrNo)
    private String wantsSupport;
    @CCD(label = "Language Interpreter", typeOverride = FieldType.YesOrNo)
    private String languageInterpreter;
    @CCD(label = "Languages", showCondition = "languageInterpreter=\"Yes\"")
    @JsonInclude
    private String languages;
    @CCD(label = "Languages", showCondition = "languageInterpreter=\"DoNotShow\"", typeOverride = FieldType.DynamicList)
    private DynamicList languagesList;
    @CCD(label = "Sign languages")
    private String signLanguageType;
    @CCD(
            label = "Hearing arrangement options",
            showCondition = "wantsSupport=\"Yes\"",
            typeOverride = FieldType.MultiSelectList,
            typeParameterOverride = "hearingArrangementOptions"
    )
    private List<String> arrangements;
    @CCD(label = "Unavailable dates", showCondition = "wantsToAttend=\"Yes\"", typeOverride = FieldType.YesOrNo)
    private String scheduleHearing;
    @CCD(
            label = "Excluded Dates",
            showCondition = "scheduleHearing=\"Yes\"",
            typeOverride = FieldType.Collection,
            typeParameterOverride = "dateRange"
    )
    private List<ExcludeDate> excludeDates;
    @CCD(
            label = "Agree less notice of hearing availability",
            showCondition = "wantsToAttend=\"Yes\"",
            typeOverride = FieldType.YesOrNo
    )
    private String agreeLessNotice;
    @CCD(label = "Hearing Route", typeOverride = FieldType.FixedList, typeParameterOverride = "FL_hearingRoute")
    private HearingRoute hearingRoute;
    @CCD(label = "Other Information")
    private String other;
    @CCD(label = "Next hearing type", showCondition = "hmcHearingType=\"*\"")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HmcHearingType hmcHearingType;

    @JsonCreator
    public HearingOptions(@JsonProperty("wantsToAttend") String wantsToAttend,
                          @JsonProperty("wantsSupport") String wantsSupport,
                          @JsonProperty("languageInterpreter") String languageInterpreter,
                          @JsonProperty("languages") String languages,
                          @JsonProperty("languagesList") DynamicList languagesList,
                          @JsonProperty("signLanguageType") String signLanguageType,
                          @JsonProperty("arrangements") List<String> arrangements,
                          @JsonProperty("scheduleHearing") String scheduleHearing,
                          @JsonProperty("excludeDates") List<ExcludeDate> excludeDates,
                          @JsonProperty("agreeLessNotice") String agreeLessNotice,
                          @JsonProperty("hearingRoute") HearingRoute hearingRoute,
                          @JsonProperty("other") String other,
                          @JsonProperty("hmcHearingType") HmcHearingType hmcHearingType) {
        this.wantsToAttend = wantsToAttend;
        this.wantsSupport = wantsSupport;
        this.languageInterpreter = languageInterpreter;
        this.languages = languages;
        this.languagesList = languagesList;
        this.signLanguageType = signLanguageType;
        this.arrangements = arrangements;
        this.scheduleHearing = scheduleHearing;
        this.excludeDates = excludeDates;
        this.agreeLessNotice = agreeLessNotice;
        this.hearingRoute = hearingRoute;
        this.other = other;
        this.hmcHearingType = hmcHearingType;
    }

    @JsonIgnore
    public Boolean isWantsToAttendHearing() {
        return StringUtils.isNotBlank(wantsToAttend) && wantsToAttend.equalsIgnoreCase("yes");
    }

    @JsonIgnore
    public Boolean isAgreeLessNotice() {
        return StringUtils.isNotBlank(agreeLessNotice) && agreeLessNotice.equalsIgnoreCase("yes");
    }

    public Boolean wantsSignLanguageInterpreter() {
        return arrangements != null && arrangements.contains("signLanguageInterpreter");
    }

    public Boolean wantsHearingLoop() {
        return arrangements != null && arrangements.contains("hearingLoop");
    }

    public Boolean wantsAccessibleHearingRoom() {
        return arrangements != null && arrangements.contains("disabledAccess");
    }
}

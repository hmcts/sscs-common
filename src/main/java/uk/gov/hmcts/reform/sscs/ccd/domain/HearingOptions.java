package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude
public class HearingOptions {

    private String wantsToAttend;
    private String wantsSupport;
    private String languageInterpreter;
    @JsonInclude
    private String languages;
    private DynamicList languagesList;
    private String signLanguageType;
    private List<String> arrangements;
    private String scheduleHearing;
    private List<ExcludeDate> excludeDates;
    private String agreeLessNotice;
    private HearingRoute hearingRoute;
    private String other;
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

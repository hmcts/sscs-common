package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingOptions {

    private YesNo wantsToAttend;
    private YesNo wantsSupport;
    private YesNo languageInterpreter;
    private String languages;
    private String signLanguageType;
    private List<String> arrangements;
    private YesNo scheduleHearing;
    private List<ExcludeDate> excludeDates;
    private YesNo agreeLessNotice;
    private String other;

    @JsonCreator
    public HearingOptions(@JsonProperty("wantsToAttend") YesNo wantsToAttend,
                          @JsonProperty("wantsSupport") YesNo wantsSupport,
                          @JsonProperty("languageInterpreter") YesNo languageInterpreter,
                          @JsonProperty("languages") String languages,
                          @JsonProperty("signLanguageType") String signLanguageType,
                          @JsonProperty("arrangements") List<String> arrangements,
                          @JsonProperty("scheduleHearing") YesNo scheduleHearing,
                          @JsonProperty("excludeDates") List<ExcludeDate> excludeDates,
                          @JsonProperty("agreeLessNotice") YesNo agreeLessNotice,
                          @JsonProperty("other") String other) {
        this.wantsToAttend = wantsToAttend;
        this.wantsSupport = wantsSupport;
        this.languageInterpreter = languageInterpreter;
        this.languages = languages;
        this.signLanguageType = signLanguageType;
        this.arrangements = arrangements;
        this.scheduleHearing = scheduleHearing;
        this.excludeDates = excludeDates;
        this.agreeLessNotice = agreeLessNotice;
        this.other = other;
    }

    @JsonIgnore
    public Boolean isWantsToAttendHearing() {
        return isYes(wantsToAttend);
    }

    @JsonIgnore
    public Boolean isAgreeLessNotice() {
        return isYes(agreeLessNotice);
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

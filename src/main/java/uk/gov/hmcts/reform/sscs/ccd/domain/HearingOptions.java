package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingOptions {

    private String wantsToAttend;
    private String wantsSupport;
    private String languageInterpreter;
    private String languages;
    private String signLanguageType;
    private List<String> arrangements;
    private String scheduleHearing;
    private List<ExcludeDate> excludeDates;
    private String other;

    @JsonCreator
    public HearingOptions(@JsonProperty("wantsToAttend") String wantsToAttend,
                          @JsonProperty("wantsSupport") String wantsSupport,
                          @JsonProperty("languageInterpreter") String languageInterpreter,
                          @JsonProperty("languages") String languages,
                          @JsonProperty("signLanguageType") String signLanguageType,
                          @JsonProperty("arrangements") List<String> arrangements,
                          @JsonProperty("scheduleHearing") String scheduleHearing,
                          @JsonProperty("excludeDates") List<ExcludeDate> excludeDates,
                          @JsonProperty("other") String other) {
        this.wantsToAttend = wantsToAttend;
        this.wantsSupport = wantsSupport;
        this.languageInterpreter = languageInterpreter;
        this.languages = languages;
        this.signLanguageType = signLanguageType;
        this.arrangements = arrangements;
        this.scheduleHearing = scheduleHearing;
        this.excludeDates = excludeDates;
        this.other = other;
    }

    @JsonIgnore
    public Boolean isWantsToAttendHearing() {
        return wantsToAttend != null && wantsToAttend.toLowerCase().equals("yes");
    }

    public Boolean getWantsSignLanguageInterpreter() {
        return arrangements.contains("signLanguageInterpreter");
    }

    public Boolean getWantsHearingLoop() {
        return arrangements.contains("hearingLoop");
    }

    public Boolean getWantsAccessibleHearingRoom() {
        return arrangements.contains("disabledAccess");
    }
}

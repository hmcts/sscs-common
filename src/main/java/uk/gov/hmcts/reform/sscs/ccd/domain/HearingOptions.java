package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
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
    private String agreeLessNotice;
    private HearingOptions hearingOptions;
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
                          @JsonProperty("agreeLessNotice") String agreeLessNotice,
                          @JsonProperty("hearingOptions") HearingOptions hearingOptions,
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
        this.hearingOptions = hearingOptions;
        this.other = other;
    }

    @JsonIgnore
    public Boolean isWantsToAttendHearing() {
        return StringUtils.isNotBlank(wantsToAttend) && wantsToAttend.toLowerCase().equals("yes");
    }

    @JsonIgnore
    public Boolean isAgreeLessNotice() {
        return StringUtils.isNotBlank(agreeLessNotice) && agreeLessNotice.toLowerCase().equals("yes");
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

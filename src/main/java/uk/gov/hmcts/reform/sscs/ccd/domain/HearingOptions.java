package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingOptions {

    private String wantsToAttend;
    private String wantsSupport;
    private String languageInterpreter;
    private DynamicList languages;
    private String signLanguageType;
    private List<String> arrangements;
    private String scheduleHearing;
    private List<ExcludeDate> excludeDates;
    private String agreeLessNotice;
    private String other;

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

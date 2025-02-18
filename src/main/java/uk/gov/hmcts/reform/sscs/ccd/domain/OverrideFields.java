package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class OverrideFields {
    private Integer duration;
    private HearingInterpreter appellantInterpreter;
    private HearingChannel appellantHearingChannel;
    private HearingWindow hearingWindow;
    private YesNo autoList;
    private List<CcdValue<CcdValue<String>>> hearingVenueEpimsIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HmcHearingType hmcHearingType;

    public OverrideFields(@JsonProperty Integer duration,
                          @JsonProperty HearingInterpreter appellantInterpreter,
                          @JsonProperty HearingChannel appellantHearingChannel,
                          @JsonProperty HearingWindow hearingWindow,
                          @JsonProperty YesNo autoList,
                          @JsonProperty List<CcdValue<CcdValue<String>>> hearingVenueEpimsIds) {
        this.duration = duration;
        this.appellantInterpreter = appellantInterpreter;
        this.appellantHearingChannel = appellantHearingChannel;
        this.hearingWindow = hearingWindow;
        this.autoList = autoList;
        this.hearingVenueEpimsIds = hearingVenueEpimsIds;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isAllNull() {
        return isNull(duration)
            && isNull(appellantInterpreter)
            && isNull(appellantHearingChannel)
            && isNull(hearingWindow)
            && isNull(autoList)
            && isNull(hearingVenueEpimsIds);
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public boolean isAllNullPostDirections() {
        return isNull(duration)
            && isNull(appellantInterpreter)
            && isNull(appellantHearingChannel)
            && isNull(hearingWindow)
            && isNull(autoList)
            && isNull(hearingVenueEpimsIds)
            && isNull(hmcHearingType);
    }
}

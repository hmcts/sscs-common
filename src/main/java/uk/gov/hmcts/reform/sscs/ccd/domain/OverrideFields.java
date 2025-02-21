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

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OverrideFields {
    @JsonInclude
    private Integer duration;
    @JsonInclude
    private HearingInterpreter appellantInterpreter;
    @JsonInclude
    private HearingChannel appellantHearingChannel;
    @JsonInclude
    private HearingWindow hearingWindow;
    @JsonInclude
    private YesNo autoList;
    @JsonInclude
    private List<CcdValue<CcdValue<String>>> hearingVenueEpimsIds;
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
}

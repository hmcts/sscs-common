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
@JsonInclude
public class OverrideFields {
    private Integer duration;
    private HearingInterpreter appellantInterpreter;
    private HearingChannel appellantHearingChannel;
    private HearingWindow hearingWindow;
    private YesNo autoList;
    private List<CcdValue<CcdValue<String>>> hearingVenueEpimsIds;

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
}

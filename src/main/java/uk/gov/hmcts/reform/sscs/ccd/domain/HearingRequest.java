package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class HearingRequest {
    private LocalDateTime initialRequestTimestamp;

    private YesNo autoListFlag;
    private YesNo inWelshFlag;
    private YesNo isLinkedFlag;
    private YesNo additionalSecurityFlag;
    private YesNo sensitiveFlag;
    private YesNo interpreterRequiredFlag;

    private SscsHearingType hearingType;
    private LocalDateTime firstDateTimeMustBe;
    private HearingWindowRange hearingWindowRange;
    private int duration;
    private HearingPriorityType hearingPriorityType;
    private List<CcdValue<HearingLocation>> hearingLocations;
    private List<CcdValue<String>> facilitiesRequired;
    private String listingComments;
    private String leadJudgeContractType;
    private PanelRequirements panelRequirements;
}

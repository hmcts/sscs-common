package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String initialRequestTimestamp;

    private YesNo autoListFlag;
    private YesNo inWelshFlag;
    private YesNo isLinkedFlag;
    private YesNo additionalSecurityFlag;
    private YesNo sensitiveFlag;
    private YesNo interpreterRequiredFlag;

    private String hearingType;
    private String firstDateTimeMustBe;
    private HearingWindowRange hearingWindowRange;
    private int duration;
    private String hearingPriorityType;
    private List<HearingLocation> hearingLocations;
    private List<String> facilitiesRequired;
    private String listingComments;
    private String leadJudgeContractType;
    private PanelRequirements panelRequirements;
}

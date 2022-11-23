package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulingAndListingFields {
    private HearingRoute hearingRoute;
    private HearingState hearingState;
    private OverrideFields overrideFields;
    private OverrideFields defaultListingValues;
    private List<AmendReason> amendReasons;

    @JsonIgnore
    public boolean isMissingListingRequirements() {
        return isMissingListingRequirements(overrideFields) && isMissingListingRequirements(defaultListingValues);
    }

    @JsonIgnore
    private boolean isMissingListingRequirements(OverrideFields overrideFields) {
        return isNull(overrideFields) || isEmpty(overrideFields.getDuration());
    }
}

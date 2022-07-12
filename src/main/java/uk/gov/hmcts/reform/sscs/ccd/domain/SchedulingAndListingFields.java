package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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
    @Getter(AccessLevel.NONE)
    private OverrideFields overrideFields;
    @Getter(AccessLevel.NONE)
    private OverrideFields defaultOverrideFields;
    private List<AmendReason> amendReasons;

    @JsonIgnore
    public OverrideFields getOverrideFields() {
        if (nonNull(overrideFields)) {
            overrideFields = new OverrideFields();
        }
        return overrideFields;
    }

    @JsonIgnore
    public OverrideFields getDefaultOverrideFields() {
        if (nonNull(defaultOverrideFields)) {
            defaultOverrideFields = new OverrideFields();
        }
        return defaultOverrideFields;
    }

}

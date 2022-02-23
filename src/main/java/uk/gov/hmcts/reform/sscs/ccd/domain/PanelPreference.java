package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class PanelPreference {

    private String memberId;
    private String memberType;
    private RequirementType requirementType;

    public enum RequirementType {
        MUSTINC,
        OPTINC,
        EXCLUDE
    }
}

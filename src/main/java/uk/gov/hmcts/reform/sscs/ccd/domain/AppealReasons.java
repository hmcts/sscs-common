package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppealReasons {
    @CCD(label = "Reasons", typeOverride = FieldType.Collection, typeParameterOverride = "appealReason")
    private List<AppealReason> reasons;
    @CCD(label = "Other Reasons")
    private String otherReasons;

    @JsonCreator
    public AppealReasons(@JsonProperty("reasons") List<AppealReason> reasons,
                         @JsonProperty("otherReasons") String otherReasons) {
        this.reasons = reasons;
        this.otherReasons = otherReasons;
    }
}

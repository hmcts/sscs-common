package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppealReasons {
    private List<AppealReason> reasons;
    private String otherReasons;

    @JsonCreator
    public AppealReasons(@JsonProperty("reasons") List<AppealReason> reasons,
                         @JsonProperty("otherReasons") String otherReasons) {
        this.reasons = reasons;
        this.otherReasons = otherReasons;
    }
}

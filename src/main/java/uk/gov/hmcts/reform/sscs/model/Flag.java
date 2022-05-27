package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Flag {
    @JsonProperty("FlagDetails")
    private List<FlagDetail> flagDetails;
}

package uk.gov.hmcts.reform.sscs.model.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudicialRefDataUsersRequest {

    String ccdServiceName;
    @JsonProperty("object_ids")
    List<String> objectIds;
    @JsonProperty("sidam_ids")
    List<String> sidamIds;
    @JsonProperty("personal_code")
    List<String> personalCodes;
}

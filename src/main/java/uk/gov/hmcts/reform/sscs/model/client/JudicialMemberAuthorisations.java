package uk.gov.hmcts.reform.sscs.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JudicialMemberAuthorisations {

    private String jurisdiction;
    @JsonProperty("service_codes")
    private List<String> serviceCodes;
    @JsonProperty("ticket_code")
    private String ticketCode;
    @JsonProperty("ticket_description")
    private String ticketDescription;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
}

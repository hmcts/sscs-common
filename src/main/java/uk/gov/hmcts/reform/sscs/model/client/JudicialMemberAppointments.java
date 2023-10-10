package uk.gov.hmcts.reform.sscs.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
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
public class JudicialMemberAppointments {


    private String appointment;
    @JsonProperty("appointment_type")
    private String appointmentType;
    @JsonProperty("base_location_id")
    private String baseLocationId;
    @JsonProperty("cft_region")
    private String cftRegion;
    @JsonProperty("cft_region_id")
    private String cftRegionId;
    @JsonProperty("court_name")
    private String courtName;
    @JsonProperty("epimms_id")
    private String epimmsId;
    @JsonProperty("is_principal_appointment")
    private Boolean isPrincipalAppointment;
    private String location;
    @JsonProperty("location_id")
    private String locationId;
    private List<String> roles;
    @JsonProperty("service_code")
    private String serviceCode;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
}

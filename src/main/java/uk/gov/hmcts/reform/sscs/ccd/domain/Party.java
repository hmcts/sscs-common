package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude
@EqualsAndHashCode(callSuper = true)
public abstract class Party extends Entity {
    private String isAppointee;
    private Appointee appointee;

    private Role role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ibcRole;

    private YesNo confidentialityRequired;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime confidentialityRequiredChangedDate;

}

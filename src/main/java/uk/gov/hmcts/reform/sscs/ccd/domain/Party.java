package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude
@EqualsAndHashCode(callSuper = true)
public abstract class Party extends Entity {
    private String isAddressSameAsAppointee;

    private String isAppointee;

    private YesNo showRole;
    private Role role;

    private Appointee appointee;
    private Representative rep;

    private YesNo sendNotification;
}

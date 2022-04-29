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
    private String isAppointee;
    private Appointee appointee;

    private Role role;

    private YesNo confidentialityRequired;
}

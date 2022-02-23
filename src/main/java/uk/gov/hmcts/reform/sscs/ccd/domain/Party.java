package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.*;

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
    private String hasRepresentative;

    private Appointee appointee;
    private Representative rep;

    private YesNo sendNotification;

    @JsonGetter
    public void setHasRepresentative(String rep) {
        if (nonNull(this.rep)) {
            this.rep.setHasRepresentative(rep);
        }
        this.hasRepresentative = rep;
    }

    @JsonSetter
    public String getHasRepresentative() {
        if (nonNull(this.rep) || nonNull(this.hasRepresentative)) {
            return (nonNull(this.rep) && isYes(this.rep.getHasRepresentative())) || isYes(this.hasRepresentative)
                    ? "Yes" : "No";
        } else {
            return null;
        }
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DynamicList confidentialityRequirement;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("confidentialityRequiredConfirmedDate")
    private LocalDateTime confidentialityRequiredChangedDate;

    @JsonIgnore
    public YesNoUnknown getConfidentialityRequiredAnswer() {
        return (nonNull(confidentialityRequirement) && nonNull(confidentialityRequirement.getValue())
            && nonNull(confidentialityRequirement.getValue().getCode()))
            ? YesNoUnknown.valueOf(confidentialityRequirement.getValue().getCode()) : null;
    }

    @JsonIgnore
    public void setConfidentialityRequiredAnswer(final YesNoUnknown confidentialityRequiredAnswer) {
        if (isNull(this.confidentialityRequirement)) {
            setConfidentialityRequirement(nonNull(confidentialityRequiredAnswer) ? new DynamicList(
                new DynamicListItem(confidentialityRequiredAnswer.name(), confidentialityRequiredAnswer.toString()),
                null) : null);
        } else {
            setConfidentialityRequirement(new DynamicList(
                new DynamicListItem(nonNull(confidentialityRequiredAnswer) ? confidentialityRequiredAnswer.name() : null,
                    nonNull(confidentialityRequiredAnswer) ? confidentialityRequiredAnswer.toString() : null),
                confidentialityRequirement.getListItems()));
        }
    }

}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public abstract class Entity {
    private String id;
    private Identity identity;

    private Name name;
    private Address address;
    private Contact contact;

    private String organisation;
    private String organisationType;
    private String cftOrganisationId;

    private YesNo showRole;
    private Role role;

    private YesNo confidentialityRequired;
    private YesNo unacceptableCustomerBehaviour;
    private YesNo vulnerableFlag;
    private String vulnerabilityDetails;

    private ReasonableAdjustmentDetails reasonableAdjustment;

    private Subscription otherPartySubscription;
    private YesNo sendNewOtherPartyNotification;

    private List<RelatedParty> relatedParties;
}

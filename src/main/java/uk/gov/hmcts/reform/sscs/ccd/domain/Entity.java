package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude
public abstract class Entity {
    private String id;
    private Identity identity;

    private Name name;
    private Address address;
    private Contact contact;

    private String organisation;

    private YesNo confidentialityRequired;
    private YesNo unacceptableCustomerBehaviour;
    private YesNo vulnerableFlag;
    private String vulnerabilityDetails;

    private Subscription subscription;
    private ReasonableAdjustmentDetails reasonableAdjustment;
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
public class Party { // Currently a lot of overlap between Appellant, Appointee, Representative and OtherParty Classes, may be idea to consolidate these classes, Party would make sense for a uniform term for all these.
    private String id;
    private Address address;
    private Contact contact;
    private Role role;
    private YesNo showRole;
    private List<RelatedParty> relatedParties;

    private PartyType partyType;
    private PartyDetails partyDetails;

    private HearingOptions hearingOptions;
    private HearingSubtype hearingSubtype;

    private Subscription subscription;
    private YesNo sendNewNotification;

    public enum PartyType {
        IND,
        ORG
    }
}

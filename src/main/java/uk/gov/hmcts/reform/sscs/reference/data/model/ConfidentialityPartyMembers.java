package uk.gov.hmcts.reform.sscs.reference.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfidentialityPartyMembers {

    APPELLANT_OR_APPOINTEE("appellantOrAppointee", "Appellant Or Appointee"),
    JOINT_PARTY("jointParty", "Joint Party"),
    OTHER_PARTY("otherParty", "Other Party"),
    OTHER_PARTY_APPOINTEE("otherPartyAppointee", "Other Party Appointee"),
    OTHER_PARTY_REP("otherPartyRep", "Other Party Rep"),
    REPRESENTATIVE("representative", "Representative"),
    FTA("fta", "FTA");

    private final String code;
    private final String label;
}

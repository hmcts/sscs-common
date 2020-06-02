package uk.gov.hmcts.reform.sscs.idam;

public class UserDetailsTransformer {

    private uk.gov.hmcts.reform.idam.client.models.UserDetails reformUserDetails;

    public UserDetailsTransformer(uk.gov.hmcts.reform.idam.client.models.UserDetails reformUserDetails) {

        this.reformUserDetails = reformUserDetails;
    }

    public uk.gov.hmcts.reform.sscs.idam.UserDetails asLocalUserDetails() {
        return new uk.gov.hmcts.reform.sscs.idam.UserDetails(
                this.reformUserDetails.getId(),
                this.reformUserDetails.getEmail(),
                this.reformUserDetails.getForename(),
                this.reformUserDetails.getSurname().orElseGet(null),
                this.reformUserDetails.getRoles()
                );
    }
}



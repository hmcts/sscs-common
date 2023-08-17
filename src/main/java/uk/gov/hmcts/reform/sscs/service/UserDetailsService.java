package uk.gov.hmcts.reform.sscs.service;

import static java.util.Objects.isNull;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.sscs.idam.UserRole;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

@Service
@AllArgsConstructor
public class UserDetailsService {
    protected final IdamClient idamClient;
    protected final JudicialRefDataService judicialRefDataService;

    public String buildLoggedInUserName(String userAuthorisation) {
        UserDetails userDetails = getUserDetails(userAuthorisation);
        return userDetails.getFullName();
    }

    public String buildLoggedInUserSurname(String userAuthorisation) {
        UserDetails userDetails = getUserDetails(userAuthorisation);
        return userDetails.getSurname().orElse("");
    }

    public String getUserRole(String userAuthorisation) {
        List<String> users = getUserInfo(userAuthorisation).getRoles();

        for (UserRole userRole : UserRole.values()) {
            if (users.contains(userRole.getValue())) {
                return userRole.getLabel();
            }
        }

        return null;
    }

    public UserInfo getUserInfo(String userAuthorisation) {
        UserInfo userInfo = idamClient.getUserInfo(userAuthorisation);
        if (isNull(userInfo)) {
            throw new IllegalStateException("Unable to obtain signed in user info");
        }
        return userInfo;
    }

    private UserDetails getUserDetails(String userAuthorisation) throws IllegalStateException {
        UserDetails userDetails = idamClient.getUserDetails(userAuthorisation);
        if (userDetails == null) {
            throw new IllegalStateException("Unable to obtain signed in user details");
        }
        return userDetails;
    }

    public JudicialUserBase getLoggedInUserAsJudicialUser(String userAuthorisation) {
        UserDetails userDetails = getUserDetails(userAuthorisation);

        String idamId = userDetails.getId();
        String personalCode = judicialRefDataService.getPersonalCode(idamId);

        return new JudicialUserBase(idamId, personalCode);
    }
}

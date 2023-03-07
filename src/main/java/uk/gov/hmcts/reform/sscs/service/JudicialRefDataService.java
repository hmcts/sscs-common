package uk.gov.hmcts.reform.sscs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.client.JudicialRefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataSearchRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUser;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserSearch;
import uk.gov.hmcts.reform.sscs.utility.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudicialRefDataService {

    private final JudicialRefDataApi judicialRefDataApi;
    private final IdamService idamService;

    public String getJudicialUserFullName(String personalCode) {
        log.info("Requesting Judicial User with personal code {}", personalCode);
        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
            .personalCodes(List.of(personalCode)).build();

        List<JudicialUser> judicialUsers = judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        return judicialUsers.get(0).getFullName();
    }

    public String getJudicialUserTitleWithInitialsAndLastName(String personalCode) {
        String name = getJudicialUserFullName(personalCode);

        log.info("Requesting Judicial User with name {} and personal code {}", name, personalCode);
        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataSearchRequest judicialRefDataUsersRequest = JudicialRefDataSearchRequest.builder()
            .searchString(name).build();

        List<JudicialUserSearch> judicialUsers = judicialRefDataApi.searchUsersBySearchString(
            idamTokens.getIdamOauth2Token(), idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        log.info(judicialUsers.toString());

        for (JudicialUserSearch judicialUser : judicialUsers) {
            if (judicialUser.getPersonalCode().equals(personalCode)) {
                return StringUtils.convertNameToTitleInitalsAndSurname(judicialUser);
            }
        }

        log.error("Unable to get {}'s title", name);

        return StringUtils.getInitalsAndSurnameFromName(name);
    }
}

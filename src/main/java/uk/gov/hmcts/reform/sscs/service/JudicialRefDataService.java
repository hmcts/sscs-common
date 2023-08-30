package uk.gov.hmcts.reform.sscs.service;

import static java.util.Objects.nonNull;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.client.JudicialRefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.client.*;
import uk.gov.hmcts.reform.sscs.utility.StringUtils;

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

        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataSearchRequest judicialRefDataUsersRequest = JudicialRefDataSearchRequest.builder()
            .searchString(name).build();

        List<JudicialUserSearch> judicialUsers = judicialRefDataApi.searchUsersBySearchString(
            idamTokens.getIdamOauth2Token(), idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        for (JudicialUserSearch judicialUser : judicialUsers) {
            if (nonNull(judicialUser.getPersonalCode()) && judicialUser.getPersonalCode().equals(personalCode)) {
                return StringUtils.convertNameToTitleInitalsAndSurname(judicialUser);
            }
        }

        return StringUtils.getInitalsAndSurnameFromName(name);
    }

    public String getPersonalCode(@NonNull String idamId) {
        return getJudicialUser(idamId).getPersonalCode();
    }

    public JudicialUserBase getJudicialUser(@NonNull String idamId) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
                .sidamIds(List.of(idamId)).build();

        List<JudicialUser> judicialUsers = judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        if (judicialUsers.size() > 0) {
            JudicialUser judicialUser = judicialUsers.get(0);

            return new JudicialUserBase(judicialUser.getSidamId(), judicialUser.getPersonalCode());
        }

        return null;
    }
}

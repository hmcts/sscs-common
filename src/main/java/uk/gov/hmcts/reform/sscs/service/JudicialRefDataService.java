package uk.gov.hmcts.reform.sscs.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.client.JudicialRefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.client.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudicialRefDataService {

    private final JudicialRefDataApi judicialRefDataApi;
    private final IdamService idamService;
    @Value("${feature.elinksV2.enabled}")
    private boolean elinksV2Feature;

    public List<String> getAllJudicialUsersFullNames(@NonNull List<JudicialUserBase> judicialUsers) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        return judicialUsers.stream()
                .filter(panelMember -> isNotBlank(panelMember.getPersonalCode()))
                .map(panelMember ->
                        getJudicialUserDisplayName(panelMember.getPersonalCode(), idamTokens))
                .filter(Objects::nonNull)
                .toList();
    }

    public String getJudicialUserDisplayName(@NonNull String personalCode) {
        return getJudicialUserDisplayName(personalCode, idamService.getIdamTokens());
    }

    private String getJudicialUserDisplayName(@NonNull String personalCode, IdamTokens idamTokens) {
        log.info("Requesting Judicial User with personal code {}", personalCode);

        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
            .personalCodes(List.of(personalCode)).build();

        List<JudicialUser> judicialUsers = getJudicialUsersUsingSetElinksAPIVerion(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        JudicialUser judicialUser = judicialUsers.get(0);

        return String.format("%s %s %s", judicialUser.getTitle(), splitInitials(judicialUser), judicialUser.getSurname());
    }

    private static String splitInitials(JudicialUser judicialUser) {
        String fullName = judicialUser.getFullName();
        String surname = judicialUser.getSurname();
        String title = judicialUser.getTitle();

        if (isNotBlank(fullName) && isNotBlank(surname) && isNotBlank(title)) {
            String[] givenName = fullName.substring(title.length(), fullName.length() - surname.length() - 1).trim().split("\s");
            return givenName[0].charAt(0) + (givenName.length > 1 ? " " + givenName[1].charAt(0) : "");
        }

        return "";
    }

    public JudicialUserBase getJudicialUserFromPersonalCode(@NonNull String personalCode) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
                .personalCodes(List.of(personalCode)).build();

        List<JudicialUser> judicialUsers = getJudicialUsersUsingSetElinksAPIVerion(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        if (!judicialUsers.isEmpty()) {
            JudicialUser judicialUser = judicialUsers.get(0);

            return new JudicialUserBase(judicialUser.getSidamId(), judicialUser.getPersonalCode());
        }

        return null;
    }

    public String getPersonalCode(@NonNull String idamId) {
        return getJudicialUserFromIdamId(idamId).getPersonalCode();
    }

    public JudicialUserBase getJudicialUserFromIdamId(@NonNull String idamId) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
                .sidamIds(List.of(idamId)).build();

        List<JudicialUser> judicialUsers = getJudicialUsersUsingSetElinksAPIVerion(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        if (!judicialUsers.isEmpty()) {
            JudicialUser judicialUser = judicialUsers.get(0);

            return new JudicialUserBase(judicialUser.getSidamId(), judicialUser.getPersonalCode());
        }

        return null;
    }


    private List<JudicialUser> getJudicialUsersUsingSetElinksAPIVerion(String authorisation, String serviceAuthorization,
                                                            JudicialRefDataUsersRequest judicialRefDataUsersRequest) {
        if (elinksV2Feature) {
            return judicialRefDataApi.getJudicialUsersV2(authorisation, serviceAuthorization, judicialRefDataUsersRequest);
        } else {
            return judicialRefDataApi.getJudicialUsers(authorisation, serviceAuthorization, judicialRefDataUsersRequest);
        }
    }

}

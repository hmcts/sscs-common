package uk.gov.hmcts.reform.sscs.service;

import java.util.List;
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
    @Value("${feature.elinksV2.enabled:false}")
    private boolean elinksV2Feature;

    public String getJudicialUserFullName(@NonNull String personalCode) {
        log.info("Requesting Judicial User with personal code {}", personalCode);
        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
            .personalCodes(List.of(personalCode)).build();

        List<JudicialUser> judicialUsers = getJudicialUsersUsingSetElinksAPIVerion(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        return judicialUsers.get(0).getFullName();
    }

    public JudicialUserBase getJudicialUserFromPersonalCode(@NonNull String personalCode) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
                .personalCodes(List.of(personalCode)).build();

        List<JudicialUser> judicialUsers = getJudicialUsersUsingSetElinksAPIVerion(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest);

        if (judicialUsers.size() > 0) {
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

        if (judicialUsers.size() > 0) {
            JudicialUser judicialUser = judicialUsers.get(0);

            return new JudicialUserBase(judicialUser.getSidamId(), judicialUser.getPersonalCode());
        }

        return null;
    }


    private List<JudicialUser> getJudicialUsersUsingSetElinksAPIVerion(String authorisation, String serviceAuthorization,
                                                            JudicialRefDataUsersRequest judicialRefDataUsersRequest){
        if (elinksV2Feature) {
            return judicialRefDataApi.getJudicialUsersV2(authorisation, serviceAuthorization, judicialRefDataUsersRequest);
        }
        else {
            return judicialRefDataApi.getJudicialUsers(authorisation, serviceAuthorization, judicialRefDataUsersRequest);
        }
    }

}

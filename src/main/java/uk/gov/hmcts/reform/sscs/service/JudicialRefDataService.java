package uk.gov.hmcts.reform.sscs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.client.JudicialRefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudicialRefDataService {

    private final JudicialRefDataApi judicialRefDataApi;
    private final IdamService idamService;

    public List<String> getJudicialUserFullName(String personalCode) {
        log.info("Requesting Judicial User with personal code {}", personalCode);
        IdamTokens idamTokens = idamService.getIdamTokens();

        List<JudicialUser> judicialUsers = judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), JudicialRefDataUsersRequest.builder()
                .personalCodes(List.of(personalCode)).build());

        return judicialUsers.stream().map(JudicialUser::getFullName).collect(Collectors.toList());
    }
}

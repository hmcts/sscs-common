package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.sscs.client.JudicialRefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataSearchRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUser;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserSearch;

@RunWith(MockitoJUnitRunner.class)
public class JudicialRefDataServiceTest {

    @Mock
    private IdamService idamService;
    @Mock
    private JudicialRefDataApi judicialRefDataApi;

    @InjectMocks
    JudicialRefDataService judicialRefDataService;

    @Test
    public void getJudicalUserByPersonalCode() {
        String personalCode = "1234";
        JudicialUser judicialUser = JudicialUser.builder().fullName("Full Name").build();
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();
        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
            .personalCodes(List.of(personalCode)).build();

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest)).thenReturn(List.of(judicialUser));

        String result = judicialRefDataService.getJudicialUserFullName(personalCode);

        assertEquals(result, judicialUser.getFullName());
    }

    @Test
    public void getJudicialUserTitleWithInitialsAndLastNameByPersonalCode() {
        String personalCode = "1234";
        String fullName = "Full Name";
        JudicialUser judicialUser = JudicialUser.builder().fullName(fullName).build();
        IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();
        JudicialRefDataUsersRequest judicialRefDataUsersRequest = JudicialRefDataUsersRequest.builder()
            .personalCodes(List.of(personalCode)).build();

        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequest)).thenReturn(List.of(judicialUser));

        JudicialRefDataSearchRequest judicialRefDataSearchRequest = JudicialRefDataSearchRequest.builder()
            .searchString(fullName).build();
        when(judicialRefDataApi.searchUsersBySearchString(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataSearchRequest)).thenReturn(
                List.of(JudicialUserSearch.builder().personalCode("324123").build(),
                    JudicialUserSearch.builder().title("Mr").personalCode(personalCode).fullName(fullName).build()));

        String result = judicialRefDataService.getJudicialUserTitleWithInitialsAndLastName(personalCode);

        assertEquals(result, "Mr F Name");
    }
}

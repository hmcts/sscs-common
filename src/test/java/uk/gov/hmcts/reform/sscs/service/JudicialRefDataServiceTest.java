package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.sscs.client.JudicialRefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUser;

@RunWith(MockitoJUnitRunner.class)
public class JudicialRefDataServiceTest {
    @Mock
    private IdamService idamService;
    @Mock
    private JudicialRefDataApi judicialRefDataApi;

    @InjectMocks
    private JudicialRefDataService judicialRefDataService;

    private final IdamTokens idamTokens = IdamTokens.builder().idamOauth2Token("auth2").serviceAuthorization("s2s").build();

    private static final String PERSONAL_CODE = "1234";

    private static final String idamId = "4444-4444-4444";
    private final JudicialUser judicialUserCode = JudicialUser.builder().fullName("Full Name").build();
    private final JudicialUser judicialUserIdam = JudicialUser.builder().personalCode(PERSONAL_CODE).build();
    private final JudicialRefDataUsersRequest judicialRefDataUsersRequestCode = JudicialRefDataUsersRequest.builder()
            .personalCodes(List.of(PERSONAL_CODE)).build();
    private final JudicialRefDataUsersRequest judicialRefDataUsersRequestIdam = JudicialRefDataUsersRequest.builder()
            .sidamIds(List.of(idamId)).build();

    @Test
    public void getJudicialUserByPersonalCodeElinksV1() {
        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", false);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestCode)).thenReturn(List.of(judicialUserCode));

        String result = judicialRefDataService.getJudicialUserFullName(PERSONAL_CODE);

        assertEquals(result, judicialUserCode.getFullName());
    }

    @Test
    public void getJudicialUserByPersonalCodeElinksV2() {
        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", true);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsersV2(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestCode)).thenReturn(List.of(judicialUserCode));

        String result = judicialRefDataService.getJudicialUserFullName(PERSONAL_CODE);

        assertEquals(result, judicialUserCode.getFullName());
    }

    @Test
    public void getJudicialUserPersonalCodeWithIdamIdElinksV1() {
        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", false);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestIdam)).thenReturn(List.of(judicialUserIdam));

        String result = judicialRefDataService.getPersonalCode(idamId);

        assertEquals(result, judicialUserIdam.getPersonalCode());
    }

    @Test
    public void getJudicialUserPersonalCodeWithIdamIdElinksV2() {
        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", true);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsersV2(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestIdam)).thenReturn(List.of(judicialUserIdam));

        String result = judicialRefDataService.getPersonalCode(idamId);

        assertEquals(result, judicialUserIdam.getPersonalCode());
    }
}

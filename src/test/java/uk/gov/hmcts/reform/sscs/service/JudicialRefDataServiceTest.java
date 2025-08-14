package uk.gov.hmcts.reform.sscs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.sscs.client.JudicialRefDataApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;
import uk.gov.hmcts.reform.sscs.model.client.JudicialRefDataUsersRequest;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUser;

@ExtendWith(MockitoExtension.class)
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
    private final JudicialUser judicialUserCode = JudicialUser.builder().personalCode(PERSONAL_CODE).build();
    private final JudicialRefDataUsersRequest judicialRefDataUsersRequestCode = JudicialRefDataUsersRequest.builder()
            .personalCodes(List.of(PERSONAL_CODE)).build();
    private final JudicialRefDataUsersRequest judicialRefDataUsersRequestIdam = JudicialRefDataUsersRequest.builder()
            .sidamIds(List.of(idamId)).build();

    @ParameterizedTest
    @ValueSource(strings = {"Dr Verity Gill Bloggs, Dr, VGB, Bloggs, V G",
        "Mr Fitz-William Darcy, Mr, FWD, Darcy, F",
        "Ms Ali Hazelwood, Ms, AH, Hazelwood, A",
        "Mr Chadwick T'Challa Boseman, Mr, CTCB, Boseman, C T",
        "Miss Amelia Mignonette Thermopolis Renaldi, Miss, AMTR, Renaldi, A M"})
    void getJudicialUserByPersonalCodeElinksV1(String judicialUserDataString) {
        String[] judicalUserDataArray = judicialUserDataString.split("\\s*,\\s*");

        JudicialUser judicialUserName1 = JudicialUser.builder()
                .fullName(judicalUserDataArray[0])
                .title(judicalUserDataArray[1])
                .initials(judicalUserDataArray[2])
                .surname(judicalUserDataArray[3])
                .build();

        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", false);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestCode)).thenReturn(List.of(judicialUserName1));

        String result = judicialRefDataService.getJudicialUserDisplayName(PERSONAL_CODE);

        String judicalNoticeName1 = String.format("%s %s %s", judicialUserName1.getTitle(), judicalUserDataArray[4],
                judicialUserName1.getSurname());
        assertEquals(judicalNoticeName1, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Dr Verity Gill Bloggs, Dr, VGB, Bloggs, V G",
        "Mr Fitz-William Darcy, Mr, FWD, Darcy, F",
        "Ms Ali Hazelwood, Ms, AH, Hazelwood, A",
        "Mr Chadwick T'Challa Boseman, Mr, CTCB, Boseman, C T",
        "Miss Amelia Mignonette Thermopolis Renaldi, Miss, AMTR, Renaldi, A M"})
    void getJudicialUserByPersonalCodeElinksV2(String judicialUserDataString) {
        String[] judicalUserDataArray = judicialUserDataString.split("\\s*,\\s*");

        JudicialUser judicialUserName1 = JudicialUser.builder()
                .fullName(judicalUserDataArray[0])
                .title(judicalUserDataArray[1])
                .initials(judicalUserDataArray[2])
                .surname(judicalUserDataArray[3])
                .build();

        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", true);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsersV2(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestCode)).thenReturn(List.of(judicialUserName1));

        String result = judicialRefDataService.getJudicialUserDisplayName(PERSONAL_CODE);

        String judicalNoticeName1 = String.format("%s %s %s", judicialUserName1.getTitle(), judicalUserDataArray[4],
                judicialUserName1.getSurname());
        assertEquals(judicalNoticeName1, result);
    }

    @Test
    void getJudicialUserPersonalCodeWithIdamIdElinksV1() {
        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", false);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsers(idamTokens.getIdamOauth2Token(),
            idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestIdam)).thenReturn(List.of(judicialUserCode));

        String result = judicialRefDataService.getPersonalCode(idamId);

        assertEquals(judicialUserCode.getPersonalCode(), result);
    }

    @Test
    void getJudicialUserPersonalCodeWithIdamIdElinksV2() {
        ReflectionTestUtils.setField(judicialRefDataService, "elinksV2Feature", true);
        when(idamService.getIdamTokens()).thenReturn(idamTokens);
        when(judicialRefDataApi.getJudicialUsersV2(idamTokens.getIdamOauth2Token(),
                idamTokens.getServiceAuthorization(), judicialRefDataUsersRequestIdam)).thenReturn(List.of(judicialUserCode));

        String result = judicialRefDataService.getPersonalCode(idamId);

        assertEquals(judicialUserCode.getPersonalCode(), result);
    }
}

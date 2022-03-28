package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.sscs.service.RegionalProcessingCenterService.*;

import java.util.Map;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.hmcts.reform.sscs.ccd.domain.HearingRoute;
import uk.gov.hmcts.reform.sscs.ccd.domain.RegionalProcessingCenter;

@RunWith(JUnitParamsRunner.class)
public class RegionalProcessingCenterServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private static final RegionalProcessingCenterService regionalProcessingCenterService;

    static {
        AirLookupService airLookupService = new AirLookupService();
        airLookupService.init();
        regionalProcessingCenterService = new RegionalProcessingCenterService(airLookupService);
        regionalProcessingCenterService.init();
    }


    @SuppressWarnings("unused")
    private Object[] getDifferentRpcScenarios() {
        RegionalProcessingCenter liverpoolRpc = RegionalProcessingCenter.builder()
            .name("LIVERPOOL")
            .address1("HM Courts & Tribunals Service")
            .address2("Social Security & Child Support Appeals")
            .address3("Prudential Buildings")
            .address4("36 Dale Street")
            .city("LIVERPOOL")
            .postcode("L2 5UZ")
            .phoneNumber("0300 123 1142")
            .faxNumber("0870 324 0109")
            .email("Liverpool_SYA_Resp@justice.gov.uk")
            .hearingRoute(HearingRoute.GAPS)
            .build();

        RegionalProcessingCenter cardiffRpc = RegionalProcessingCenter.builder()
            .name("CARDIFF")
            .address1("HM Courts & Tribunals Service")
            .address2("Social Security & Child Support Appeals")
            .address3("Eastgate House")
            .address4("Newport Road")
            .city("CARDIFF")
            .postcode("CF24 0AB")
            .phoneNumber("0300 123 1142")
            .faxNumber("0870 739 4438")
            .email("Cardiff_SYA_Respon@justice.gov.uk")
            .hearingRoute(HearingRoute.GAPS)
            .build();


        return new Object[]{
            new Object[]{liverpoolRpc, "SSCS Liverpool"},
            new Object[]{cardiffRpc, "SSCS Cardiff"}
        };
    }
}

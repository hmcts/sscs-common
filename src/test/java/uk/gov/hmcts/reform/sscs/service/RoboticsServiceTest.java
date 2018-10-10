package uk.gov.hmcts.reform.sscs.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static uk.gov.hmcts.reform.sscs.ccd.util.CaseDataUtils.buildCaseData;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.domain.robotics.RoboticsWrapper;
import uk.gov.hmcts.reform.sscs.json.RoboticsJsonMapper;
import uk.gov.hmcts.reform.sscs.json.RoboticsJsonValidator;

public class RoboticsServiceTest {

    private RoboticsJsonMapper roboticsJsonMapper = mock(RoboticsJsonMapper.class);
    private RoboticsJsonValidator roboticsJsonValidator = mock(RoboticsJsonValidator.class);

    private RoboticsService service;

    @Before
    public void setup() {
        service = new RoboticsService(roboticsJsonMapper, roboticsJsonValidator);
    }

    @Test
    public void createValidRoboticsAndReturnAsJsonObject() {

        RoboticsWrapper appeal =
            RoboticsWrapper
                .builder()
                .sscsCaseData(buildCaseData())
                .ccdCaseId(123L).venueName("Bromley")
                .build();

        JSONObject mappedJson = mock(JSONObject.class);

        given(roboticsJsonMapper.map(appeal)).willReturn(mappedJson);

        JSONObject actualRoboticsJson = service.createRobotics(appeal);

        then(roboticsJsonMapper).should(times(1)).map(appeal);
        then(roboticsJsonValidator).should(times(1)).validate(mappedJson);

        assertEquals(mappedJson, actualRoboticsJson);
    }

}

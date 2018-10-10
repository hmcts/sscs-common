package uk.gov.hmcts.reform.sscs.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.domain.robotics.RoboticsWrapper;
import uk.gov.hmcts.reform.sscs.json.RoboticsJsonMapper;
import uk.gov.hmcts.reform.sscs.json.RoboticsJsonValidator;

@Service
public class RoboticsService {

    private final RoboticsJsonMapper roboticsJsonMapper;
    private final RoboticsJsonValidator roboticsJsonValidator;

    @Autowired
    public RoboticsService(
        RoboticsJsonMapper roboticsJsonMapper,
        RoboticsJsonValidator roboticsJsonValidator
    ) {
        this.roboticsJsonMapper = roboticsJsonMapper;
        this.roboticsJsonValidator = roboticsJsonValidator;
    }

    public JSONObject createRobotics(RoboticsWrapper appeal) {

        JSONObject roboticsAppeal =
            roboticsJsonMapper.map(appeal);

        roboticsJsonValidator.validate(roboticsAppeal);

        return roboticsAppeal;
    }

}

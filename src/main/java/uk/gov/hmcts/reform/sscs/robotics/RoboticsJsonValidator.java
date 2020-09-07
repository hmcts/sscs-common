package uk.gov.hmcts.reform.sscs.robotics;

import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoboticsJsonValidator {

    private final String schemaResourceLocation = "/schema/sscs-robotics.json";
    private Schema schema = null;

    public void validate(JSONObject roboticsJson) {

        tryLoadSchema();

        try {
            schema.validate(roboticsJson);
        } catch (ValidationException validationException) {
            log.error("Robotics service failed to validate json", validationException);
            throw new RoboticsValidationException(validationException);
        }
    }

    private synchronized void tryLoadSchema() {

        if (schema != null) {
            return;
        }

        InputStream inputStream = getClass().getResourceAsStream(schemaResourceLocation);
        schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
    }
}

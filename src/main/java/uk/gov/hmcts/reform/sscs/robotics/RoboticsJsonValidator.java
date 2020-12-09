package uk.gov.hmcts.reform.sscs.robotics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.InputStream;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoboticsJsonValidator {

    private final String schemaResourceLocation;
    private JsonSchema schema = null;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public RoboticsJsonValidator(@Value("${robotics.schema.resource.location:/schema/sscs-robotics.json}") String schemaResourceLocation) {
        this.schemaResourceLocation = schemaResourceLocation;
    }

    public void validate(JSONObject roboticsJson) {

        if (schema == null) {
            schema = prepareSchema(schemaResourceLocation);
        }

        try {

            JsonNode jsonNode = toJsonNode(roboticsJson);
            Set<ValidationMessage> validationErrors = validateAgainstSchema(jsonNode);

            if (!validationErrors.isEmpty()) {

                throw new RoboticsValidationException("Robotics service found validation errors", validationErrors);
            }
        } catch (JsonProcessingException jsonTransformException) {
            throw new RoboticsValidationException("Robotics service failed to transform json", null);
        }
    }

    protected JsonSchema prepareSchema(String name) {

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        InputStream inputStream = getClass().getResourceAsStream(name);
        return factory.getSchema(inputStream);
    }

    public JsonNode toJsonNode(JSONObject jsonObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonObj.toString());
    }

    public Set<ValidationMessage>  validateAgainstSchema(JsonNode node) {
        Set<ValidationMessage> errors = schema.validate(node);
        return errors;
    }

}

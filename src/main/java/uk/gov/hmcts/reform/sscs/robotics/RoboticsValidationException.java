package uk.gov.hmcts.reform.sscs.robotics;

import com.networknt.schema.ValidationMessage;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
        reason = "Invalid robotics validation")
public class RoboticsValidationException extends RuntimeException {

    private Set<ValidationMessage> validationErrors;

    public RoboticsValidationException(String message, Set<ValidationMessage> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public Set<ValidationMessage> getValidationErrors() {
        return this.validationErrors;
    }
}

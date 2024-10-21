package uk.gov.hmcts.reform.sscs.ccd.validation.address;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PostcodeValidator implements ConstraintValidator<Postcode, String> {

    private final String url;
    private final boolean enabled;
    private final RestTemplate restTemplate;
    private final List<String> testPostcodes;
    private static final String POSTCODE_RESULT = "true";

    public static String POSTCODE_REGEX = "^((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y]"
        + "[0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z])|([Gg][Ii][Rr]))))\\s?([0-9][A-Za-z]{2})|(0[Aa]{2}))$";

    @Autowired
    public PostcodeValidator() {
        this.url = null;
        this.enabled = false;
        this.restTemplate = null;
        this.testPostcodes = Collections.emptyList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!isBlank(value) && !isPostcodeFormat(value)) {
            return false;
        }
        return true;
    }

    private boolean isPostcodeFormat(String value) {
        return value.matches(POSTCODE_REGEX);
    }

    public boolean isValidPostcodeFormat(String postcode) {
        if (!enabled) {
            log.info("PostcodeValidator is not enabled");
            return true;
        }
        if (testPostcodes.contains(postcode)) {
            log.info("PostcodeValidator received a test postcode ", postcode);
            return true;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<byte[]> response = restTemplate
                    .exchange(url, HttpMethod.GET, requestEntity, byte[].class, postcode);

            logIfNotValidPostCode(postcode, response.getStatusCode().value());
            return response.getStatusCode().value() == 200
                    && nonNull(response.getBody()) && contains(new String(response.getBody()), POSTCODE_RESULT);

        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() != 200) {
                log.info("Post code search returned statusCode {} for postcode {}", e.getRawStatusCode(), postcode);
            }
            return false;
        }
    }

    private void logIfNotValidPostCode(String postCode, int statusCode) {
        if (statusCode != 200) {
            log.info("Post code search returned statusCode {} for postcode {}", statusCode, postCode);
        }
    }
}

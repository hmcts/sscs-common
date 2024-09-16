package uk.gov.hmcts.reform.sscs.ccd.validation.sscscasedata;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.Address;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appellant;
import uk.gov.hmcts.reform.sscs.ccd.domain.Appointee;
import uk.gov.hmcts.reform.sscs.ccd.validation.address.PostcodeValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppealPostcodeHelper {

    private final PostcodeValidator postcodeValidator;
    private static ConstraintValidatorContext context;

    public String resolvePostcode(Appellant appellant) {

        if (appellant == null) {
            return StringUtils.EMPTY;
        }

        return Optional.ofNullable(appellant.getAppointee())
            .map(Appointee::getAddress)
            .map(Address::getPostcode)
            .filter(this::isValidPostcode)
            .orElse(Optional.ofNullable(appellant.getAddress())
                .map(Address::getPostcode)
                .filter(this::isValidPostcode)
                .orElse(StringUtils.EMPTY));
    }

    private boolean isValidPostcode(String postcode) {
        return postcodeValidator.isValid(postcode, context)
            && postcodeValidator.isValidPostcodeFormat(postcode);
    }
}

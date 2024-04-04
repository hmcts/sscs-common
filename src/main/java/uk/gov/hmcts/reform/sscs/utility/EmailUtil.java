package uk.gov.hmcts.reform.sscs.utility;

import java.util.Optional;
import java.util.regex.Pattern;

public final class EmailUtil {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);

    private EmailUtil() {
        // Void
    }

    public static boolean isEmailValid(String value) {
        String cleanEmail = Optional.ofNullable(value).orElse("");
        return EMAIL_REGEX.matcher(cleanEmail).matches();
    }
}
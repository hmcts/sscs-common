package uk.gov.hmcts.reform.sscs.utility;

import static gcardone.junidecode.Junidecode.unidecode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SurnameUtil {

    private SurnameUtil() { }

    public static boolean compare(String surname, String caseDataLastName) {

        String normalizedSurname = normalizeSurname(surname);
        String normalizedCaseDataSurname = normalizeSurname(caseDataLastName);

        return normalizedSurname.equals(normalizedCaseDataSurname);
    }

    private static String normalizeSurname(String surname) {

        return unidecode(removeBracketedAppointee(surname).toLowerCase())
                .replaceAll("[^a-zA-Z]", "");
    }

    private static String removeBracketedAppointee(String surname) {

        Pattern pattern = Pattern.compile("\\(.*appointee.*\\)");
        Matcher matcher = pattern.matcher(surname.toLowerCase());
        System.out.println(surname + " " + matcher.replaceFirst(""));
        return matcher.replaceFirst("");
    }


}


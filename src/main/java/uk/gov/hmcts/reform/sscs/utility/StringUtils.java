package uk.gov.hmcts.reform.sscs.utility;

import uk.gov.hmcts.reform.sscs.model.client.JudicialUserSearch;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
        //
    }

    public static String getGramaticallyJoinedStrings(List<String> strings) {
        StringBuilder result = new StringBuilder();
        if (strings.size() == 1) {
            return strings.get(0);
        } else if (strings.size() > 1) {
            result.append(String.join(", ", strings.subList(0, strings.size() - 1)));
            result.append(" and ");
            result.append(strings.get(strings.size() - 1));
        }
        return result.toString();
    }

    public static String convertNameToTitleInitalsAndSurname(JudicialUserSearch judicialUser) {
        return judicialUser.getTitle() + " " + getInitalsAndSurnameFromName(judicialUser.getFullName());
    }

    public static String getInitalsAndSurnameFromName(String fullName) {
        StringBuilder result = new StringBuilder();

        String[] splitName = fullName.split("\\s+");
        Iterator<String> iterator = Arrays.stream(splitName).iterator();

        while (iterator.hasNext()) {
            String name = iterator.next();

            if (!iterator.hasNext()) {
                result.append(name);
            } else {
                result.append(Character.toUpperCase(name.charAt(0))).append(" ");
            }
        }

        return result.toString();
    }
}

package uk.gov.hmcts.reform.sscs.helper;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TestHelper {

    private TestHelper() {

    }

    public static <T> String getDuplicates(Collection<T> collection) {

        Set<T> duplicates = new LinkedHashSet<>();
        Set<T> uniques = new HashSet<>();

        for (T t : collection) {
            if (!uniques.add(t)) {
                duplicates.add(t);
            }
        }
        return duplicates.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n-"));
    }
}

package uk.gov.hmcts.reform.sscs.helper;

import static java.util.Collections.nCopies;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.exception.ReferenceDataImportException;
import uk.gov.hmcts.reform.sscs.utility.JsonDataReader;

@Slf4j
public final class ReferenceDataHelper {

    private ReferenceDataHelper() {

    }

    public static <T> List<T> getReferenceData(String filename, TypeReference<List<T>> typeReference) {
        try {
            return JsonDataReader.importObjectDataList(filename,  typeReference);
        } catch (IOException exception) {
            throw new ReferenceDataImportException(String
                    .format("Error loading Reference Data while reading from %s, %s",
                            filename, exception.getMessage()), exception);
        }
    }

    public static <T> Map<T, T> generateHashMap(Collection<T> collection) {
        return generateHashMap(collection, reference -> reference);
    }

    public static <R, T> Map<R, T> generateHashMap(Collection<T> collection,
                                                   com.google.common.base.Function<T, R> keyFunction) {
        try {
            return Maps.uniqueIndex(collection, keyFunction);
        } catch (IllegalArgumentException exception) {
            throw new ReferenceDataImportException(String
                    .format("Reference Data Invalid, there are the following duplicates:%n%s%nError Message: %s",
                            getDuplicatesString(collection), exception.getMessage()), exception);
        }
    }

    public static <T> String getDuplicatesString(Collection<T> collection) {
        return getDuplicates(collection)
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n - ", " - ", ""));
    }

    public static <T> List<T> getDuplicates(Collection<T> collection) {
        return collection.stream()
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .filter(n -> n.getValue() > 1)
                .flatMap(n -> nCopies(n.getValue().intValue(), n.getKey()).stream())
                .collect(toList());
    }
}

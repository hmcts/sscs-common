package uk.gov.hmcts.reform.sscs.helper;

import com.fasterxml.jackson.core.type.TypeReference;
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
            ReferenceDataImportException referenceDataException = new ReferenceDataImportException(String
                    .format("Error loading Reference Data while reading from %s, %s",
                            filename, exception.getMessage()), exception);
            log.error("Error", referenceDataException);
            throw referenceDataException;
        }
    }

    public static <T> Map<T, T> generateHashMap(Collection<T> collection) {
        return collection.stream()
                .collect(Collectors
                        .toMap(reference -> reference, reference -> reference, (a, b) -> b));
    }
}

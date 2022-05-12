package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.exception.ReferenceDataImportException;
import uk.gov.hmcts.reform.sscs.utility.JsonDataReader;

@Slf4j
public abstract class ReferenceData {

    protected static <T> List<T> getReferenceData(String filename, TypeReference<List<T>> typeReference) {
        try {
            return JsonDataReader.importObjectDataList(filename,  typeReference);
        } catch (IOException exception) {
            ReferenceDataImportException referenceDataException = new ReferenceDataImportException(String
                    .format("Error loading Reference Data while reading from %s, %s",
                            filename, exception.getMessage()), exception);
            log.error(referenceDataException.getMessage());
            throw referenceDataException;
        }
    }

    protected static <T extends ReferenceData> Map<Integer, T> generateHashMap(List<T> data) {
        return data.stream().collect(Collectors.toMap(ReferenceData::getHash, reference -> reference, (a, b) -> b));
    }

    public abstract Integer getHash();
}

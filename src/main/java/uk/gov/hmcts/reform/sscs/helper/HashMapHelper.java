package uk.gov.hmcts.reform.sscs.helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uk.gov.hmcts.reform.sscs.model.Hashable;

public class HashMapHelper {

    private HashMapHelper(){

    }

    public static <T extends Hashable> Map<Integer, T> generateHashMap(List<T> data) {
        return data.stream().collect(Collectors.toMap(Hashable::getHash, reference -> reference, (a, b) -> b));
    }
}

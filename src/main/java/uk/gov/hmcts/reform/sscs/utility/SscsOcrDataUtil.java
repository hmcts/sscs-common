package uk.gov.hmcts.reform.sscs.utility;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public final class SscsOcrDataUtil {

    private SscsOcrDataUtil() {

    }

    public static boolean findBooleanExists(String... values) {
        for (String v : values) {
            if (StringUtils.isNotBlank(v)) {
                return true;
            }
        }
        return false;
    }

    public static String getField(Map<String, Object> pairs, String field) {
        return pairs != null && pairs.containsKey(field) && pairs.get(field) != null ? StringUtils.trimToNull(pairs.get(field).toString()) : null;
    }
}

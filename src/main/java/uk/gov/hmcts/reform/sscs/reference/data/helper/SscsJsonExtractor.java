package uk.gov.hmcts.reform.sscs.reference.data.helper;
import io.micrometer.core.instrument.util.StringUtils;
import uk.gov.hmcts.reform.sscs.ccd.domain.OcrDataField;
import uk.gov.hmcts.reform.sscs.ccd.domain.ScannedData;
import uk.gov.hmcts.reform.sscs.ccd.domain.ExceptionRecord;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SscsJsonExtractor {

    public ScannedData extractJson(ExceptionRecord exceptionCaseData) {
        Map<String, Object> ocrPairs = build(exceptionCaseData.getOcrDataFields());
        String openingDate = exceptionCaseData.getOpeningDate() != null ? exceptionCaseData.getOpeningDate().toLocalDate().toString() : LocalDate.now().toString();
        return ScannedData.builder().ocrCaseData(ocrPairs).records(exceptionCaseData.getScannedDocuments()).openingDate(openingDate).build();
    }

    public static Map<String, Object> build(List<OcrDataField> exceptionCaseData) {
        Map<String, Object> pairs = new HashMap<>();

        if (exceptionCaseData != null) {
            for (OcrDataField ocrDataField : exceptionCaseData) {
                if (!StringUtils.isEmpty(ocrDataField.getName())) {
                    String value = !StringUtils.isEmpty(ocrDataField.getValue()) ? ocrDataField.getValue() : null;
                    pairs.put(ocrDataField.getName(), value);
                }
            }
        }

        return pairs;
    }
}

package uk.gov.hmcts.reform.sscs.ccd.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ScannedData {

    private Map<String, Object> ocrCaseData;

    private List<InputScannedDoc> records;

    private String openingDate;

}

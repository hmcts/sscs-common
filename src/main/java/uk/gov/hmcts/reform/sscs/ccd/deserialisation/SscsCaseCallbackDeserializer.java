package uk.gov.hmcts.reform.sscs.ccd.deserialisation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.callback.Callback;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

@Component
public class SscsCaseCallbackDeserializer implements Deserializer<Callback<SscsCaseData>> {

    private final ObjectMapper mapper;

    public SscsCaseCallbackDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Callback<SscsCaseData> deserialize(String source) {
        try {
            Callback<SscsCaseData> callback = mapper.readValue(
                    source,
                    new TypeReference<Callback<SscsCaseData>>() {
                    }
            );

            callback.getCaseDetails().getCaseData().setCcdCaseId(String.valueOf(callback.getCaseDetails().getId()));

            return callback;

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not deserialize callback", e);
        }
    }
}

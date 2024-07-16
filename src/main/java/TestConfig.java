import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.hmcts.reform.sscs.ccd.domain.Correction;
import uk.gov.hmcts.reform.sscs.ccd.domain.PostHearing;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;

public class TestConfig {
    public static void main(String[] args) throws JsonProcessingException {
        SscsCaseData sscsCaseData = SscsCaseData.builder()
                .postHearing(PostHearing.builder().build()).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Correction.class, MixIn.class);
        System.out.println(objectMapper.writeValueAsString(sscsCaseData));
    }
}


@JsonInclude(JsonInclude.Include.ALWAYS)
class MixIn {
}

package uk.gov.hmcts.reform.sscs.service;

import static uk.gov.hmcts.reform.sscs.helper.HashMapHelper.generateHashMap;
import static uk.gov.hmcts.reform.sscs.helper.ReferenceDataHelper.getReferenceData;
import static uk.gov.hmcts.reform.sscs.model.HearingDuration.getHash;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.model.HearingDuration;

@Getter
@Setter
@Component
public class HearingDurationsComponent {
    private static final String JSON_DATA_LOCATION = "reference-data/hearing-durations.json";

    private List<HearingDuration> hearingDurations;
    private Map<Integer, HearingDuration> hashMap;

    public HearingDurationsComponent() {
        hearingDurations = getReferenceData(JSON_DATA_LOCATION, new TypeReference<>() {});
        hashMap = generateHashMap(hearingDurations);
    }

    public HearingDuration getHearingDuration(String benefitCode, String issueCode) {
        return getHearingDuration(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode));
    }

    public HearingDuration getHearingDuration(BenefitCode benefitCode, Issue issue) {
        return hashMap.get(getHash(benefitCode, issue));
    }
}

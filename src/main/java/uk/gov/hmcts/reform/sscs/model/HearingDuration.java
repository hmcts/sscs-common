package uk.gov.hmcts.reform.sscs.model;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.exception.HearingDurationImportException;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class HearingDuration {

    private BenefitCode benefitCode;
    private Issue issue;
    private Integer durationFaceToFace;
    private Integer durationInterpreter;
    private Integer durationPaper;

    private static final List<HearingDuration> hearingDurations;

    private static final Map<Integer, HearingDuration> BY_QUERY = new HashMap<>();

    private static final int MULTIPLE_ISSUES_EXTRA_TIME = 15;

    public static final String JSON_DATA_LOCATION = "reference-data/hearing-durations.json";

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            hearingDurations = objectMapper.readValue(new ClassPathResource(JSON_DATA_LOCATION).getInputStream(), new TypeReference<List<HearingDuration>>(){});
        } catch (IOException exception) {
            log.error("Error while reading HearingDuration from " + JSON_DATA_LOCATION + exception.getMessage(), exception);
            throw new HearingDurationImportException("Error while reading HearingDuration from " + JSON_DATA_LOCATION, exception);
        }
        for (HearingDuration hearingDuration: hearingDurations) {
            Integer hash = getQueryHash(hearingDuration.benefitCode, hearingDuration.issue);
            BY_QUERY.put(hash, hearingDuration);
        }
    }

    public Integer getDurationFaceToFace(List<String> elementsDisputed) {
        return addExtraTime(durationFaceToFace, elementsDisputed);
    }

    public Integer getDurationInterpreter(List<String> elementsDisputed) {
        return addExtraTime(durationInterpreter, elementsDisputed);
    }

    public static HearingDuration getHearingDuration(String benefitCode, String issueCode) {
        return getHearingDuration(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode));
    }

    public static HearingDuration getHearingDuration(BenefitCode benefitCode, Issue issue) {
        return BY_QUERY.get(getQueryHash(benefitCode, issue));
    }

    private static Integer getQueryHash(BenefitCode benefitCode, Issue issue) {
        return Objects.hash(benefitCode, issue);
    }

    private Integer addExtraTime(Integer initialDuration, List<String> elements) {
        return nonNull(initialDuration)
                && UC.equals(this.benefitCode) && (UM.equals(this.issue) || US.equals(this.issue))
                && isNotEmpty(elements) && (elements.contains("WC") || elements.contains("SG"))
                ? initialDuration + MULTIPLE_ISSUES_EXTRA_TIME
                : initialDuration;
    }
}

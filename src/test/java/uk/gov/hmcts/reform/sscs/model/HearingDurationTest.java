package uk.gov.hmcts.reform.sscs.model;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode.UC;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.US;

import java.util.*;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.service.HearingDurationsService;

public class HearingDurationTest {

    HearingDurationsService hearingDurations;

    @Before
    public void setup() {
        hearingDurations = new HearingDurationsService();
    }

    @DisplayName("There should be no duplicate hearing durations")
    @Test
    public void hearingDurationsDuplicates() {
        List<HearingDuration> hearingDurationsList = hearingDurations.getHearingDurations();
        Set<HearingDuration> result = new HashSet<>(hearingDurationsList);

        assertThat(result)
                .withFailMessage("There are the following duplicates:\n%s",
                        getDuplicates(hearingDurationsList))
                .hasSameSizeAs(hearingDurationsList);
    }

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration "
            + "the valid Face To Face mapping is returned")
    @Test
    public void getHearingDurationFaceToFace() {
        HearingDuration result = hearingDurations.getHearingDuration("003", "LE");

        assertThat(result.getBenefitCode()).isEqualTo(BenefitCode.PIP_REASSESSMENT_CASE);
        assertThat(result.getIssue()).isEqualTo(Issue.LE);
        assertThat(result.getDurationFaceToFace()).isEqualTo(60);
    }

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration "
            + "the valid Interpreter mapping is returned")
    @Test
    public void getHearingDurationInterpreter() {
        HearingDuration result = hearingDurations.getHearingDuration("003", "LE");

        assertThat(result.getBenefitCode()).isEqualTo(BenefitCode.PIP_REASSESSMENT_CASE);
        assertThat(result.getIssue()).isEqualTo(Issue.LE);
        assertThat(result.getDurationInterpreter()).isEqualTo(90);
    }

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration "
            + "the valid Paper mapping is returned")
    @Test
    public void getHearingDurationPaper() {
        HearingDuration result = hearingDurations.getHearingDuration("003", "LE");

        assertThat(result.getBenefitCode()).isEqualTo(BenefitCode.PIP_REASSESSMENT_CASE);
        assertThat(result.getIssue()).isEqualTo(Issue.LE);
        assertThat(result.getDurationPaper()).isEqualTo(30);
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void addExtraTimeNoElement() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of());

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with no elements Disputed that give extra time are given to getDurationFaceToFace "
            + "the valid duration is returned")
    @Test
    public void addExtraTimeCorrectElement() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of("RY"));

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with the elements Disputed WorkCapability that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void addExtraTimeCorrectWc() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of("WC"));

        assertThat(result).isEqualTo(45);
    }

    @DisplayName("When a list with the elements Disputed SupportGroup that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void addExtraTimeCorrectSg() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of("SG"));

        assertThat(result).isEqualTo(45);
    }

    public static <T> String getDuplicates(Collection<T> collection) {

        Set<T> duplicates = new LinkedHashSet<>();
        Set<T> uniques = new HashSet<>();

        for (T t : collection) {
            if (!uniques.add(t)) {
                duplicates.add(t);
            }
        }
        return duplicates.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n-"));
    }
}

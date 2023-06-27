package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode.UC;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.US;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getDuplicates;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingDuration;
import uk.gov.hmcts.reform.sscs.reference.data.service.HearingDurationsService;

public class HearingDurationsServiceTest {

    HearingDurationsService hearingDurations;

    @Before
    public void setup() {
        hearingDurations = new HearingDurationsService();
    }

    @DisplayName("There should be no duplicate hearing durations")
    @Test
    public void testHearingDurationsDuplicates() {
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
    public void testGHearingDurationFaceToFace() {
        HearingDuration result = hearingDurations.getHearingDuration("003", "LE");

        assertThat(result.getBenefitCode()).isEqualTo(BenefitCode.PIP_REASSESSMENT_CASE);
        assertThat(result.getIssue()).isEqualTo(Issue.LE);
        assertThat(result.getDurationFaceToFace()).isEqualTo(60);
    }

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration "
            + "the valid Interpreter mapping is returned")
    @Test
    public void testGHearingDurationInterpreter() {
        HearingDuration result = hearingDurations.getHearingDuration("003", "LE");

        assertThat(result.getBenefitCode()).isEqualTo(BenefitCode.PIP_REASSESSMENT_CASE);
        assertThat(result.getIssue()).isEqualTo(Issue.LE);
        assertThat(result.getDurationInterpreter()).isEqualTo(90);
    }

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration "
            + "the valid Paper mapping is returned")
    @Test
    public void testGHearingDurationPaper() {
        HearingDuration result = hearingDurations.getHearingDuration("003", "LE");

        assertThat(result.getBenefitCode()).isEqualTo(BenefitCode.PIP_REASSESSMENT_CASE);
        assertThat(result.getIssue()).isEqualTo(Issue.LE);
        assertThat(result.getDurationPaper()).isEqualTo(30);
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void testAddExtraTimeNoElement() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of());

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with no elements Disputed that give extra time are given to getDurationFaceToFace "
            + "the valid duration is returned")
    @Test
    public void testAddExtraTimeCorrectElement() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of("RY"));

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with the elements Disputed WorkCapability that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void testAddExtraTimeCorrectWc() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of("WC"));

        assertThat(result).isEqualTo(45);
    }

    @DisplayName("When a list with the elements Disputed SupportGroup that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void testAddExtraTimeCorrectSg() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of("SG"));

        assertThat(result).isEqualTo(45);
    }

    @DisplayName("When the list contains a non-existent elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void testAddExtraTimeIncorrectIssueCode() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US,List.of("TEST"));
        assertThat(result).isEqualTo(30);
    }
}

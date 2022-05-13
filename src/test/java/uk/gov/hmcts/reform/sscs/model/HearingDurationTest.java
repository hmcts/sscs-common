package uk.gov.hmcts.reform.sscs.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.service.HearingDurationsComponent;

public class HearingDurationTest {

    HearingDurationsComponent hearingDurations;

    @Before
    public void setup() {
        hearingDurations = new HearingDurationsComponent();
    }

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration the valid mapping is returned")
    @Test
    public void getHearingDuration() {
        HearingDuration result = hearingDurations.getHearingDuration("003", "LE");

        assertEquals(BenefitCode.PIP_REASSESSMENT_CASE, result.getBenefitCode());
        assertThat(result.getIssue()).isEqualTo(Issue.LE);
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void getDurationFaceToFace() {
        int result = hearingDurations.getHearingDuration("001", "US")
                .getDurationFaceToFace(List.of());

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationInterpreter"
            + "the valid duration is returned")
    @Test
    public void getDurationInterpreter() {
        int result = hearingDurations.getHearingDuration("001", "US")
                .getDurationInterpreter(List.of());

        assertThat(result).isEqualTo(60);
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void addExtraTimeNoElement() {
        int result = hearingDurations.getHearingDuration("001", "US")
            .addExtraTimeIfNeeded(30, List.of());

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with no elements Disputed that give extra time are given to getDurationFaceToFace "
            + "the valid duration is returned")
    @Test
    public void addExtraTimeCorrectElement() {
        int result = hearingDurations.getHearingDuration("001", "US")
                .addExtraTimeIfNeeded(30,List.of("RY"));
        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with the elements Disputed WorkCapability that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void addExtraTimeCorrectWc() {
        int result = hearingDurations.getHearingDuration("001", "US")
                .addExtraTimeIfNeeded(30,List.of("WC"));

        assertThat(result).isEqualTo(45);
    }

    @DisplayName("When a list with the elements Disputed SupportGroup that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void addExtraTimeCorrectSg() {
        int result = hearingDurations.getHearingDuration("001", "US")
                .addExtraTimeIfNeeded(30,List.of("SG"));

        assertThat(result).isEqualTo(45);
    }
}

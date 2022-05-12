package uk.gov.hmcts.reform.sscs.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;

public class HearingDurationTest {

    @DisplayName("When valid Benefit Code and Issue Code is given to getHearingDuration the valid mapping is returned")
    @Test
    public void getHearingDuration() {
        HearingDuration result = HearingDuration.getHearingDuration("003", "LE");

        Assert.assertEquals(BenefitCode.PIP_REASSESSMENT_CASE,result.getBenefitCode());
        Assert.assertEquals(Issue.LE,result.getIssue());
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void getDurationFaceToFace() {
        int result = HearingDuration.getHearingDuration("001", "US")
                .getDurationFaceToFace(List.of());

        assertEquals(30,result);
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationInterpreter"
            + "the valid duration is returned")
    @Test
    public void getDurationInterpreter() {
        int result = HearingDuration.getHearingDuration("001", "US")
                .getDurationInterpreter(List.of());

        assertEquals(60,result);
    }

    @DisplayName("When an empty list of elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void addExtraTimeNoElement() {
        int result = HearingDuration.getHearingDuration("001", "US").addExtraTimeIfNeeded(30,List.of());

        assertEquals(30,result);
    }

    @DisplayName("When a list with no elements Disputed that give extra time are given to getDurationFaceToFace "
            + "the valid duration is returned")
    @Test
    public void addExtraTimeCorrectElement() {
        int result = HearingDuration.getHearingDuration("001", "US")
                .addExtraTimeIfNeeded(30,List.of("RY"));
        assertEquals(30,result);
    }

    @DisplayName("When a list with the elements Disputed WorkCapability that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void addExtraTimeCorrectWc() {
        int result = HearingDuration.getHearingDuration("001", "US")
                .addExtraTimeIfNeeded(30,List.of("WC"));

        assertEquals(45,result);
    }

    @DisplayName("When a list with the elements Disputed SupportGroup that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void addExtraTimeCorrectSg() {
        int result = HearingDuration.getHearingDuration("001", "US")
                .addExtraTimeIfNeeded(30,List.of("SG"));

        assertEquals(45,result);
    }
}

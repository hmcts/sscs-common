package uk.gov.hmcts.reform.sscs.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;

public class HearingDurationTest {

    @Test
    public void getHearingDuration() {
        HearingDuration result = HearingDuration.getHearingDuration("003", "LE");

        Assert.assertEquals(BenefitCode.PIP_REASSESSMENT_CASE,result.getBenefitCode());
        Assert.assertEquals(Issue.LE,result.getIssue());
    }

    @Test
    public void getDurationFaceToFaceNoElement() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationFaceToFace(List.of());

        assertEquals(30,result);
    }

    @Test
    public void getDurationFaceToFaceNoCorrectElement() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationFaceToFace(List.of("PH"));

        assertEquals(30,result);
    }

    @Test
    public void getDurationFaceToFaceWc() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationFaceToFace(List.of("WC"));

        assertEquals(45,result);
    }

    @Test
    public void getDurationFaceToFaceSg() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationFaceToFace(List.of("SG"));

        assertEquals(45,result);
    }

    @Test
    public void getDurationInterpreterNoElement() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationInterpreter(List.of());

        assertEquals(60,result);
    }

    @Test
    public void getDurationInterpreterNoCorrectElement() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationInterpreter(List.of("PH"));

        assertEquals(60,result);
    }

    @Test
    public void getDurationInterpreterWc() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationInterpreter(List.of("WC"));

        assertEquals(75,result);
    }

    @Test
    public void getDurationInterpreterSg() {
        int result = HearingDuration.getHearingDuration("001", "US").getDurationInterpreter(List.of("SG"));

        assertEquals(75,result);
    }
}

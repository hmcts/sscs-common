package uk.gov.hmcts.reform.sscs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode.UC;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.US;
import static uk.gov.hmcts.reform.sscs.reference.data.helper.ReferenceDataHelper.getDuplicates;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingDuration;
import uk.gov.hmcts.reform.sscs.reference.data.service.HearingDurationsService;

@ExtendWith(MockitoExtension.class)
public class HearingDurationsServiceTest {
    private static final String BENEFIT_CODE = "002";

    private static final String ISSUE_CODE = "DD";

    private static final int DURATION_FACE_TO_FACE = 60;

    private static final int DURATION_INTERPRETER = 75;

    private static final int DURATION_PAPER = 40;

    private HearingDurationsService hearingDurations;

    private SscsCaseData caseData;

    @BeforeEach
    public void setup() {
        hearingDurations = new HearingDurationsService();

        caseData = SscsCaseData.builder()
            .benefitCode(BENEFIT_CODE)
            .issueCode(ISSUE_CODE)
            .appeal(Appeal.builder()
                .hearingOptions(HearingOptions.builder()
                    .build())
                .build())
            .schedulingAndListingFields(SchedulingAndListingFields.builder()
                .overrideFields(OverrideFields.builder()
                    .build())
                .build())
            .build();
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
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US, List.of());

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with no elements Disputed that give extra time are given to getDurationFaceToFace "
            + "the valid duration is returned")
    @Test
    public void testAddExtraTimeCorrectElement() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US, List.of("RY"));

        assertThat(result).isEqualTo(30);
    }

    @DisplayName("When a list with the elements Disputed WorkCapability that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void testAddExtraTimeCorrectWc() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US, List.of("WC"));

        assertThat(result).isEqualTo(45);
    }

    @DisplayName("When a list with the elements Disputed SupportGroup that give extra time are given to getDurationFaceToFace "
            + "the valid duration with extra time is returned")
    @Test
    public void testAddExtraTimeCorrectSg() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US, List.of("SG"));

        assertThat(result).isEqualTo(45);
    }

    @DisplayName("When the list contains a non-existent elements Disputed is given to getDurationFaceToFace"
            + "the valid duration is returned")
    @Test
    public void testAddExtraTimeIncorrectIssueCode() {
        int result = hearingDurations.addExtraTimeIfNeeded(30, UC, US, List.of("TEST"));
        assertThat(result).isEqualTo(30);
    }
  
    @DisplayName("When the benefit or issue code is null "
            + "getHearingDurationBenefitIssueCodes returns null Parameterized Tests")
    @ParameterizedTest
    @CsvSource(value = {
        "null,null",
        "002,null",
        "null,DD",
    }, nullValues = {"null"})
    void getHearingDurationBenefitIssueCodesPaperWithNullSources(String benefitCode, String issueCode) {
        hearingDurations = new HearingDurationsService();
        caseData = SscsCaseData.builder()
            .benefitCode(benefitCode)
            .issueCode(issueCode)
            .appeal(Appeal.builder()
                .hearingSubtype(HearingSubtype.builder().build())
                .hearingOptions(HearingOptions.builder().build())
                .build())
            .build();

        Integer result = hearingDurations.getHearingDurationBenefitIssueCodes(caseData);

        assertThat(result).isNull();
    }

    @DisplayName("When wantsToAttend for the Appeal is Yes and languageInterpreter is null "
            + "getHearingDurationBenefitIssueCodes return the correct face to face durations")
    @Test
    public void getHearingDurationBenefitIssueCodesFaceToFace() {
        setupHearingDurationMap();
        caseData.getAppeal().getHearingOptions().setWantsToAttend("Yes");

        Integer result = hearingDurations.getHearingDurationBenefitIssueCodes(caseData);

        assertThat(result).isEqualTo(DURATION_FACE_TO_FACE);
    }

    @DisplayName("When wantsToAttend for the Appeal is no and the hearing type is not paper "
            + "getHearingDurationBenefitIssueCodes returns null")
    @Test
    public void getHearingDurationBenefitIssueCodesNotPaper() {
        setupHearingDurationMap();
        List<CcdValue<OtherParty>> otherParties = List.of(new CcdValue<>(
            OtherParty.builder()
                .hearingOptions(HearingOptions.builder()
                    .wantsToAttend("yes")
                    .build())
                .hearingSubtype(HearingSubtype.builder()
                    .wantsHearingTypeTelephone("yes")
                    .hearingTelephoneNumber("123123")
                    .build())
                    .build())
        );

        caseData.setOtherParties(otherParties);
        caseData.getAppeal().getHearingOptions().setWantsToAttend("No");

        Integer result = hearingDurations.getHearingDurationBenefitIssueCodes(caseData);

        assertThat(result).isEqualTo(60);
    }

    @DisplayName("When wantsToAttend for the Appeal is No and the hearing type is paper "
            + "getHearingDurationBenefitIssueCodes return the correct paper durations")
    @Test
    public void getHearingDurationBenefitIssueCodesNotAttendNotPaper() {
        setupHearingDurationMap();
        caseData.getAppeal().getHearingOptions().setWantsToAttend("No");
        caseData.setDwpIsOfficerAttending("Yes");

        Integer result = hearingDurations.getHearingDurationBenefitIssueCodes(caseData);

        assertThat(result).isEqualTo(DURATION_PAPER);
    }

    @DisplayName("When wantsToAttend for the Appeal is no and the hearing type is paper "
            + "getHearingDurationBenefitIssueCodes return the correct paper durations")
    @Test
    public void getHearingDurationBenefitIssueCodesPaper() {
        setupHearingDurationMap();
        caseData.getAppeal().getHearingOptions().setWantsToAttend("No");

        Integer result = hearingDurations.getHearingDurationBenefitIssueCodes(caseData);

        assertThat(result).isEqualTo(DURATION_PAPER);
    }

    @DisplayName("When wantsToAttend for the Appeal is Yes "
            + "getHearingDurationBenefitIssueCodes return the correct interpreter durations")
    @Test
    public void getHearingDurationBenefitIssueCodesInterpreter() {
        setupHearingDurationMap();
        caseData.getAppeal().getHearingOptions().setWantsToAttend("Yes");
        caseData.getAppeal().getHearingOptions().setLanguageInterpreter("Yes");

        Integer result = hearingDurations.getHearingDurationBenefitIssueCodes(caseData);

        assertThat(result).isEqualTo(DURATION_INTERPRETER);
    }

    private void setupHearingDurationMap() {
        HashMap<HearingDuration, HearingDuration> hearingDurationsMap = new HashMap<>();
        hearingDurationsMap.put(new HearingDuration(BenefitCode.getBenefitCode(BENEFIT_CODE), Issue.getIssue(ISSUE_CODE)),
                new HearingDuration(
                        BenefitCode.PIP_NEW_CLAIM,
                        Issue.DD,
                        DURATION_FACE_TO_FACE,
                        DURATION_INTERPRETER,
                        DURATION_PAPER
                ));
        ReflectionTestUtils.setField(hearingDurations, "hearingDurationsHashMap", hearingDurationsMap);
    }
}

package uk.gov.hmcts.reform.sscs.utility;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;

public class AppealNumberGenerator {
    private static final int LENGTH = 10;
    private static final char MINIMUM_CODE_POINT = '0';
    private static final char MAXIMUM_CODE_POINT = 'z';
    private static final List<String> DRAFT_DRAFT_ARCHIVED_STATE = Arrays.asList(State.DRAFT.getId(), State.DRAFT_ARCHIVED.getId());
    private static final List<String> DORMANT_VOID_STATE = Arrays.asList(State.DORMANT_APPEAL_STATE.getId(), State.VOID_STATE.getId());

    private AppealNumberGenerator() {
        //Empty
    }

    public static String generateAppealNumber() {
        SecureRandom random = new SecureRandom();
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(MINIMUM_CODE_POINT, MAXIMUM_CODE_POINT)
                .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS).usingRandom(random::nextInt)
                .build();
        return generator.generate(LENGTH);
    }

    public static boolean filterCaseNotDraftOrArchivedDraft(SscsCaseDetails sscsCaseDetails) {
        return !(sscsCaseDetails != null && (DRAFT_DRAFT_ARCHIVED_STATE.contains(sscsCaseDetails.getState())));
    }

    public static boolean filterActiveCasesForCitizen(SscsCaseDetails sscsCaseDetails) {
        return !(sscsCaseDetails != null && (DRAFT_DRAFT_ARCHIVED_STATE.contains(sscsCaseDetails.getState())
                || DORMANT_VOID_STATE.contains(sscsCaseDetails.getState())));
    }

    public static boolean filterDormantCasesForCitizen(SscsCaseDetails sscsCaseDetails) {
        return (sscsCaseDetails != null && (DORMANT_VOID_STATE.contains(sscsCaseDetails.getState())));
    }
}

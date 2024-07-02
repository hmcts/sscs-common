package uk.gov.hmcts.reform.sscs.utility;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;
import static uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel.*;
import static uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel.FACE_TO_FACE;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;

public class HearingChannelUtil {

    private HearingChannelUtil() {
    }

    public static boolean isPaperCase(SscsCaseData caseData) {
        return PAPER == getHearingChannel(caseData);
    }

    public static HearingChannel getHearingChannel(@Valid SscsCaseData caseData) {
        List<HearingChannel> hearingChannels = getAllHearingChannelPreferences(caseData);

        if (hearingChannels.contains(FACE_TO_FACE)) {
            return FACE_TO_FACE;
        } else if (hearingChannels.contains(VIDEO)) {
            return VIDEO;
        } else if (hearingChannels.contains(TELEPHONE)) {
            return TELEPHONE;
        } else {
            return PAPER;
        }
    }

    public static List<HearingChannel> getAllHearingChannelPreferences(@Valid SscsCaseData caseData) {
        HearingChannel individualPreferredHearingChannel = getIndividualPreferredHearingChannel(
                caseData.getAppeal().getHearingSubtype(),
                caseData.getAppeal().getHearingOptions(),
                caseData.getSchedulingAndListingFields().getOverrideFields()
        );

        List<HearingChannel> hearingChannels = new ArrayList<>();

        hearingChannels.add(individualPreferredHearingChannel);

        if (nonNull(caseData.getOtherParties())) {
            hearingChannels.addAll(
                caseData.getOtherParties().stream()
                    .map(CcdValue::getValue)
                    .map(otherParty -> getIndividualPreferredHearingChannel(
                        otherParty.getHearingSubtype(),
                        otherParty.getHearingOptions(), null)).toList());
        }

        return hearingChannels;
    }

    public static HearingChannel getIndividualPreferredHearingChannel(HearingSubtype hearingSubtype,
                                                               HearingOptions hearingOptions,
                                                               OverrideFields overrideFields) {

        if (nonNull(overrideFields) && nonNull(overrideFields.getAppellantHearingChannel())) {
            return overrideFields.getAppellantHearingChannel();
        }

        if (isNull(hearingOptions)) {
            return null;
        }

        if (isFalse(hearingOptions.isWantsToAttendHearing())) {
            return NOT_ATTENDING;
        }

        if (nonNull(hearingSubtype)) {
            if (isYes(hearingSubtype.getWantsHearingTypeFaceToFace())) {
                return FACE_TO_FACE;
            } else if (shouldPreferVideoHearingChannel(hearingSubtype)) {
                return VIDEO;
            } else if (shouldPreferTelephoneHearingChannel(hearingSubtype)) {
                return TELEPHONE;
            }
        } else {
            return null;
        }

        if (isTrue(hearingOptions.isWantsToAttendHearing())) {
            return FACE_TO_FACE;
        }

        throw new IllegalStateException("Failed to determine a preferred hearing channel");
    }

    public static boolean shouldPreferVideoHearingChannel(HearingSubtype hearingSubtype) {
        return isYes(hearingSubtype.getWantsHearingTypeVideo())
                && nonNull(hearingSubtype.getHearingVideoEmail());
    }

    public static boolean shouldPreferTelephoneHearingChannel(HearingSubtype hearingSubtype) {
        return isYes(hearingSubtype.getWantsHearingTypeTelephone()) && nonNull(hearingSubtype.getHearingTelephoneNumber());
    }

    public static boolean isInterpreterRequired(SscsCaseData caseData) {
        Appeal appeal = caseData.getAppeal();
        return isYes(caseData.getAdjournment().getInterpreterRequired())
                || isInterpreterRequiredHearingOptions(appeal.getHearingOptions())
                || isInterpreterRequiredOtherParties(caseData.getOtherParties());
    }

    public static boolean isInterpreterRequiredOtherParties(List<CcdValue<OtherParty>> otherParties) {
        return nonNull(otherParties) && otherParties.stream().map(CcdValue::getValue)
                .anyMatch(o -> isInterpreterRequiredHearingOptions(o.getHearingOptions()));
    }

    public static boolean isInterpreterRequiredHearingOptions(HearingOptions hearingOptions) {
        return  isYes(hearingOptions.getLanguageInterpreter()) || hearingOptions.wantsSignLanguageInterpreter();
    }
}

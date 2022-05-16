package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SignLanguage {

    AMERICAN_SIGN_LANGUAGE("americanSignLanguage", "American Sign Language (ASL)"),
    BRITISH_SIGN_LANGUAGE("britishSignLanguage", "British Sign Language (BSL)"),
    HANDS_ON_SIGNING("handsOnSigning", "Hands on signing"),
    INTERNATIONAL_SIGN("internationalSign", "International Sign (IS)"),
    LIPSPEAKER("lipspeaker", "Lipspeaker"),
    MAKATON("makaton", "Makaton"),
    DEAFBLIND_MANUAL_ALPHABET("deafblindManualAlphabet", "Deafblind manual alphabet"),
    NOTETAKER("notetaker", "Notetaker"),
    DEAF_RELAY("deafRelay", "Deaf Relay"),
    SPEECH_SUPPORTED_ENGLISH("speechSupportedEnglish", "Speech Supported English (SSE)"),
    VISUAL_FRAME_SIGNING("visualFrameSigning", "Visual frame signing"),
    PALANTYPIST("palantypist", "Palantypist / Speech to text");

    private final String hmcReference;
    private final String ccdReference;

    public static SignLanguage getSignLanguageKeyByCcdReference(String value) {
        return Arrays.stream(SignLanguage.values())
                .filter(sl -> sl.getCcdReference().equals(value))
                .findFirst()
                .orElse(null);
    }
}
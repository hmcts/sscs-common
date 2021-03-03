package uk.gov.hmcts.reform.sscs.ccd.callback;

import lombok.Getter;

@Getter
public enum DwpDocumentType {
    TL1_FORM("tl1Form", "TL1 Form"),
    UCB("ucb", "Unacceptable customer behaviour document"),
    APPENDIX_12("appendix12", "Appendix 12 document"),
    DWP_LT_203("dwpLT203", "LT203 or OM1 document"),
    DWP_CHALLENGE_VALIDITY("dwpChallengeValidityDocument", "DWP challenge validity document"),
    DWP_LAPSE_LETTER("dwpLapseLetter", "Lapse letter"),
    DWP_RESPONSE("dwpResponse", "DWP response"),
    DWP_EDITED_RESPONSE("dwpEditedResponse", "DWP edited response"),
    AT_38("at38", "AT38"),
    DWP_EVIDENCE_BUNDLE("dwpEvidenceBundle", "DWP evidence bundle"),
    DWP_EDITED_EVIDENCE_BUNDLE("dwpEditedEvidenceBundle", "DWP edited evidence bundle"),
    AUDIO_DOCUMENT("audioDocument", "Audio document"),
    VIDEO_DOCUMENT("videoDocument", "Video document");


    private String value;
    private String label;

    DwpDocumentType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}

package uk.gov.hmcts.reform.sscs.config;

import uk.gov.hmcts.reform.sscs.ccd.callback.CallbackType;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.FIRST_NAME;
import static uk.gov.hmcts.reform.sscs.config.SscsConstants.TITLE;
import static uk.gov.hmcts.reform.sscs.ccd.callback.CallbackType.EXCEPTION_CALLBACK;

public enum WarningMessage {

    APPELLANT_TITLE(TITLE, "Appellant title"),
    APPOINTEE_TITLE(TITLE, "Appointee title"),
    REPRESENTATIVE_TITLE(TITLE, "Representative title"),
    OTHER_PARTY_TITLE(TITLE, "Other party title"),
    APPELLANT_FIRST_NAME(FIRST_NAME, "Appellant first name"),
    APPOINTEE_FIRST_NAME(FIRST_NAME, "Appointee first name"),
    REPRESENTATIVE_FIRST_NAME(FIRST_NAME, "Representative first name"),
    OTHER_PARTY_FIRST_NAME(FIRST_NAME, "Other party first name"),
    BENEFIT_TYPE_DESCRIPTION(SscsConstants.BENEFIT_TYPE_DESCRIPTION, "Benefit type description"),
    BENEFIT_TYPE_OTHER(SscsConstants.BENEFIT_TYPE_OTHER, "Benefit type description"),
    MRN_DATE(SscsConstants.MRN_DATE, "Mrn date"),
    HEARING_TYPE("is_hearing_type_oral and/or is_hearing_type_paper", "Hearing type"),
    REPRESENTATIVE_NAME_OR_ORGANISATION("representative_company, representative_first_name and representative_last_name", "Representative organisation, Representative first name and Representative last name"),
    HEARING_TELEPHONE_NUMBER_MULTIPLE("Telephone hearing selected but the number used is invalid. Please check either the hearing_telephone_number or person1_phone fields", "Telephone hearing selected but the number used is invalid. Please check either the telephone or hearing telephone number fields"),

    HEARING_SUB_TYPE_TELEPHONE_OR_VIDEO_FACE_TO_FACE("hearing_type_telephone, hearing_type_video and hearing_type_face_to_face", "Hearing option telephone, video and face to face"),
    PERSON1_CHILD_MAINTENANCE_NUMBER("person1_child_maintenance_number", "Child maintenance number"),
    APPELLANT_PARTY_NAME("is_paying_parent, is_receiving_parent, is_another_party and other_party_details", "Appellant role and/or description"),
    APPELLANT_PARTY_DESCRIPTION("other_party_details", "Appellant role and/or description");
    private String exceptionRecordMessage;
    private String validationRecordMessage;

    WarningMessage(String exceptionRecordMessage, String validationRecordMessage) {
        this.exceptionRecordMessage = exceptionRecordMessage;
        this.validationRecordMessage = validationRecordMessage;
    }

    public static String getMessageByCallbackType(CallbackType callbackType, String personType, String name, String endMessage) {
        String startMessage =  callbackType == EXCEPTION_CALLBACK
                ? personType + valueOf(name.toUpperCase()).exceptionRecordMessage
                : valueOf(name.toUpperCase()).validationRecordMessage;

        return endMessage != null ? startMessage + " " + endMessage : startMessage;
    }

}

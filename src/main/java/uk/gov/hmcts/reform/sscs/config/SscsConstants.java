package uk.gov.hmcts.reform.sscs.config;

public class SscsConstants {

    private SscsConstants() {
        //
    }
    public static final String ZONE_ID = "Europe/London";
    public static final String PERSON_1_CHILD_MAINTENANCE_NUMBER = "person1_child_maintenance_number";
    public static final String OTHER_PARTY_VALUE = "other_party";
    public static final String REPRESENTATIVE_VALUE = "representative";
    public static final String HEARING_TYPE_ORAL = "oral";
    public static final String HEARING_TYPE_PAPER = "paper";
    public static final String HEARING_OPTIONS_EXCLUDE_DATES_LITERAL = "hearing_options_exclude_dates";
    public static final String BENEFIT_TYPE_DESCRIPTION = "benefit_type_description";
    public static final String BENEFIT_TYPE_OTHER = "benefit_type_other";
    public static final String HEARING_TYPE_DESCRIPTION = "hearing_type";
    public static final String REPRESENTATIVE_NAME_OR_ORGANISATION_DESCRIPTION = "representative_name_or_organisation";
    public static final String MRN_DATE = "mrn_date";
    public static final String ISSUING_OFFICE = "office";
    public static final String YES_LITERAL = "Yes";
    public static final String TITLE = "_title";
    public static final String FIRST_NAME = "_first_name";
    public static final String LAST_NAME = "_last_name";
    public static final String ADDRESS_LINE1 = "_address_line1";
    public static final String ADDRESS_LINE2 = "_address_line2";
    public static final String ADDRESS_LINE3 = "_address_line3";
    public static final String ADDRESS_LINE4 = "_address_line4";
    public static final String ADDRESS_POSTCODE = "_postcode";
    public static final String MOBILE = "_mobile";
    public static final String NINO = "_nino";
    public static final String DOB = "_dob";
    public static final String IS_EMPTY = "is empty";
    public static final String IS_MISSING = "is missing";
    public static final String ARE_EMPTY = "are empty. At least one must be populated";
    public static final String IS_INVALID = "is invalid";
    public static final String HAS_INVALID_ADDRESS  = "has invalid characters at the beginning";
    public static final String IS_IN_FUTURE = "is in future";
    public static final String IS_IN_PAST = "is in past";
    public static final String HEARING_TYPE_TELEPHONE_LITERAL = "hearing_type_telephone";
    public static final String HEARING_TELEPHONE_NUMBER_MULTIPLE_LITERAL = "hearing_telephone_number_multiple";
    public static final String HEARING_TYPE_VIDEO_LITERAL = "hearing_type_video";
    public static final String HEARING_SUB_TYPE_TELEPHONE_OR_VIDEO_FACE_TO_FACE_DESCRIPTION = "hearing_sub_type_telephone_or_video_face_to_face";
    public static final String PHONE_SELECTED_NOT_PROVIDED = "has not been provided but data indicates hearing telephone is required";
    public static final String EMAIL_SELECTED_NOT_PROVIDED = "has not been provided but data indicates hearing video is required";
    public static final String HAS_REPRESENTATIVE_FIELD_MISSING = "The \"Has representative\" field is not selected, please select an option to proceed";
}
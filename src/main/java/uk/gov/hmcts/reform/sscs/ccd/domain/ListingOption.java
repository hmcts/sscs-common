package uk.gov.hmcts.reform.sscs.ccd.domain;

public enum ListingOption {

    READY_TO_LIST("readyToList"),
    NOT_LISTABLE("notListable");

    private String value;

    ListingOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
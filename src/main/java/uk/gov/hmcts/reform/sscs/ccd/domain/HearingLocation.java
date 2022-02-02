package uk.gov.hmcts.reform.sscs.ccd.domain;

public class HearingLocation {
    private String locationType;
    private LocationId locationId;

    public enum LocationId {
        court,
        cluster,
        region
    }
}

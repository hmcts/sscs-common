package uk.gov.hmcts.reform.sscs.ccd.domain;

public class UnavailabilityDow {
    private Dow dow;
    private DowUnavailabilityType dowUnavailabilityType;

    public enum Dow {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    }

    public enum DowUnavailabilityType {
        AM,
        PM,
        AllDay
    }

}

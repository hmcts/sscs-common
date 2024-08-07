package uk.gov.hmcts.reform.sscs.ccd.callback;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Optional;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.EventType;
import uk.gov.hmcts.reform.sscs.ccd.exception.RequiredFieldMissingException;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Callback<T extends CaseData> {

    @JsonProperty("event_id")
    private EventType event;

    private CaseDetails<T> caseDetails;
    private Optional<CaseDetails<T>> caseDetailsBefore = Optional.empty();
    private String pageId = null;

    @JsonProperty("ignore_warning")
    private boolean ignoreWarnings;

    private Callback() {
        // noop -- for deserializer
    }

    public Callback(CaseDetails<T> caseDetails, Optional<CaseDetails<T>> caseDetailsBefore, EventType event, boolean ignoreWarnings) {
        requireNonNull(caseDetails);
        requireNonNull(caseDetailsBefore);
        requireNonNull(event);

        this.caseDetails = caseDetails;
        this.caseDetailsBefore = caseDetailsBefore;
        this.event = event;
        this.ignoreWarnings = ignoreWarnings;
    }

    public EventType getEvent() {
        return event;
    }

    public CaseDetails<T> getCaseDetails() {

        if (caseDetails == null) {
            throw new RequiredFieldMissingException("caseDetails field is required");
        }

        return caseDetails;
    }

    public Optional<CaseDetails<T>> getCaseDetailsBefore() {
        return caseDetailsBefore;
    }

    public boolean isIgnoreWarnings() {
        return ignoreWarnings;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}

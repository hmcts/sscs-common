package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostHearing {
    @JsonProperty("postHearingRequestType")
    private PostHearingRequestType requestType;
    @JsonProperty("postHearingReviewType")
    private PostHearingReviewType reviewType;
    @Getter(AccessLevel.NONE)
    @JsonProperty("setAside")
    private SetAside setAside;
    @Getter(AccessLevel.NONE)
    @JsonProperty("correction")
    private Correction correction;
    @Getter(AccessLevel.NONE)
    @JsonProperty("statementOfReasons")
    private StatementOfReasons statementOfReasons;
    @Getter(AccessLevel.NONE)
    @JsonProperty("permissionToAppeal")
    private PermissionToAppeal permissionToAppeal;
    @Getter(AccessLevel.NONE)
    @JsonProperty("libertyToApply")
    private LibertyToApply libertyToApply;
    @Getter(AccessLevel.NONE)
    @JsonProperty("sendToFirstTier")
    private SendToFirstTier sendToFirstTier;

    @JsonIgnore
    public SetAside getSetAside() {
        if (isNull(setAside)) {
            setAside = new SetAside();
        }
        return setAside;
    }

    @JsonIgnore
    public Correction getCorrection() {
        if (isNull(correction)) {
            correction = new Correction();
        }
        return correction;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public StatementOfReasons getStatementOfReasons() {
        if (isNull(statementOfReasons)) {
            statementOfReasons = new StatementOfReasons();
        }
        return statementOfReasons;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public PermissionToAppeal getPermissionToAppeal() {
        if (isNull(permissionToAppeal)) {
            permissionToAppeal = new PermissionToAppeal();
        }
        return permissionToAppeal;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public LibertyToApply getLibertyToApply() {
        if (isNull(libertyToApply)) {
            libertyToApply = new LibertyToApply();
        }
        return libertyToApply;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SendToFirstTier getSendToFirstTier() {
        if (isNull(sendToFirstTier)) {
            sendToFirstTier = new SendToFirstTier();
        }
        return sendToFirstTier;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public RequestFormat getRequestFormat() {
        switch (requestType) {
            case SET_ASIDE:
                return getSetAside().getRequestFormat();
            case CORRECTION:
                return getCorrection().getRequestFormat();
            case STATEMENT_OF_REASONS:
                return getStatementOfReasons().getRequestFormat();
            case LIBERTY_TO_APPLY:
                return getLibertyToApply().getRequestFormat();
            case PERMISSION_TO_APPEAL:
                return getPermissionToAppeal().getRequestFormat();
            default:
                break;
        }
        return null;
    }
}

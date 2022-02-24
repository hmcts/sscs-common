package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingSubtype {

    private YesNo wantsHearingTypeTelephone;
    private String hearingTelephoneNumber;
    private YesNo wantsHearingTypeVideo;
    private String hearingVideoEmail;
    private YesNo wantsHearingTypeFaceToFace;

    @JsonCreator
    public HearingSubtype(@JsonProperty("wantsHearingTypeTelephone") YesNo wantsHearingTypeTelephone,
                          @JsonProperty("hearingTelephoneNumber") String hearingTelephoneNumber,
                          @JsonProperty("wantsHearingTypeVideo") YesNo wantsHearingTypeVideo,
                          @JsonProperty("hearingVideoEmail") String hearingVideoEmail,
                          @JsonProperty("wantsHearingTypeFaceToFace") YesNo wantsHearingTypeFaceToFace) {
        this.wantsHearingTypeTelephone = wantsHearingTypeTelephone;
        this.hearingTelephoneNumber = hearingTelephoneNumber;
        this.wantsHearingTypeVideo = wantsHearingTypeVideo;
        this.hearingVideoEmail = hearingVideoEmail;
        this.wantsHearingTypeFaceToFace = wantsHearingTypeFaceToFace;
    }

    @JsonIgnore
    public Boolean isWantsHearingTypeTelephone() {
        return isYes(wantsHearingTypeTelephone);
    }

    @JsonIgnore
    public Boolean isWantsHearingTypeVideo() {
        return isYes(wantsHearingTypeVideo);
    }

    @JsonIgnore
    public Boolean isWantsHearingTypeFaceToFace() {
        return isYes(wantsHearingTypeFaceToFace);
    }
}

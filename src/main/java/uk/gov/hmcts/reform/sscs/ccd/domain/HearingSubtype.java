package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingSubtype {

    private String wantsHearingTypeTelephone;
    private String hearingTelephoneNumber;
    private String wantsHearingTypeVideo;
    private String hearingVideoEmail;
    private String wantsHearingTypeFaceToFace;

    @JsonCreator
    public HearingSubtype(@JsonProperty("wantsHearingTypeTelephone") String wantsHearingTypeTelephone,
                          @JsonProperty("hearingTelephoneNumber") String hearingTelephoneNumber,
                          @JsonProperty("wantsHearingTypeVideo") String wantsHearingTypeVideo,
                          @JsonProperty("hearingVideoEmail") String hearingVideoEmail,
                          @JsonProperty("wantsHearingTypeFaceToFace") String wantsHearingTypeFaceToFace) {
        this.wantsHearingTypeTelephone = wantsHearingTypeTelephone;
        this.hearingTelephoneNumber = hearingTelephoneNumber;
        this.wantsHearingTypeVideo = wantsHearingTypeVideo;
        this.hearingVideoEmail = hearingVideoEmail;
        this.wantsHearingTypeFaceToFace = wantsHearingTypeFaceToFace;
    }

    @JsonIgnore
    public Boolean isWantsHearingTypeTelephone() {
        return StringUtils.isNotBlank(wantsHearingTypeTelephone) && wantsHearingTypeTelephone.toLowerCase().equals("yes");
    }

    @JsonIgnore
    public Boolean isWantsHearingTypeVideo() {
        return StringUtils.isNotBlank(wantsHearingTypeVideo) && wantsHearingTypeVideo.toLowerCase().equals("yes");
    }

    @JsonIgnore
    public Boolean isWantsHearingTypeFaceToFace() {
        return StringUtils.isNotBlank(wantsHearingTypeFaceToFace) && wantsHearingTypeFaceToFace.toLowerCase().equals("yes");
    }
}

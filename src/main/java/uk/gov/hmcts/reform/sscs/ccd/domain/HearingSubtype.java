package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HearingSubtype {

    @CCD(label = "Wants hearing type telephone", typeOverride = FieldType.YesOrNo)
    private String wantsHearingTypeTelephone;
    @CCD(label = "Hearing telephone number", showCondition = "wantsHearingTypeTelephone=\"Yes\"")
    private String hearingTelephoneNumber;
    @CCD(label = "Wants hearing type video", typeOverride = FieldType.YesOrNo)
    private String wantsHearingTypeVideo;
    @CCD(label = "Hearing video email address", showCondition = "wantsHearingTypeVideo=\"Yes\"")
    private String hearingVideoEmail;
    @CCD(label = "Wants hearing type face to face", typeOverride = FieldType.YesOrNo)
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

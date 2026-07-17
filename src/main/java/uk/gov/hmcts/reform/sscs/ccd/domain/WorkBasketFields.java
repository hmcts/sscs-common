package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;
import uk.gov.hmcts.reform.sscs.ccd.access.SscsCitizenCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.SuperuserCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.ClerkCudSuperuserCudSystemupdateCrudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.DwpresponsewriterHmrcresponsewriterCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.IbcaresponsewriterCudAccess;
import uk.gov.hmcts.reform.sscs.ccd.access.JudgeCudAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class WorkBasketFields {
    @CCD(label = "Hearing Date", access = {SscsCitizenCrudAccess.class, SuperuserCudSystemupdateCrudAccess.class})
    @JsonProperty("workBasketHearingDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate hearingDate;

    @CCD(
            label = "Hearing Date Issued",
            access = {ClerkCudSuperuserCudSystemupdateCrudAccess.class, DwpresponsewriterHmrcresponsewriterCudAccess.class, IbcaresponsewriterCudAccess.class}
    )
    @JsonProperty("workBasketHearingDateIssued")
    private String hearingDateIssued;

    @CCD(
            label = "Hearing Venue",
            typeOverride = FieldType.FixedList,
            typeParameterOverride = "FL_hearingEpimsId",
            access = {SscsCitizenCrudAccess.class, ClerkCudSuperuserCudSystemupdateCrudAccess.class, JudgeCudAccess.class}
    )
    @JsonProperty("workBasketHearingEpimsId")
    private String hearingEpimsId;
}

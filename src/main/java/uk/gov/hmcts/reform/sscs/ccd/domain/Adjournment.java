package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.ccd.validation.documentlink.DocumentLinkMustBePdf;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Adjournment {
    @JsonProperty("adjournCaseGenerateNotice")
    private YesNo generateNotice;
    @JsonProperty("adjournCaseTypeOfHearing")
    private AdjournCaseTypeOfHearing typeOfHearing;
    @JsonProperty("adjournCaseCanCaseBeListedRightAway")
    private YesNo canCaseBeListedRightAway;
    @JsonProperty("adjournCaseAreDirectionsBeingMadeToParties")
    private YesNo areDirectionsBeingMadeToParties;
    @JsonProperty("adjournCaseDirectionsDueDateDaysOffset")
    private AdjournCaseDaysOffset directionsDueDateDaysOffset;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("adjournCaseDirectionsDueDate")
    private LocalDate directionsDueDate;
    @JsonProperty("adjournCaseTypeOfNextHearing")
    private AdjournCaseTypeOfHearing typeOfNextHearing;
    @JsonProperty("adjournCaseNextHearingVenue")
    private AdjournCaseNextHearingVenue nextHearingVenue;
    @JsonProperty("adjournCaseNextHearingVenueSelected")
    private DynamicList nextHearingVenueSelected;
    @JsonProperty("adjournCasePanelMembersExcluded")
    private AdjournCasePanelMembersExcluded panelMembersExcluded;
    @JsonProperty("adjournCaseDisabilityQualifiedPanelMemberName")
    private String disabilityQualifiedPanelMemberName;
    @JsonProperty("adjournCaseMedicallyQualifiedPanelMemberName")
    private String medicallyQualifiedPanelMemberName;
    @JsonProperty("adjournCaseOtherPanelMemberName")
    private String otherPanelMemberName;
    @JsonProperty("adjournCasePanelMember1")
    private JudicialUserBase panelMember1;
    @JsonProperty("adjournCasePanelMember2")
    private JudicialUserBase panelMember2;
    @JsonProperty("adjournCasePanelMember3")
    private JudicialUserBase panelMember3;
    @JsonProperty("adjournCaseSignedInUser")
    private JudicialUserBase signedInUser;
    @JsonProperty("adjournCaseNextHearingListingDurationType")
    private AdjournCaseNextHearingDurationType nextHearingListingDurationType;
    @JsonProperty("adjournCaseNextHearingListingDuration")
    private Integer nextHearingListingDuration;
    @JsonProperty("adjournCaseNextHearingListingDurationUnits")
    private AdjournCaseNextHearingDurationUnits nextHearingListingDurationUnits;
    @JsonProperty("adjournCaseInterpreterRequired")
    private YesNo interpreterRequired;
    @JsonProperty("adjournCaseInterpreterLanguageList")
    private DynamicList interpreterLanguage;
    @JsonProperty("adjournCaseNextHearingDateType")
    private AdjournCaseNextHearingDateType nextHearingDateType;
    @JsonProperty("adjournCaseNextHearingDateOrPeriod")
    private AdjournCaseNextHearingDateOrPeriod nextHearingDateOrPeriod;
    @JsonProperty("adjournCaseNextHearingDateOrTime")
    private String nextHearingDateOrTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("adjournCaseNextHearingFirstAvailableDateAfterDate")
    private LocalDate nextHearingFirstAvailableDateAfterDate;
    @JsonProperty("adjournCaseNextHearingFirstAvailableDateAfterPeriod")
    private AdjournCaseNextHearingPeriod nextHearingFirstAvailableDateAfterPeriod;
    @JsonProperty("adjournCaseTime")
    private AdjournCaseTime time;
    @JsonProperty("adjournCaseReasons")
    private List<CollectionItem<String>> reasons;
    @JsonProperty("adjournCaseAdditionalDirections")
    private List<CollectionItem<String>> additionalDirections;

    @DocumentLinkMustBePdf(message = "You need to upload PDF documents only")
    @JsonProperty("adjournCasePreviewDocument")
    private DocumentLink previewDocument;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("adjournCaseGeneratedDate")
    private LocalDate generatedDate;
    @JsonProperty("adjournmentInProgress")
    private YesNo adjournmentInProgress;

    @SuppressWarnings("unused")
    @JsonIgnore
    public List<JudicialUserBase> getPanelMembers() {
        List<JudicialUserBase> panelMembers = Arrays.asList(this.panelMember1,
            this.panelMember2,
            this.panelMember3);

        return panelMembers.stream().filter(Objects::nonNull)
            .filter(panelMember -> nonNull(panelMember.getIdamId()) || nonNull(panelMember.getPersonalCode()))
            .collect(Collectors.toList());
    }

}

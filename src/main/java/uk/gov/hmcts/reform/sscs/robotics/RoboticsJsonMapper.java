package uk.gov.hmcts.reform.sscs.robotics;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Benefit.*;
import static uk.gov.hmcts.reform.sscs.ccd.domain.YesNo.isYes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.model.dwp.OfficeMapping;
import uk.gov.hmcts.reform.sscs.service.AirLookupService;
import uk.gov.hmcts.reform.sscs.service.DwpAddressLookupService;

@Component
@Slf4j
public class RoboticsJsonMapper {

    private static final String YES = "Yes";
    private static final String NO = "No";
    private static final String ESA_CASE_CODE = "051DD";
    private static final String PIP_CASE_CODE = "002DD";

    private final DwpAddressLookupService dwpAddressLookupService;
    private final AirLookupService airLookupService;

    @Autowired
    public RoboticsJsonMapper(DwpAddressLookupService dwpAddressLookupService,
                              AirLookupService airLookupService) {
        this.dwpAddressLookupService = dwpAddressLookupService;
        this.airLookupService = airLookupService;
    }

    private static String getCaseCode(SscsCaseData sscsCaseData) {

        if (isNotEmpty(sscsCaseData.getCaseCode())) {
            return sscsCaseData.getCaseCode();
        }

        // Leave this in for now, whilst we have legacy cases where the case code is not set.
        // This will be an issue for cases where the caseworker tries to regenerate the robotics json. Can remove after a few weeks I suspect.
        Optional<Benefit> benefitOptional = findBenefitByShortName(sscsCaseData.getAppeal().getBenefitType().getCode());

        if (benefitOptional.filter(b -> ESA == b).isPresent()) {
            return ESA_CASE_CODE;
        } else {
            return PIP_CASE_CODE;
        }
    }

    private static JSONObject buildDetails(Name name, Address address, Contact contact) {
        JSONObject json = new JSONObject();

        buildName(json, name.getTitle(), name.getFirstName(), name.getLastName());

        return buildContactDetails(json, address, contact);
    }

    private static JSONObject buildAppointeeDetails(Appointee appointee, boolean sameAddressAsAppellant) {
        JSONObject json = new JSONObject();

        json.put("title", appointee.getName().getTitle());
        json.put("firstName", appointee.getName().getFirstName());
        json.put("lastName", appointee.getName().getLastName());

        json.put("sameAddressAsAppellant", sameAddressAsAppellant ? YES : NO);

        return buildContactDetails(json, appointee.getAddress(), appointee.getContact());
    }

    private static JSONObject buildJointPartyDetails(Name name, Address address, boolean sameAddressAsAppellant,
                                                     String dob, String nino) {
        JSONObject json = new JSONObject();

        buildName(json, name.getTitle(), name.getFirstName(), name.getLastName());

        json.put("sameAddressAsAppellant", sameAddressAsAppellant ? YES : NO);
        json.put("dob", dob);
        json.put("nino", nino);

        return buildContactDetails(json, address, null);
    }

    private static JSONObject buildRepresentativeDetails(Representative rep) {
        JSONObject json = new JSONObject();

        String title = isNotEmpty(rep.getName().getTitle()) ? rep.getName().getTitle() : "s/m";
        String firstName = isNotEmpty(rep.getName().getFirstName()) ? rep.getName().getFirstName() : ".";
        String lastName = isNotEmpty(rep.getName().getLastName()) ? rep.getName().getLastName() : ".";
        buildName(json, title, firstName, lastName);

        if (rep.getOrganisation() != null) {
            json.put("organisation", rep.getOrganisation());
        }

        return buildContactDetails(json, rep.getAddress(), rep.getContact());
    }

    @SuppressWarnings("unchecked")
    private static JSONArray buildOtherParties(JSONObject obj, List<CcdValue<OtherParty>> otherParties) {

        JSONArray otherPartyArray = new JSONArray();
        for (CcdValue<OtherParty> otherParty : otherParties) {
            if (otherParty != null) {
                otherPartyArray.add(buildOtherPartyDetails(obj, otherParty.getValue()));
            }
        }

        return otherPartyArray;
    }

    private static JSONObject buildOtherPartyDetails(JSONObject obj, OtherParty otherParty) {
        JSONObject json = new JSONObject();

        Name name = YES.equalsIgnoreCase(otherParty.getIsAppointee()) ? otherParty.getAppointee().getName() : otherParty.getName();
        Address address = YES.equalsIgnoreCase(otherParty.getIsAppointee()) ? otherParty.getAppointee().getAddress() : otherParty.getAddress();
        Contact contact = YES.equalsIgnoreCase(otherParty.getIsAppointee()) ? otherParty.getAppointee().getContact() : otherParty.getContact();

        json.put("otherParty", buildDetails(name, address, contact));

        if (otherParty.getRep() != null
                && otherParty.getRep().getHasRepresentative() != null
                && otherParty.getRep().getHasRepresentative().equals(YES)) {
            json.put("otherPartyRepresentative", buildRepresentativeDetails(otherParty.getRep()));
        }

        if (otherParty.getHearingOptions() != null) {
            JSONObject hearingArrangements = buildHearingOptions(otherParty.getHearingOptions());
            if (hearingArrangements.length() > 0) {
                json.put("hearingArrangements", buildExcludedDates(otherParty.getHearingOptions(), hearingArrangements));
            }

            if (Boolean.TRUE.equals(otherParty.getHearingOptions().isWantsToAttendHearing())) {
                obj.put("hearingType", convertBooleanToPaperOral(otherParty.getHearingOptions().isWantsToAttendHearing()));
            }
        }

        return json;
    }

    private static void buildName(JSONObject json, String title, String firstName, String lastName) {
        json.put("title", title);
        json.put("firstName", firstName);
        json.put("lastName", lastName);
    }

    @SuppressWarnings("unchecked")
    private static JSONObject buildHearingOptions(HearingOptions hearingOptions) {
        JSONObject hearingArrangements = new JSONObject();

        if (hearingOptions.getArrangements() != null) {

            if (hearingOptions.getLanguageInterpreter() != null && hearingOptions.getLanguageInterpreter().equals(YES) && hearingOptions.getLanguages() != null) {
                hearingArrangements.put("languageInterpreter", hearingOptions.getLanguages().getValue().getLabel());
            }

            if (Boolean.TRUE.equals(hearingOptions.wantsSignLanguageInterpreter()) && hearingOptions.getSignLanguageType() != null) {
                hearingArrangements.put("signLanguageInterpreter", hearingOptions.getSignLanguageType());
            }

            hearingArrangements.put("hearingLoop", convertBooleanToYesNo(hearingOptions.wantsHearingLoop()));
            hearingArrangements.put("accessibleHearingRoom", convertBooleanToYesNo(hearingOptions.wantsAccessibleHearingRoom()));
        } else if (hearingOptions.getOther() != null || hearingOptions.getExcludeDates() != null) {

            hearingArrangements.put("hearingLoop", convertBooleanToYesNo(false));
            hearingArrangements.put("accessibleHearingRoom", convertBooleanToYesNo(false));
        }

        if (hearingOptions.getOther() != null) {
            hearingArrangements.put("other", hearingOptions.getOther());
        }

        buildExcludedDates(hearingOptions, hearingArrangements);

        return hearingArrangements;
    }

    public static JSONObject buildExcludedDates(HearingOptions hearingOptions, JSONObject hearingArrangements) {
        if (hearingOptions.getExcludeDates() != null
                && hearingOptions.getExcludeDates().size() > 0) {
            JSONArray datesCantAttendArray = new JSONArray();
            for (ExcludeDate a : hearingOptions.getExcludeDates()) {
                if (!isBlank(a.getValue().getStart())) {
                    // Assume start and end date are always the same
                    datesCantAttendArray.add(getLocalDate(a.getValue().getStart()));
                }
            }

            hearingArrangements.put("datesCantAttend", datesCantAttendArray);
        }

        return hearingArrangements;
    }

    private static JSONObject buildContactDetails(JSONObject json, Address address, Contact contact) {
        if (address != null) {
            json.put("addressLine1", address.getLine1());

            if (address.getLine2() != null) {
                json.put("addressLine2", address.getLine2());
            }

            json.put("townOrCity", address.getTown());
            json.put("county", address.getCounty());
            json.put("postCode", address.getPostcode());
        }
        if (contact != null) {
            json.put("phoneNumber", contact.getMobile());
            json.put("email", contact.getEmail());
        }

        return json;
    }

    private static JSONObject buildElementsDisputedLists(JSONObject json, SscsCaseData sscsCaseData) {

        JSONObject elementsDisputed = new JSONObject();

        if (sscsCaseData.getElementsDisputedGeneral() != null && sscsCaseData.getElementsDisputedGeneral().size() > 0) {
            elementsDisputed.put("general", buildElementIssueArray(sscsCaseData.getElementsDisputedGeneral()));
        }

        if (sscsCaseData.getElementsDisputedSanctions() != null && sscsCaseData.getElementsDisputedSanctions().size() > 0) {
            elementsDisputed.put("sanctions", buildElementIssueArray(sscsCaseData.getElementsDisputedSanctions()));
        }

        if (sscsCaseData.getElementsDisputedOverpayment() != null && sscsCaseData.getElementsDisputedOverpayment().size() > 0) {
            elementsDisputed.put("overpayment", buildElementIssueArray(sscsCaseData.getElementsDisputedOverpayment()));
        }

        if (sscsCaseData.getElementsDisputedHousing() != null && sscsCaseData.getElementsDisputedHousing().size() > 0) {
            elementsDisputed.put("housing", buildElementIssueArray(sscsCaseData.getElementsDisputedHousing()));
        }

        if (sscsCaseData.getElementsDisputedChildCare() != null && sscsCaseData.getElementsDisputedChildCare().size() > 0) {
            elementsDisputed.put("childCare", buildElementIssueArray(sscsCaseData.getElementsDisputedChildCare()));
        }

        if (sscsCaseData.getElementsDisputedCare() != null && sscsCaseData.getElementsDisputedCare().size() > 0) {
            elementsDisputed.put("care", buildElementIssueArray(sscsCaseData.getElementsDisputedCare()));
        }

        if (sscsCaseData.getElementsDisputedChildElement() != null && sscsCaseData.getElementsDisputedChildElement().size() > 0) {
            elementsDisputed.put("childElement", buildElementIssueArray(sscsCaseData.getElementsDisputedChildElement()));
        }

        if (sscsCaseData.getElementsDisputedChildDisabled() != null && sscsCaseData.getElementsDisputedChildDisabled().size() > 0) {
            elementsDisputed.put("childDisabled", buildElementIssueArray(sscsCaseData.getElementsDisputedChildDisabled()));
        }

        if (sscsCaseData.getElementsDisputedLimitedWork() != null && sscsCaseData.getElementsDisputedLimitedWork().size() > 0) {
            elementsDisputed.put("limitedCapabilityWork", buildElementIssueArray(sscsCaseData.getElementsDisputedLimitedWork()));
        }
        return elementsDisputed;
    }

    @SuppressWarnings("unchecked")
    private static JSONArray buildElementIssueArray(List<ElementDisputed> elementList) {
        JSONArray elementsDisputedArray = new JSONArray();

        elementList.forEach(e -> elementsDisputedArray.add(e.getValue().getIssueCode()));

        return elementsDisputedArray;
    }

    private static String convertBooleanToYesNo(Boolean value) {
        return Boolean.TRUE.equals(value) ? YES : NO;
    }

    private static String convertBooleanToPaperOral(Boolean value) {
        return Boolean.TRUE.equals(value) ? "Oral" : "Paper";
    }

    private static String getLocalDate(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return localDate.toString();
    }

    public JSONObject map(RoboticsWrapper roboticsWrapper) {

        SscsCaseData sscsCaseData = roboticsWrapper.getSscsCaseData();

        JSONObject obj = buildAppealDetails(new JSONObject(), sscsCaseData);

        obj.put("caseId", roboticsWrapper.getCcdCaseId());
        obj.put("evidencePresent", roboticsWrapper.getEvidencePresent());
        obj.put("caseCode", getCaseCode(sscsCaseData));

        if (!isAppointeeDetailsEmpty(sscsCaseData.getAppeal().getAppellant().getAppointee()) && YES.equalsIgnoreCase(sscsCaseData.getAppeal().getAppellant().getIsAppointee())) {
            boolean sameAddressAsAppointee = YES.equalsIgnoreCase(sscsCaseData.getAppeal().getAppellant().getIsAddressSameAsAppointee());
            obj.put("appointee", buildAppointeeDetails(sscsCaseData.getAppeal().getAppellant().getAppointee(), sameAddressAsAppointee));
        }

        obj.put("appellant", buildDetails(sscsCaseData.getAppeal().getAppellant().getName(),
                sscsCaseData.getAppeal().getAppellant().getAddress(),
                sscsCaseData.getAppeal().getAppellant().getContact()));

        if (sscsCaseData.getAppeal().getRep() != null
                && sscsCaseData.getAppeal().getRep().getHasRepresentative() != null
                && sscsCaseData.getAppeal().getRep().getHasRepresentative().equals(YES)) {
            obj.put("representative", buildRepresentativeDetails(sscsCaseData.getAppeal().getRep()));
        }

        if (sscsCaseData.getOtherParties() != null
                && sscsCaseData.getOtherParties().size() > 0) {
            JSONArray otherParties = buildOtherParties(obj, sscsCaseData.getOtherParties());

            obj.put("otherParties", otherParties);
        }

        if (sscsCaseData.getAppeal().getAppellant().getRole() != null
                && !AppellantRole.OTHER.getName().equalsIgnoreCase(sscsCaseData.getAppeal().getAppellant().getRole().getName())) {
            obj.put("appellantRole", sscsCaseData.getAppeal().getAppellant().getRole().getName());
        }

        if (sscsCaseData.getChildMaintenanceNumber() != null) {
            obj.put("childMaintenanceNumber", sscsCaseData.getChildMaintenanceNumber());
        }

        if (sscsCaseData.getAppeal().getHearingOptions() != null) {
            JSONObject hearingArrangements = buildHearingOptions(sscsCaseData.getAppeal().getHearingOptions());
            if (hearingArrangements.length() > 0) {
                obj.put("hearingArrangements", hearingArrangements);
            }
        }

        addRpcEmail(sscsCaseData.getRegionalProcessingCenter(), obj);

        String isReadyToList = NO;
        if (roboticsWrapper.getState().equals(State.READY_TO_LIST)) {
            isReadyToList = YES;
        }
        obj.put("isReadyToList", isReadyToList);

        String isDigital = NO;
        if (StringUtils.equals(roboticsWrapper.getSscsCaseData().getCreatedInGapsFrom(), "readyToList")) {
            isDigital = YES;
        }

        obj.put("isDigital", isDigital);

        setConfidentialFlag(roboticsWrapper, obj);

        obj.put("dwpResponseDate", sscsCaseData.getDwpResponseDate());

        Optional<OfficeMapping> officeMapping = buildOffice(sscsCaseData.getAppeal());

        String dwpIssuingOffice = EMPTY;
        String dwpPresentingOffice = EMPTY;

        if (officeMapping.isEmpty()) {
            log.error("could not find dwp officeAddress for benefitType {} and dwpIssuingOffice {} so could not set dwp offices in robotics",
                    sscsCaseData.getAppeal().getBenefitType().getCode(), sscsCaseData.getAppeal().getMrnDetails().getDwpIssuingOffice());
        }

        if (sscsCaseData.getDwpOriginatingOffice() != null && sscsCaseData.getDwpOriginatingOffice().getValue().getLabel() != null) {
            dwpIssuingOffice = buildOffice(sscsCaseData.getAppeal().getBenefitType().getCode(), sscsCaseData.getDwpOriginatingOffice().getValue().getLabel()).map(f -> f.getMapping().getGaps()).orElse(EMPTY);
        }
        if (dwpIssuingOffice.isEmpty() && officeMapping.isPresent()) {
            dwpIssuingOffice = officeMapping.get().getMapping().getGaps();
        }

        if (sscsCaseData.getDwpPresentingOffice() != null && sscsCaseData.getDwpPresentingOffice().getValue().getLabel() != null) {
            dwpPresentingOffice = buildOffice(sscsCaseData.getAppeal().getBenefitType().getCode(), sscsCaseData.getDwpPresentingOffice().getValue().getLabel()).map(f -> f.getMapping().getGaps()).orElse(EMPTY);
        }
        if (dwpPresentingOffice.isEmpty() && officeMapping.isPresent()) {
            dwpPresentingOffice = officeMapping.get().getMapping().getGaps();
        }

        String dwpIsOfficerAttending = sscsCaseData.getDwpIsOfficerAttending() != null ? sscsCaseData.getDwpIsOfficerAttending() : NO;
        String dwpUcb = sscsCaseData.getDwpUcb() != null ? sscsCaseData.getDwpUcb() : NO;

        obj.put("dwpIssuingOffice", dwpIssuingOffice);
        obj.put("dwpPresentingOffice", dwpPresentingOffice);
        obj.put("dwpIsOfficerAttending", dwpIsOfficerAttending);
        obj.put("dwpUcb", dwpUcb);

        JSONObject elementsDisputed = buildElementsDisputedLists(obj, sscsCaseData);
        if (elementsDisputed.length() > 0) {
            obj.put("elementsDisputed", elementsDisputed);
        }

        JointParty jointParty = sscsCaseData.getJointParty();
        if (isYes(jointParty.getHasJointParty())) {
            boolean sameAddressAsAppellant = isYes(jointParty.getJointPartyAddressSameAsAppellant());
            if (sameAddressAsAppellant) {
                obj.put("jointParty", buildJointPartyDetails(jointParty.getName(), sscsCaseData.getAppeal().getAppellant().getAddress(), true,
                        jointParty.getIdentity().getDob(), jointParty.getIdentity().getNino()));
            } else {
                obj.put("jointParty", buildJointPartyDetails(jointParty.getName(), jointParty.getAddress(), false,
                        jointParty.getIdentity().getDob(), jointParty.getIdentity().getNino()));
            }
        }
        if (sscsCaseData.getElementsDisputedIsDecisionDisputedByOthers() != null) {
            obj.put("ucDecisionDisputedByOthers", sscsCaseData.getElementsDisputedIsDecisionDisputedByOthers());
        }
        if (sscsCaseData.getElementsDisputedLinkedAppealRef() != null) {
            obj.put("linkedAppealRef", sscsCaseData.getElementsDisputedLinkedAppealRef());
        }
        if (sscsCaseData.getAppeal().getHearingSubtype() != null) {
            if (sscsCaseData.getAppeal().getHearingSubtype().getWantsHearingTypeTelephone() != null) {
                obj.put("wantsHearingTypeTelephone", sscsCaseData.getAppeal().getHearingSubtype().getWantsHearingTypeTelephone());
            }
            if (sscsCaseData.getAppeal().getHearingSubtype().getWantsHearingTypeVideo() != null) {
                obj.put("wantsHearingTypeVideo", sscsCaseData.getAppeal().getHearingSubtype().getWantsHearingTypeVideo());
            }
            if (sscsCaseData.getAppeal().getHearingSubtype().getWantsHearingTypeFaceToFace() != null) {
                obj.put("wantsHearingTypeFaceToFace", sscsCaseData.getAppeal().getHearingSubtype().getWantsHearingTypeFaceToFace());
            }
        }

        return obj;
    }

    private void setConfidentialFlag(RoboticsWrapper roboticsWrapper, JSONObject obj) {
        if ((roboticsWrapper.getSscsCaseData().getConfidentialityRequestOutcomeAppellant() != null && RequestOutcome.GRANTED.equals(roboticsWrapper.getSscsCaseData().getConfidentialityRequestOutcomeAppellant().getRequestOutcome()))
                || (roboticsWrapper.getSscsCaseData().getConfidentialityRequestOutcomeJointParty() != null && RequestOutcome.GRANTED.equals(roboticsWrapper.getSscsCaseData().getConfidentialityRequestOutcomeJointParty().getRequestOutcome()))
                || (isSscs2Or5Type(roboticsWrapper.getSscsCaseData().getBenefitType()) && YesNo.YES.equals(roboticsWrapper.getSscsCaseData().getIsConfidentialCase()))) {
            obj.put("isConfidential", YES);
        }
    }

    private boolean isSscs2Or5Type(Optional<Benefit> benefitType) {
        return benefitType.filter(benefit -> SscsType.SSCS2.equals(benefit.getSscsType())
                || SscsType.SSCS5.equals(benefit.getSscsType())).isPresent();
    }

    private void addRpcEmail(RegionalProcessingCenter rpc, JSONObject obj) {
        if (rpc != null && rpc.getEmail() != null) {
            obj.put("rpcEmail", rpc.getEmail());
        }
    }

    private JSONObject buildAppealDetails(JSONObject obj, SscsCaseData sscsCaseData) {
        Appeal appeal = sscsCaseData.getAppeal();
        obj.put("appellantNino", appeal.getAppellant().getIdentity().getNino());

        Optional<String> venueNameO = findVenueName(sscsCaseData);

        if (venueNameO.isPresent()) {
            obj.put("appellantPostCode", venueNameO.get());
        } else {
            log.info("could not find venueName to use for appellantPostCode");
        }

        if (sscsCaseData.getCaseCreated() != null) {
            obj.put("appealDate", sscsCaseData.getCaseCreated());
        } else {
            obj.put("appealDate", LocalDate.now().toString());
        }

        obj.put("receivedVia", appeal.getReceivedVia());

        if (appeal.getMrnDetails() != null) {
            if (appeal.getMrnDetails().getMrnDate() != null) {
                obj.put("mrnDate", appeal.getMrnDetails().getMrnDate());
            }
            if (appeal.getMrnDetails().getMrnLateReason() != null) {
                obj.put("mrnReasonForBeingLate", appeal.getMrnDetails().getMrnLateReason());
            }
        }

        Optional<OfficeMapping> officeMapping = buildOffice(appeal);

        if (officeMapping.isPresent()) {
            obj.put("pipNumber", officeMapping.get().getMapping().getGaps());
        } else {
            log.error("could not find dwp officeAddress for benefitType {} and dwpIssuingOffice {}",
                    appeal.getBenefitType().getCode(), appeal.getMrnDetails().getDwpIssuingOffice());
        }

        obj.put("hearingType", convertBooleanToPaperOral(appeal.getHearingOptions().isWantsToAttendHearing()));

        if (Boolean.TRUE.equals(appeal.getHearingOptions().isWantsToAttendHearing())) {
            obj.put("hearingRequestParty", appeal.getAppellant().getName().getFullName());
        }

        return obj;
    }

    public Optional<String> findVenueName(SscsCaseData sscsCaseData) {
        try {
            String postcodeToUse = YES.equalsIgnoreCase(sscsCaseData.getAppeal().getAppellant().getIsAppointee())
                    ? sscsCaseData.getAppeal().getAppellant().getAppointee().getAddress().getPostcode()
                    : sscsCaseData.getAppeal().getAppellant().getAddress().getPostcode();

            String venueString = airLookupService.lookupAirVenueNameByPostCode(postcodeToUse, sscsCaseData.getAppeal().getBenefitType());

            return Optional.of(venueString);
        } catch (Exception e) {
            log.error("Error trying to find venue name", e);
            return Optional.empty();
        }
    }

    private Optional<OfficeMapping> buildOffice(Appeal appeal) {
        return buildOffice(appeal.getBenefitType().getCode(), appeal.getMrnDetails().getDwpIssuingOffice());
    }

    private Optional<OfficeMapping> buildOffice(String benefitCode, String dwpIssuingOffice) {
        return dwpAddressLookupService.getDwpMappingByOffice(benefitCode, dwpIssuingOffice);
    }

    private boolean isAppointeeDetailsEmpty(Appointee appointee) {
        return appointee == null
                || (isAddressEmpty(appointee.getAddress())
                && isContactEmpty(appointee.getContact())
                && isIdentityEmpty(appointee.getIdentity())
                && isNameEmpty(appointee.getName()));
    }

    private boolean isAddressEmpty(Address address) {
        return address == null
                || (address.getLine1() == null
                && address.getLine2() == null
                && address.getTown() == null
                && address.getCounty() == null
                && address.getPostcode() == null);
    }

    private boolean isContactEmpty(Contact contact) {
        return contact == null
                || (contact.getEmail() == null
                && contact.getPhone() == null
                && contact.getMobile() == null);
    }

    private boolean isIdentityEmpty(Identity identity) {
        return identity == null
                || (identity.getDob() == null
                && identity.getNino() == null);
    }

    private boolean isNameEmpty(Name name) {
        return name == null
                || (name.getFirstName() == null
                && name.getLastName() == null
                && name.getTitle() == null);
    }
}

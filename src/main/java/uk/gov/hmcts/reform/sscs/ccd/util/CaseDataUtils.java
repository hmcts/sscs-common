package uk.gov.hmcts.reform.sscs.ccd.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.*;
import uk.gov.hmcts.reform.sscs.ccd.service.SscsCcdConvertService;

public final class CaseDataUtils {

    public static final String YES = "Yes";

    private CaseDataUtils() {
    }

    public static SscsCaseData buildCaseData() {
        return buildCaseData("Test");
    }

    public static SscsCaseData buildCaseData(final String surname) {
        return buildCaseData(surname, "PIP", "DWP PIP (1)");
    }
    
    public static SscsCaseData buildCaseData(final String surname, String benefitTypeCode, String issuingOffice) {
        Name name = Name.builder()
                .title("Mr")
                .firstName("User")
                .lastName(surname)
                .build();
        Name repName = Name.builder()
                .title("Mrs")
                .firstName("Wendy")
                .lastName("Giles")
                .build();
        Address address = Address.builder()
                .line1("123 Hairy Lane")
                .line2("Off Hairy Park")
                .town("Hairyfield")
                .county("Kent")
                .postcode("TN32 6PL")
                .build();
        Contact contact = Contact.builder()
                .email("mail@email.com")
                .phone("01234567890")
                .mobile("01234567890")
                .build();
        Identity identity = Identity.builder()
                .dob("1904-03-10")
                .nino("AB 22 55 66 B")
                .build();

        Name appointeeName = Name.builder()
                .title("Mrs")
                .firstName("April")
                .lastName("Appointer")
                .build();

        Address appointeeAddress = Address.builder()
                .line1("42 Appointed Mews")
                .line2("Apford")
                .town("Apton")
                .county("Appshire")
                .postcode("AP12 4PA")
                .build();

        Contact appointeeContact = Contact.builder()
                .email("appointee@hmcts.net")
                .phone("0207 946 0555")
                .mobile("07700 900555")
                .build();

        Identity appointeeIdentity = Identity.builder()
                .dob("1905-05-05")
                .nino("ZZ 11 22 33 B")
                .build();

        Appointee appointee = Appointee.builder()
                .name(appointeeName)
                .address(appointeeAddress)
                .contact(appointeeContact)
                .identity(appointeeIdentity)
                .build();

        Appellant appellant = Appellant.builder()
                .name(name)
                .address(address)
                .contact(contact)
                .identity(identity)
                .appointee(appointee)
                .build();

        BenefitType benefitType = BenefitType.builder()
                .code(benefitTypeCode)
                .build();

        DateRange dateRange1 = DateRange.builder()
                .start("2018-06-30")
                .end("2018-06-30")
                .build();
        DateRange dateRange2 = DateRange.builder()
                .start("2018-07-30")
                .end("2018-07-30")
                .build();
        DateRange dateRange3 = DateRange.builder()
                .start("2018-08-30")
                .end("2018-08-30")
                .build();
        ExcludeDate excludeDate1 = ExcludeDate.builder()
                .value(dateRange1)
                .build();
        ExcludeDate excludeDate2 = ExcludeDate.builder()
                .value(dateRange2)
                .build();
        ExcludeDate excludeDate3 = ExcludeDate.builder()
                .value(dateRange3)
                .build();

        List<ExcludeDate> excludeDates = new ArrayList<>();
        excludeDates.add(excludeDate1);
        excludeDates.add(excludeDate2);
        excludeDates.add(excludeDate3);

        HearingOptions hearingOptions = HearingOptions.builder()
                .wantsToAttend(YES)
                .languageInterpreter(YES)
                .languages("Spanish")
                .signLanguageType("A sign language")
                .arrangements(Arrays.asList("hearingLoop", "signLanguageInterpreter"))
                .other("Yes, this...")
                .excludeDates(excludeDates)
                .build();

        HearingSubtype hearingSubtype = HearingSubtype.builder()
                .wantsHearingTypeTelephone("Yes")
                .hearingTelephoneNumber("07988999000")
                .build();

        MrnDetails mrnDetails = MrnDetails.builder()
                .mrnDate("2018-06-29")
                .dwpIssuingOffice(issuingOffice)
                .mrnLateReason("Lost my paperwork")
                .build();

        Representative representative = Representative.builder()
                .hasRepresentative(YES)
                .name(repName)
                .address(address)
                .contact(contact)
                .organisation("HP Ltd")
                .build();

        final Appeal appeal = Appeal.builder()
                .appellant(appellant)
                .benefitType(benefitType)
                .hearingOptions(hearingOptions)
                .hearingSubtype(hearingSubtype)
                .mrnDetails(mrnDetails)
                .rep(representative)
                .signer("Signer")
                .hearingType("oral")
                .receivedVia("Online")
                .build();

        Address venueAddress = Address.builder()
                .postcode("AB12 3ED")
                .build();
        Venue venue = Venue.builder()
                .name("Aberdeen")
                .address(venueAddress)
                .build();
        HearingDetails hearingDetails = HearingDetails.builder()
                .venue(venue)
                .hearingDate("2017-05-24")
                .time("10:45")
                .adjourned(YES)
                .build();
        Hearing hearings = Hearing.builder()
                .value(hearingDetails)
                .build();
        List<Hearing> hearingsList = new ArrayList<>();
        hearingsList.add(hearings);

        DocumentDetails doc = DocumentDetails.builder()
                .dateReceived("2017-05-24")
                .evidenceType("General")
                .build();
        Document documents = Document.builder()
                .value(doc)
                .build();
        List<Document> documentsList = new ArrayList<>();
        documentsList.add(documents);
        final Evidence evidence = Evidence.builder()
                .documents(documentsList)
                .build();

        DwpTimeExtensionDetails dwpTimeExtensionDetails = DwpTimeExtensionDetails.builder()
                .requested(YES)
                .granted(YES)
                .build();
        DwpTimeExtension dwpTimeExtension = DwpTimeExtension.builder()
                .value(dwpTimeExtensionDetails)
                .build();
        List<DwpTimeExtension> dwpTimeExtensionList = new ArrayList<>();
        dwpTimeExtensionList.add(dwpTimeExtension);

        EventDetails eventDetails = EventDetails.builder()
                .type("appealCreated")
                .description("Appeal Created")
                .date("2001-12-14T21:59:43.10-05:00")
                .build();
        Event events = Event.builder()
                .value(eventDetails)
                .build();

        Subscription appellantSubscription = Subscription.builder()
                .tya("app-appeal-number")
                .email("appellant@email.com")
                .mobile("")
                .subscribeEmail(YES)
                .subscribeSms(YES)
                .reason("")
                .lastLoggedIntoMya("2001-12-14T21:59:43.10-05:00")
                .build();
        Subscription appointeeSubscription = Subscription.builder()
                .tya("appointee-appeal-number")
                .email("appointee@hmcts.net")
                .mobile("07700 900555")
                .subscribeEmail(YES)
                .subscribeSms(YES)
                .reason("")
                .lastLoggedIntoMya("2001-12-14T21:59:43.10-05:00")
                .build();
        Subscription supporterSubscription = Subscription.builder()
                .tya("")
                .email("supporter@email.com")
                .mobile("")
                .subscribeEmail("")
                .subscribeSms("")
                .reason("")
                .lastLoggedIntoMya("2001-12-14T21:59:43.10-05:00")
                .build();
        Subscription representativeSubscription = Subscription.builder()
                .tya("rep-appeal-number")
                .email("representative@email.com")
                .mobile("07777777777")
                .subscribeEmail(YES)
                .subscribeSms(YES)
                .build();
        Subscriptions subscriptions = Subscriptions.builder()
                .appellantSubscription(appellantSubscription)
                .supporterSubscription(supporterSubscription)
                .representativeSubscription(representativeSubscription)
                .appointeeSubscription(appointeeSubscription)
                .build();

        RegionalProcessingCenter rpc = RegionalProcessingCenter.builder()
                .name("CARDIFF")
                .address1("HM Courts & Tribunals Service")
                .address2("Social Security & Child Support Appeals")
                .address3("Eastgate House")
                .address4("Newport Road")
                .city("CARDIFF")
                .postcode("CF24 0AB")
                .phoneNumber("0300 123 1142")
                .faxNumber("0870 739 4438")
                .email("Cardiff_SYA_Respon@justice.gov.uk")
                .build();

        return SscsCaseData.builder()
                .caseReference("SC068/17/00013")
                .caseCreated(LocalDate.now().toString())
                .appeal(appeal)
                .hearings(hearingsList)
                .evidence(evidence)
                .dwpTimeExtension(dwpTimeExtensionList)
                .events(Collections.singletonList(events))
                .subscriptions(subscriptions)
                .region("CARDIFF")
                .regionalProcessingCenter(rpc)
                .build();
    }

    public static SscsCaseData buildMinimalCaseData() {
        Name name = Name.builder()
                .title("Mr")
                .firstName("User")
                .lastName("Test")
                .build();
        Contact contact = Contact.builder()
                .email("mail@example.com")
                .phone("01234567890")
                .mobile("01234567890")
                .build();
        Identity identity = Identity.builder()
                .dob("1904-03-10")
                .nino("AB 22 55 66 B")
                .build();
        Appellant appellant = Appellant.builder()
                .name(name)
                .contact(contact)
                .identity(identity)
                .build();
        BenefitType benefitType = BenefitType.builder()
                .code("1325")
                .build();

        DateRange dateRange = DateRange.builder()
                .start("2018-06-30")
                .end("2018-06-30")
                .build();
        ExcludeDate excludeDate = ExcludeDate.builder()
                .value(dateRange)
                .build();

        HearingOptions hearingOptions = HearingOptions.builder()
                .wantsToAttend(YES)
                .arrangements(Arrays.asList("disabledAccess", "hearingLoop"))
                .excludeDates(Collections.singletonList(excludeDate))
                .other("No")
                .build();

        MrnDetails mrnDetails = MrnDetails.builder()
                .mrnDate("2018-06-30")
                .dwpIssuingOffice("1")
                .build();

        Representative representative = Representative.builder()
                .hasRepresentative(YES)
                .build();

        final Appeal appeal = Appeal.builder()
                .appellant(appellant)
                .benefitType(benefitType)
                .hearingOptions(hearingOptions)
                .mrnDetails(mrnDetails)
                .rep(representative)
                .signer("Signer")
                .receivedVia("Online")
                .build();

        EventDetails event = EventDetails.builder()
                .type("appealCreated")
                .description("Appeal Created")
                .date("2001-12-14T21:59:43.10")
                .build();
        Event events = Event.builder()
                .value(event)
                .build();

        return SscsCaseData.builder()
                .appeal(appeal)
                .events(Collections.singletonList(events))
                .build();
    }

    public static SscsCaseDetails convertCaseDetailsToSscsCaseDetails(final CaseDetails caseDetails) {
        return new SscsCcdConvertService().getCaseDetails(caseDetails);
    }

    public static CaseDetails buildCaseDetails() {
        return CaseDetails.builder().id(1L).data(buildCaseDataMap(buildCaseData())).build();
    }

    public static CaseDetails buildCaseDetailsWithSurname(final String surname) {
        final SscsCaseData caseData = buildCaseData(surname).toBuilder().build();
        return CaseDetails.builder().data(buildCaseDataMap(caseData)).build();
    }

    public static Map<String, Object> buildCaseDataMap(final SscsCaseData caseData) {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            final String json = mapper.writeValueAsString(caseData);
            return mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<CaseDetails> buildCaseDetailsList() {
        return Arrays.asList(CaseDetails.builder().data(buildCaseDataMap(buildCaseData())).build());
    }
}

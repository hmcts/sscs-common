package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import static uk.gov.hmcts.reform.sscs.reference.data.mappings.CaseTypePanelMembers.*;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaseType {

    BBA3_001("BBA3-001","BBA3","001","SSCS1","UNIVERSAL CREDIT","Credyd Cynhwysol",List.of()),
    BBA3_002("BBA3-002","BBA3","002","SSCS1","PERSONAL INDEPENDENT PAYMENT (NEW CLAIM)","Taliad Annibyniaeth Personol",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT)),
    BBA3_003("BBA3-003","BBA3","003","SSCS1","PERSONAL INDEPENDENT PAYMENT (REASSESSMENT CASE)","Taliad Annibyniaeth Personol",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT)),
    BBA3_011("BBA3-011","BBA3","011","null","CHILD TRUST FUND","null",List.of()),
    BBA3_012("BBA3-012","BBA3","012","null","HEALTH IN PREGNANCY GRANT","null",List.of()),
    BBA3_013("BBA3-013","BBA3","013","SSCS1","ATTENDANCE ALLOWANCE","null",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT)),
    BBA3_014("BBA3-014","BBA3","014","null","DIFFUSE MESOTHELIOMA","null",List.of()),
    BBA3_016("BBA3-016","BBA3","016","SSCS5","CHILD BENEFIT / CHILD BENEFIT (LONE PARENT)","null",List.of()),
    BBA3_022("BBA3-022","BBA3","022","SSCS2","CHILD SUPPORT ASSESSMENTS","null",List.of()),
    BBA3_023("BBA3-023","BBA3","023","SSCS2","CHILD SUPPORT REFORMS","null",List.of()),
    BBA3_024("BBA3-024","BBA3","024","SSCS2","CHILD SUPPORT REFORM VARIATION REFERRALS","null",List.of()),
    BBA3_025("BBA3-025","BBA3","025","SSCS2","CHILD SUPPORT DEPARTURES","null",List.of()),
    BBA3_026("BBA3-026","BBA3","026","SSCS2","CHILD SUPPORT VARIATION REFERRAL","null",List.of()),
    BBA3_028("BBA3-028","BBA3","028","SSCS2","CHILD SUPPORT DEPARTURES REFERRAL","null",List.of()),
    BBA3_030("BBA3-030","BBA3","030","null","CREDITS - APPROVED TRAINING","null",List.of()),
    BBA3_031("BBA3-031","BBA3","031","SSCS3","COMPENSATION RECOVERY UNIT","null",List.of()),
    BBA3_032("BBA3-032","BBA3","032","null","CREDITS - JURY SERVICE","null",List.of()),
    BBA3_033("BBA3-033","BBA3","033","null","CREDITS - STATUTORY SICK PAY","null",List.of()),
    BBA3_034("BBA3-034","BBA3","034","null","COEG","null",List.of()),
    BBA3_035("BBA3-035","BBA3","035","null","CREDITS - STATUTORY MATERNITY PAY","null",List.of()),
    BBA3_036("BBA3-036","BBA3","036","SSCS4","ROAD TRAFFIC (NHS CHARGE)","null",List.of()),
    BBA3_037("BBA3-037","BBA3","037","SSCS1","DISABILITY LIVING ALLOWANCE","Lwfans Byw i�r Anabl",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT)),
    BBA3_040("BBA3-040","BBA3","040","null","DISABILITY WORKING ALLOWANCE","null",List.of()),
    BBA3_043("BBA3-043","BBA3","043","null","DISABLED PERSONS TAX CREDIT","null",List.of()),
    BBA3_045("BBA3-045","BBA3","045","SSCS1","PENSION CREDITS","Credydau Pensiwn",List.of(JUDGE)),
    BBA3_049("BBA3-049","BBA3","049","null","FAMILY CREDIT","null",List.of()),
    BBA3_050("BBA3-050","BBA3","050","null","HRP","null",List.of()),
    BBA3_051("BBA3-051","BBA3","051","SSCS1","EMPLOYMENT SUPPORT ALLOWANCE","Lwfans Cyflogaeth a Chymorth",List.of()),
    BBA3_052("BBA3-052","BBA3","052","null","INCAPACITY BENEFIT","null",List.of()),
    BBA3_053("BBA3-053","BBA3","053","SSCS5","CHILD TAX CREDIT","null",List.of()),
    BBA3_054("BBA3-054","BBA3","054","SSCS5","WORKING TAX CREDIT","null",List.of()),
    BBA3_055("BBA3-055","BBA3","055","null","PENALTY PROCEEDINGS","null",List.of()),
    BBA3_056("BBA3-056","BBA3","056","SSCS5","CARERS CREDIT","null",List.of()),
    BBA3_057("BBA3-057","BBA3","057","SSCS5","TAX-FREE CHILDCARE SCHEME","null",List.of()),
    BBA3_058("BBA3-058","BBA3","058","SSCS5","30 HOURS' FREE CHILDCARE SCHEME","null",List.of()),
    BBA3_061("BBA3-061","BBA3","061","SSCS1","INCOME SUPPORT","Cymhorthdal Incwm",List.of(JUDGE)),
    BBA3_062("BBA3-062","BBA3","062","SSCS1","EMPLOYMENT SUPPORT ALLOWANCE REASSESSMENT","null",List.of()),
    BBA3_064("BBA3-064","BBA3","064","SSCS1","INDUSTRIAL DEATH BENEFIT","Budd Marwolaeth Ddiwydiannol",List.of(JUDGE, DOCTOR)),
    BBA3_067("BBA3-067","BBA3","067","SSCS�","INDUSTRIAL INJURIES DISABLEMENT BENEFIT","Budd-dal Anabledd Anafiadau Diwydiannol",List.of(JUDGE, DOCTOR)),
    BBA3_069("BBA3-069","BBA3","069","null","(HOUSING / COUNCIL TAX) BENEFIT COMBINED","null",List.of()),
    BBA3_070("BBA3-070","BBA3","070","SSCS1","CARER'S ALLOWANCE","Lwfans Gofalwr",List.of(JUDGE)),
    BBA3_073("BBA3-073","BBA3","073","SSCS1","JOB SEEKER'S ALLOWANCE","Lwfans Ceisio Gwaith",List.of(JUDGE)),
    BBA3_075("BBA3-075","BBA3","075","null","HOUSING BENEFIT","null",List.of()),
    BBA3_076("BBA3-076","BBA3","076","null","LOOKALIKES","null",List.of()),
    BBA3_077("BBA3-077","BBA3","077","null","COUNCIL TAX BENEFIT","null",List.of()),
    BBA3_079("BBA3-079","BBA3","079","SSCS1","MATERNITY BENEFIT/ALLOWANCES","Lwfans Mamolaeth",List.of(JUDGE)),
    BBA3_082("BBA3-082","BBA3","082","SSCS1","RETIREMENT PENSION","Pensiwn Ymddeol",List.of(JUDGE)),
    BBA3_085("BBA3-085","BBA3","085","null","SEVERE DISABLEMENT BENEFIT/ALLOWANCE","null",List.of()),
    BBA3_088("BBA3-088","BBA3","088","SSCS1","SOCIAL FUND - MATERNITY","Cronfa Gymdeithasol",List.of(JUDGE)),
    BBA3_089("BBA3-089","BBA3","089","SSCS1","SOCIAL FUND - FUNERAL","Cronfa Gymdeithasol",List.of(JUDGE)),
    BBA3_090("BBA3-090","BBA3","090","null","CHILDREN'S FUNERAL FUND FOR ENGLAND","null",List.of()),
    BBA3_091("BBA3-091","BBA3","091","SSCS1","VACCINE DAMAGE APPEALS","null",List.of(JUDGE, DOCTOR)),
    BBA3_094("BBA3-094","BBA3","094","SSCS1","BEREAVEMENT BENEFIT","Budd-dal Profedigaeth",List.of(JUDGE)),
    BBA3_095("BBA3-095","BBA3","095","SSCS1","BEREAVEMENT SUPPORT PAYMENT SCHEME","Cynllun Taliad Cymorth Profedigaeth",List.of(JUDGE)),
    BBA3_096("BBA3-096","BBA3","096","SSCS5","WORKING FAMILIES TAX CREDIT","null",List.of()),
    BBA3_099("BBA3-099","BBA3","099","null","OTHERS (EXTINCT/RARE BENEFITS)","null",List.of());

    private final String key;
    private final String serviceCode;
    private final String benefitCode;
    private final String TBD;
    private final String typeDescriptionEN;
    private final String typeDescriptionCY;
    private final List<CaseTypePanelMembers> panelMembers;

}

package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import static uk.gov.hmcts.reform.sscs.reference.data.mappings.CaseTypePanelMembers.JUDGE;
import static uk.gov.hmcts.reform.sscs.reference.data.mappings.CaseTypePanelMembers.DOCTOR;
import static uk.gov.hmcts.reform.sscs.reference.data.mappings.CaseTypePanelMembers.DISABILITY_EXPERT;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaseType {

    BBA3_002("BBA3-002","BBA3","caseType","SSCS1","PERSONAL INDEPENDENT PAYMENT (NEW CLAIM)","Taliad Annibyniaeth Personol",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT),"N/A"),
    BBA3_003("BBA3-003","BBA3","caseType","SSCS1","PERSONAL INDEPENDENT PAYMENT (REASSESSMENT CASE)","Taliad Annibyniaeth Personol",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT),"N/A"),
    BBA3_011("BBA3-011","BBA3","caseType","null","CHILD TRUST FUND","null",List.of(),"N/A"),
    BBA3_012("BBA3-012","BBA3","caseType","null","HEALTH IN PREGNANCY GRANT","null",List.of(),"N/A"),
    BBA3_013("BBA3-013","BBA3","caseType","SSCS1","ATTENDANCE ALLOWANCE","null",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT),"N/A"),
    BBA3_014("BBA3-014","BBA3","caseType","null","DIFFUSE MESOTHELIOMA","null",List.of(),"N/A"),
    BBA3_016("BBA3-016","BBA3","caseType","SSCS5","CHILD BENEFIT / CHILD BENEFIT (LONE PARENT)","null",List.of(),"N/A"),
    BBA3_022("BBA3-022","BBA3","caseType","SSCS2","CHILD SUPPORT ASSESSMENTS","null",List.of(),"N/A"),
    BBA3_023("BBA3-023","BBA3","caseType","SSCS2","CHILD SUPPORT REFORMS","null",List.of(),"N/A"),
    BBA3_024("BBA3-024","BBA3","caseType","SSCS2","CHILD SUPPORT REFORM VARIATION REFERRALS","null",List.of(),"N/A"),
    BBA3_025("BBA3-025","BBA3","caseType","SSCS2","CHILD SUPPORT DEPARTURES","null",List.of(),"N/A"),
    BBA3_026("BBA3-026","BBA3","caseType","SSCS2","CHILD SUPPORT VARIATION REFERRAL","null",List.of(),"N/A"),
    BBA3_028("BBA3-028","BBA3","caseType","SSCS2","CHILD SUPPORT DEPARTURES REFERRAL","null",List.of(),"N/A"),
    BBA3_030("BBA3-030","BBA3","caseType","null","CREDITS - APPROVED TRAINING","null",List.of(),"N/A"),
    BBA3_031("BBA3-031","BBA3","caseType","SSCS3","COMPENSATION RECOVERY UNIT","null",List.of(),"N/A"),
    BBA3_032("BBA3-032","BBA3","caseType","null","CREDITS - JURY SERVICE","null",List.of(),"N/A"),
    BBA3_033("BBA3-033","BBA3","caseType","null","CREDITS - STATUTORY SICK PAY","null",List.of(),"N/A"),
    BBA3_034("BBA3-034","BBA3","caseType","null","COEG","null",List.of(),"N/A"),
    BBA3_035("BBA3-035","BBA3","caseType","null","CREDITS - STATUTORY MATERNITY PAY","null",List.of(),"N/A"),
    BBA3_036("BBA3-036","BBA3","caseType","SSCS4","ROAD TRAFFIC (NHS CHARGE)","null",List.of(),"N/A"),
    BBA3_037("BBA3-037","BBA3","caseType","SSCS1","DISABILITY LIVING ALLOWANCE","Lwfans Byw i�r Anabl",List.of(JUDGE,DOCTOR,DISABILITY_EXPERT),"N/A"),
    BBA3_040("BBA3-040","BBA3","caseType","null","DISABILITY WORKING ALLOWANCE","null",List.of(),"N/A"),
    BBA3_043("BBA3-043","BBA3","caseType","null","DISABLED PERSONS TAX CREDIT","null",List.of(),"N/A"),
    BBA3_045("BBA3-045","BBA3","caseType","SSCS1","PENSION CREDITS","Credydau Pensiwn",List.of(JUDGE),"N/A"),
    BBA3_049("BBA3-049","BBA3","caseType","null","FAMILY CREDIT","null",List.of(),"N/A"),
    BBA3_050("BBA3-050","BBA3","caseType","null","HRP","null",List.of(),"N/A"),
    BBA3_051("BBA3-051","BBA3","caseType","SSCS1","EMPLOYMENT SUPPORT ALLOWANCE","Lwfans Cyflogaeth a Chymorth",List.of(),"N/A"),
    BBA3_052("BBA3-052","BBA3","caseType","null","INCAPACITY BENEFIT","null",List.of(),"N/A"),
    BBA3_053("BBA3-053","BBA3","caseType","SSCS5","CHILD TAX CREDIT","null",List.of(),"N/A"),
    BBA3_054("BBA3-054","BBA3","caseType","SSCS5","WORKING TAX CREDIT","null",List.of(),"N/A"),
    BBA3_055("BBA3-055","BBA3","caseType","null","PENALTY PROCEEDINGS","null",List.of(),"N/A"),
    BBA3_056("BBA3-056","BBA3","caseType","SSCS5","CARERS CREDIT","null",List.of(),"N/A"),
    BBA3_057("BBA3-057","BBA3","caseType","SSCS5","TAX-FREE CHILDCARE SCHEME","null",List.of(),"N/A"),
    BBA3_058("BBA3-058","BBA3","caseType","SSCS5","30 HOURS' FREE CHILDCARE SCHEME","null",List.of(),"N/A"),
    BBA3_061("BBA3-061","BBA3","caseType","SSCS1","INCOME SUPPORT","Cymhorthdal Incwm",List.of(JUDGE),"N/A"),
    BBA3_062("BBA3-062","BBA3","caseType","SSCS1","EMPLOYMENT SUPPORT ALLOWANCE REASSESSMENT","null",List.of(),"N/A"),
    BBA3_064("BBA3-064","BBA3","caseType","SSCS1","INDUSTRIAL DEATH BENEFIT","Budd Marwolaeth Ddiwydiannol",List.of(JUDGE, DOCTOR),"N/A"),
    BBA3_067("BBA3-067","BBA3","caseType","SSCS�","INDUSTRIAL INJURIES DISABLEMENT BENEFIT","Budd-dal Anabledd Anafiadau Diwydiannol",List.of(JUDGE, DOCTOR),"N/A"),
    BBA3_069("BBA3-069","BBA3","caseType","null","(HOUSING / COUNCIL TAX) BENEFIT COMBINED","null",List.of(),"N/A"),
    BBA3_070("BBA3-070","BBA3","caseType","SSCS1","CARER'S ALLOWANCE","Lwfans Gofalwr",List.of(JUDGE),"N/A"),
    BBA3_073("BBA3-073","BBA3","caseType","SSCS1","JOB SEEKER'S ALLOWANCE","Lwfans Ceisio Gwaith",List.of(JUDGE),"N/A"),
    BBA3_075("BBA3-075","BBA3","caseType","null","HOUSING BENEFIT","null",List.of(),"N/A"),
    BBA3_076("BBA3-076","BBA3","caseType","null","LOOKALIKES","null",List.of(),"N/A"),
    BBA3_077("BBA3-077","BBA3","caseType","null","COUNCIL TAX BENEFIT","null",List.of(),"N/A"),
    BBA3_079("BBA3-079","BBA3","caseType","SSCS1","MATERNITY BENEFIT/ALLOWANCES","Lwfans Mamolaeth",List.of(JUDGE),"N/A"),
    BBA3082("BBA3082","BBA3","caseType","SSCS1","RETIREMENT PENSION","Pensiwn Ymddeol",List.of(JUDGE),"N/A"),
    BBA3_085("BBA3-085","BBA3","caseType","null","SEVERE DISABLEMENT BENEFIT/ALLOWANCE","null",List.of(),"N/A"),
    BBA3_088("BBA3-088","BBA3","caseType","SSCS1","SOCIAL FUND - MATERNITY","Cronfa Gymdeithasol",List.of(JUDGE),"N/A"),
    BBA3_089("BBA3-089","BBA3","caseType","SSCS1","SOCIAL FUND - FUNERAL","Cronfa Gymdeithasol",List.of(JUDGE),"N/A"),
    BBA3_090("BBA3-090","BBA3","caseType","null","CHILDREN'S FUNERAL FUND FOR ENGLAND","null",List.of(),"N/A"),
    BBA3_091("BBA3-091","BBA3","caseType","SSCS1","VACCINE DAMAGE APPEALS","null",List.of(JUDGE, DOCTOR),"N/A"),
    BBA3_094("BBA3-094","BBA3","caseType","SSCS1","BEREAVEMENT BENEFIT","Budd-dal Profedigaeth",List.of(JUDGE),"N/A"),
    BBA3_095("BBA3-095","BBA3","caseType","SSCS1","BEREAVEMENT SUPPORT PAYMENT SCHEME","Cynllun Taliad Cymorth Profedigaeth",List.of(JUDGE),"N/A"),
    BBA3_096("BBA3-096","BBA3","caseType","SSCS5","WORKING FAMILIES TAX CREDIT","null",List.of(),"N/A"),
    BBA3_099("BBA3-099","BBA3","caseType","null","OTHERS (EXTINCT/RARE BENEFITS)","null",List.of(),"N/A");

    private final String key;
    private final String serviceCode;
    private final String categoryType;
    private final String TBD;
    private final String typeDescriptionEN;
    private final String typeDescriptionCY;
    private final List<CaseTypePanelMembers> panelMembers;
    private final String parentCode;

    public static CaseType getCaseType(String value) {
        for (CaseType ct : CaseType.values()) {
            if (ct.getTypeDescriptionEN().equals(value)) {
                return ct;
            }
        }
        return null;
    }
}

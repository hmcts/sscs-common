package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterpreterLanguage {

    ACH_ACH("ach", "Acholi", null),
    AFR_AFR("afr", "Afrikaans", null),
    AKA_AKA("aka", "Akan", null),
    ALB_ALB("alb", "Albanian", null),
    ARQ_ARQ("arq", "Algerian", null),
    AMH_AMH("amh", "Amharic", null),
    ARA_ARA("ara", "Arabic", null),
    ARA_MIDDLEEASTERN("ara-middleEastern", "Arabic", "Middle Eastern"),
    ARA_NORTHAFRICAN("ara-northAfrican", "Arabic", "North African"),
    ARM_ARM("arm", "Armenian", null),
    AII_AII("aii", "Assyrian", null),
    TEO_TEO("teo", "Ateso", null),
    AZE_AZE("aze", "Azerbajani (aka Nth Azari)", null),
    BJS_BJS("bjs", "Bajan (West Indian)", null),
    BAL_BAL("bal", "Baluchi", null),
    BAM_BAM("bam", "Bambara", null),
    BAS_BAS("bas", "Bassa", null),
    BEL_BEL("bel", "Belorussian", null),
    BEM_BEM("bem", "Benba (Bemba)", null),
    BEN_BEN("ben", "Bengali", null),
    BEN_SYLHETI("ben-sylheti", "Bengali", "Sylheti"),
    BIN_BIN("bin", "Benin/Edo", null),
    BER_BER("ber", "Berber", null),
    BTN_BTN("btn", "Bhutanese", null),
    BIH_BIH("bih", "Bihari", null),
    BYN_BYN("byn", "Bilin", null),
    ABR_ABR("abr", "Brong", null),
    BUL_BUL("bul", "Bulgarian", null),
    BUR_BUR("bur", "Burmese", null),
    YUE_YUE("yue", "Cantonese", null),
    CEB_CEB("ceb", "Cebuano", null),
    CHE_CHE("che", "Chechen", null),
    NYA_NYA("nya", "Chichewa", null),
    CTG_CTG("ctg", "Chittagonain", null),
    CPE_CPE("cpe", "Creole (English)", null),
    CPF_CPF("cpf", "Creole (French)", null),
    CPP_CPP("cpp", "Creole (Portuguese)", null),
    CRP_CRP("crp", "Creole (Spanish)", null),
    CZE_CZE("cze", "Czech", null),
    DAN_DAN("dan", "Danish", null),
    PRS_PRS("prs", "Dari", null),
    DIN_DIN("din", "Dinka", null),
    DYU_DYU("dyu", "Dioula", null),
    DUA_DUA("dua", "Douala", null),
    DUT_DUT("dut", "Dutch", null),
    EFI_EFI("efi", "Efik", null),
    EST_EST("est", "Estonian", null),
    EWE_EWE("ewe", "Ewe", null),
    EWO_EWO("ewo", "Ewondo", null),
    FAT_FAT("fat", "Fanti", null),
    FAS_FAS("fas", "Farsi", null),
    FIJ_FIJ("fij", "Fijian", null),
    NLD_NLD("nld", "Flemish", null),
    FRE_FRE("fre", "French", null),
    FRE_ARABIC("fre-arabic", "French", "Arabic"),
    FRE_AFRICAN("fre-african", "French", "African"),
    FUL_FUL("ful", "Fula", null),
    GAA_GAA("gaa", "Ga", null),
    GEO_GEO("geo", "Georgian", null),
    GER_GER("ger", "German", null),
    HAC_HAC("hac", "Gorani", null),
    GRE_GRE("gre", "Greek", null),
    GUJ_GUJ("guj", "Gujarati", null),
    SGW_SGW("sgw", "Gurage", null),
    HAK_HAK("hak", "Hakka", null),
    HAU_HAU("hau", "Hausa", null),
    HEB_HEB("heb", "Hebrew", null),
    HER_HER("her", "Herero", null),
    HIN_HIN("hin", "Hindi", null),
    HND_HND("hnd", "Hindko", null),
    HUN_HUN("hun", "Hungarian", null),
    IBB_IBB("ibb", "Ibibio", null),
    IBO_IBO("ibo", "Igbo (Also Known As Ibo)", null),
    ILO_ILO("ilo", "Ilocano", null),
    IND_IND("ind", "Indonesian", null),
    ISO_ISO("iso", "Isoko", null),
    ITA_ITA("ita", "Italian", null),
    JAM_JAM("jam", "Jamaican", null),
    JPN_JPN("jpn", "Japanese", null),
    JAV_JAV("jav", "Javanese", null),
    GJK_GJK("gjk", "Kachi", null),
    KAS_KAS("kas", "Kashmiri", null),
    KCK_KCK("kck", "Khalanga", null),
    KHM_KHM("khm", "Khmer", null),
    KON_KON("kon", "Kikongo", null),
    KIK_KIK("kik", "Kikuyu", null),
    KIN_KIN("kin", "Kinyarwandan", null),
    RUN_RUN("run", "Kirundi", null),
    KNN_KNN("knn", "Konkani", null),
    KOR_KOR("kor", "Korean", null),
    KRI_KRI("kri", "Krio (Sierra Leone)", null),
    KRU_KRU("kru", "Kru", null),
    KUR_BARDINI("kur-bardini", "Kurdish", "Bardini"),
    KUR_KURMANJI("kur-kurmanji", "Kurdish", "kurmanji"),
    KUR_SORANI("kur-sorani", "Kurdish", "Sorani "),
    KFR_KFR("kfr", "Kutchi", null),
    KIR_KIR("kir", "Kyrgyz", null),
    LAJ_LAJ("laj", "Lango", null),
    LAV_LAV("lav", "Latvian", null),
    LIN_LIN("lin", "Lingala", null),
    LIT_LIT("lit", "Lithuanian", null),
    LUB_LUB("lub", "Luba (Tshiluba)", null),
    LUG_LUG("lug", "Lugandan", null),
    LUO_LUO("luo", "Luo", null),
    LUO_KENYAN("luo-kenyan", "Luo", "Kenyan"),
    LUO_ACHOLI("luo-acholi", "Luo", "Acholi"),
    LUO_LANGO("luo-lango", "Luo", "Lango"),
    XOG_XOG("xog", "Lusoga", null),
    MAC_MAC("mac", "Macedonian", null),
    MSA_MSA("msa", "Malay", null),
    MAL_MAL("mal", "Malayalam", null),
    DIV_DIV("div", "Maldivian", null),
    MKU_MKU("mku", "Malinke", null),
    MLT_MLT("mlt", "Maltese", null),
    CMN_CMN("cmn", "Mandarin", null),
    MNK_MNK("mnk", "Mandinka", null),
    MAR_MAR("mar", "Marathi", null),
    MEN_MEN("men", "Mende", null),
    MIN_MIN("min", "Mina", null),
    RON_RON("ron", "Moldovan", null),
    MON_MON("mon", "Mongolian", null),
    MKW_MKW("mkw", "Monokutuba", null),
    NDE_NDE("nde", "Ndebele", null),
    NEP_NEP("nep", "Nepali", null),
    NOR_NOR("nor", "Norwegian", null),
    NZI_NZI("nzi", "Nzima", null),
    ORM_ORM("orm", "Oromo", null),
    BFZ_BFZ("bfz", "Pahari", null),
    PAM_PAM("pam", "Pampangan", null),
    PAG_PAG("pag", "Pangasinan", null),
    PAT_PAT("pat", "Patois", null),
    POL_POL("pol", "Polish", null),
    POR_POR("por", "Portuguese", null),
    PHR_PHR("phr", "Pothohari", null),
    PAN_PAN("pan", "Punjabi", null),
    PAN_INDIAN("pan-indian", "Punjabi", "Indian"),
    PAN_PAKISTANI("pan-pakistani", "Punjabi", "Pakistani"),
    PUS_PUS("pus", "Pushtu (Also Known As Pashto)", null),
    RMM_RMM("rmm", "Roma", null),
    RUM_RUM("rum", "Romanian", null),
    ROM_ROM("rom", "Romany", null),
    CGG_CGG("cgg", "Rukiga", null),
    NYO_NYO("nyo", "Runyoro", null),
    RUS_RUS("rus", "Russian", null),
    TTJ_TTJ("ttj", "Rutoro", null),
    SKR_SKR("skr", "Saraiki (Seraiki)", null),
    KRN_KRN("krn", "Sarpo", null),
    HBS_HBS("hbs", "Serbo-Croatian", null),
    TSN_TSN("tsn", "Setswana", null),
    SCL_SCL("scl", "Shina", null),
    SNA_SNA("sna", "Shona", null),
    SND_SND("snd", "Sindhi", null),
    SIN_SIN("sin", "Sinhalese", null),
    SLO_SLO("slo", "Slovak", null),
    SLV_SLV("slv", "Slovenian", null),
    SOM_SOM("som", "Somali", null),
    SPA_SPA("spa", "Spanish", null),
    SUS_SUS("sus", "Susu", null),
    SWA_SWA("swa", "Swahili", null),
    SWA_KIBAJUNI("swa-kibajuni", "Swahili", "Kibajuni"),
    SWA_BRAVANESE("swa-bravanese", "Swahili", "Bravanese"),
    SWE_SWE("swe", "Swedish", null),
    SYL_SYL("syl", "Sylheti", null),
    TGL_TGL("tgl", "Tagalog", null),
    TAI_TAI("tai", "Taiwanese", null),
    TAM_TAM("tam", "Tamil", null),
    TEL_TEL("tel", "Telugu", null),
    TEM_TEM("tem", "Temne", null),
    THA_THA("tha", "Thai", null),
    TIB_TIB("tib", "Tibetan", null),
    TIG_TIG("tig", "Tigre", null),
    TIR_TIR("tir", "Tigrinya", null),
    DON_DON("don", "Toura", null),
    TUR_TUR("tur", "Turkish", null),
    TUK_TUK("tuk", "Turkmen", null),
    TWI_TWI("twi", "Twi", null),
    UIG_UIG("uig", "Uighur", null),
    UKR_UKR("ukr", "Ukrainian", null),
    URD_URD("urd", "Urdu", null),
    URH_URH("urh", "Urohobo", null),
    UZB_UZB("uzb", "Uzbek", null),
    VIE_VIE("vie", "Vietnamese", null),
    VSA_VSA("vsa", "Visayan", null),
    WEL_WEL("wel", "Welsh", null),
    WOL_WOL("wol", "Wolof", null),
    XHO_XHO("xho", "Xhosa", null),
    YOR_YOR("yor", "Yoruba", null),
    ZAG_ZAG("zag", "Zaghawa", null),
    ZZA_ZZA("zza", "Zaza", null),
    ZUL_ZUL("zul", "Zulu", null);

    private final String key;
    private final String language;
    private final String dialect;

    public static InterpreterLanguage getInterpreterLanguageByLanguageAndDialect(String languages, String dialect) {
        for (InterpreterLanguage il : InterpreterLanguage.values()) {
            if (il.getLanguage().equals(languages) && il.getDialect().equals(dialect)) {
                return il;
            }
        }
        return null;
    }

    public static InterpreterLanguage getLanguageAndConvert(String languages) {
        return Arrays.stream(InterpreterLanguage.values())
                .filter(c -> c.getLanguage().equals(languages))
                .findFirst()
                .orElse(conditionNotMet(languages));
    }

    //TODO: Finish of list of lanuages SSCS-10445
    public static InterpreterLanguage conditionNotMet(String languages) {
        switch (languages) {
            case "Arabic (Middle Eastern)":
                return ARA_MIDDLEEASTERN;
            case "Arabic (North African)":
                return ARA_NORTHAFRICAN;
            case "Ashanti":
                return TWI_TWI;
            case "Azari":
                return AZE_AZE;
            case "Bajuni":
                return SWA_KIBAJUNI;
            case "Bardini":
                return KUR_KURMANJI;
            case "Bengali (Sylheti)":
                return BEN_SYLHETI;
            case "Bharuchi":
                return GUJ_GUJ;
            case "Bravanese":
                return SWA_BRAVANESE;
            case "Cambellpuri":
                return null;
            case "Edo/Benin":
                return BIN_BIN;
            case "Emakhuna":
                return null;
            case "Feili":
                return null;
            case "French (Arabic)":
                return FRE_ARABIC;
            case "French(African)":
                return FRE_AFRICAN;
            case "Gallacian":
                return null;
            case "Guran":
                return null;
            case "Hendko":
                return HND_HND;
            case "Hokkien":
                return null;
            case "Ishan":
                return null;
            case "Karaninka":
                return MNK_MNK;
            case "Khymer Khymer":
                return KHM_KHM;
            case "Kibajuni,Kibanjuni,Bajuni,Ban":
                return SWA_KIBAJUNI;
            case "Kibanjuni":
                return SWA_KIBAJUNI;
            case "Kichagga":
                return null;
            case "Kisakata":
                return null;
            case "Kiswahili":
                return SWA_SWA;
            case "Kurdish (Bardini)":
                return KUR_BARDINI;
            case "Kurdish (Kurmanji)":
                return KUR_KURMANJI;
            case "Kurdish (Sorani)":
                return KUR_SORANI;
            case "Lugisa":
                return null;
            case "Lunyankole":
                return null;
            case "Luo (Kenyan)":
                return LUO_KENYAN;
            case "Luo (Ugandan[Acholi District])":
                return LUO_ACHOLI;
            case "Luo (Ugandan[Lango District])":
                return LUO_LANGO;
            case "Lutoro":
                return null;
            case "Maghreb":
                return null;
            case "Mirpuri":
                return null;
            case "Navsari":
                return GUJ_GUJ;
            case "Ngwa":
                return null;
            case "Pathwari":
                return BFZ_BFZ;
            case "Pidgin English":
                return null;
            case "Punjabi (Indian)":
                return PAN_INDIAN;
            case "Punjabi (Pakistani)":
                return PAN_PAKISTANI;
            case "Putonghue":
                return null;
            case "Sarahuleh":
                return null;
            case "Senegal (French) Olof Dialect":
                return null;
            case "Senegal (Wolof)":
                return null;
            case "Serbo‑Croatian":
                return HBS_HBS;
            case "Training":
                return null;
            default:
                return null;
        }
    }
}
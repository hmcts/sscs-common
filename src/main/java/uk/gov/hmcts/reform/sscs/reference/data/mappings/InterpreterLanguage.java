package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterpreterLanguage {
    ACH_ACH("ach-ach", "Acholi", null),
    AFR_AFR("afr-afr", "Afrikaans", null),
    AKA_AKA("aka-aka", "Akan", null),
    ALB_ALB("alb-alb", "Albanian", null),
    ARQ_ARQ("arq-arq", "Algerian", null),
    AMH_AMH("amh-amh", "Amharic", null),
    ARA_ARA("ara-ara", "Arabic", null),
    ARA_MIDDLEEASTERN("ara-middleEastern", "Arabic", "Middle Eastern"),
    ARA_NORTHAFRICAN("ara-northAfrican", "Arabic", "North African"),
    ARM_ARM("arm-arm", "Armenian", null),
    AII_AII("aii-aii", "Assyrian", null),
    TEO_TEO("teo-teo", "Ateso", null),
    AZE_AZE("aze-aze", "Azerbajani (aka Nth Azari)", null),
    BJS_BJS("bjs-bjs", "Bajan (West Indian)", null),
    BAL_BAL("bal-bal", "Baluchi", null),
    BAM_BAM("bam-bam", "Bambara", null),
    BAS_BAS("bas-bas", "Bassa", null),
    BEL_BEL("bel-bel", "Belorussian", null),
    BEM_BEM("bem-bem", "Benba (Bemba)", null),
    BEN_BEN("ben-ben", "Bengali", null),
    BEN_SYLHETI("ben-sylheti", "Bengali", "Sylheti"),
    BIN_BIN("bin-bin", "Benin/Edo", null),
    BER_BER("ber-ber", "Berber", null),
    BTN_BTN("btn-btn", "Bhutanese", null),
    BIH_BIH("bih-bih", "Bihari", null),
    BYN_BYN("byn-byn", "Bilin", null),
    ABR_ABR("abr-abr", "Brong", null),
    BUL_BUL("bul-bul", "Bulgarian", null),
    BUR_BUR("bur-bur", "Burmese", null),
    YUE_YUE("yue-yue", "Cantonese", null),
    CEB_CEB("ceb-ceb", "Cebuano", null),
    CHE_CHE("che-che", "Chechen", null),
    NYA_NYA("nya-nya", "Chichewa", null),
    CTG_CTG("ctg-ctg", "Chittagonain", null),
    CPE_CPE("cpe-cpe", "Creole (English)", null),
    CPF_CPF("cpf-cpf", "Creole (French)", null),
    CPP_CPP("cpp-cpp", "Creole (Portuguese)", null),
    CRP_CRP("crp-crp", "Creole (Spanish)", null),
    CZE_CZE("cze-cze", "Czech", null),
    DAN_DAN("dan-dan", "Danish", null),
    PRS_PRS("prs-prs", "Dari", null),
    DIN_DIN("din-din", "Dinka", null),
    DYU_DYU("dyu-dyu", "Dioula", null),
    DUA_DUA("dua-dua", "Douala", null),
    DUT_DUT("dut-dut", "Dutch", null),
    EFI_EFI("efi-efi", "Efik", null),
    EST_EST("est-est", "Estonian", null),
    EWE_EWE("ewe-ewe", "Ewe", null),
    EWO_EWO("ewo-ewo", "Ewondo", null),
    FAT_FAT("fat-fat", "Fanti", null),
    FAS_FAS("fas-fas", "Farsi", null),
    FIJ_FIJ("fij-fij", "Fijian", null),
    NLD_NLD("nld-nld", "Flemish", null),
    FRE_FRE("fre-fre", "French", null),
    FRE_ARABIC("fre-arabic", "French", "Arabic"),
    FRE_AFRICAN("fre-african", "French", "African"),
    FUL_FUL("ful-ful", "Fula", null),
    GAA_GAA("gaa-gaa", "Ga", null),
    GEO_GEO("geo-geo", "Georgian", null),
    GER_GER("ger-ger", "German", null),
    HAC_HAC("hac-hac", "Gorani", null),
    GRE_GRE("gre-gre", "Greek", null),
    GUJ_GUJ("guj-guj", "Gujarati", null),
    SGW_SGW("sgw-sgw", "Gurage", null),
    HAK_HAK("hak-hak", "Hakka", null),
    HAU_HAU("hau-hau", "Hausa", null),
    HEB_HEB("heb-heb", "Hebrew", null),
    HER_HER("her-her", "Herero", null),
    HIN_HIN("hin-hin", "Hindi", null),
    HND_HND("hnd-hnd", "Hindko", null),
    HUN_HUN("hun-hun", "Hungarian", null),
    IBB_IBB("ibb-ibb", "Ibibio", null),
    IBO_IBO("ibo-ibo", "Igbo (Also Known As Ibo)", null),
    ILO_ILO("ilo-ilo", "Ilocano", null),
    IND_IND("ind-ind", "Indonesian", null),
    ISO_ISO("iso-iso", "Isoko", null),
    ITA_ITA("ita-ita", "Italian", null),
    JAM_JAM("jam-jam", "Jamaican", null),
    JPN_JPN("jpn-jpn", "Japanese", null),
    JAV_JAV("jav-jav", "Javanese", null),
    GJK_GJK("gjk-gjk", "Kachi", null),
    KAS_KAS("kas-kas", "Kashmiri", null),
    KCK_KCK("kck-kck", "Khalanga", null),
    KHM_KHM("khm-khm", "Khmer", null),
    KON_KON("kon-kon", "Kikongo", null),
    KIK_KIK("kik-kik", "Kikuyu", null),
    KIN_KIN("kin-kin", "Kinyarwandan", null),
    RUN_RUN("run-run", "Kirundi", null),
    KNN_KNN("knn-knn", "Konkani", null),
    KOR_KOR("kor-kor", "Korean", null),
    KRI_KRI("kri-kri", "Krio (Sierra Leone)", null),
    KRU_KRU("kru-kru", "Kru", null),
    KUR_BARDINI("kur-bardini", "Kurdish", "Bardini"),
    KUR_KURMANJI("kur-kurmanji", "Kurdish", "kurmanji"),
    KUR_SORANI("kur-sorani", "Kurdish", "Sorani "),
    KFR_KFR("kfr-kfr", "Kutchi", null),
    KIR_KIR("kir-kir", "Kyrgyz", null),
    LAJ_LAJ("laj-laj", "Lango", null),
    LAV_LAV("lav-lav", "Latvian", null),
    LIN_LIN("lin-lin", "Lingala", null),
    LIT_LIT("lit-lit", "Lithuanian", null),
    LUB_LUB("lub-lub", "Luba (Tshiluba)", null),
    LUG_LUG("lug-lug", "Lugandan", null),
    LUO_LUO("luo-luo", "Luo", null),
    LUO_KENYAN("luo-kenyan", "Luo", "Kenyan"),
    LUO_ACHOLI("luo-acholi", "Luo", "Acholi"),
    LUO_LANGO("luo-lango", "Luo", "Lango"),
    XOG_XOG("xog-xog", "Lusoga", null),
    MAC_MAC("mac-mac", "Macedonian", null),
    MSA_MSA("msa-msa", "Malay", null),
    MAL_MAL("mal-mal", "Malayalam", null),
    DIV_DIV("div-div", "Maldivian", null),
    MKU_MKU("mku-mku", "Malinke", null),
    MLT_MLT("mlt-mlt", "Maltese", null),
    CMN_CMN("cmn-cmn", "Mandarin", null),
    MNK_MNK("mnk-mnk", "Mandinka", null),
    MAR_MAR("mar-mar", "Marathi", null),
    MEN_MEN("men-men", "Mende", null),
    MIN_MIN("min-min", "Mina", null),
    RON_RON("ron-ron", "Moldovan", null),
    MON_MON("mon-mon", "Mongolian", null),
    MKW_MKW("mkw-mkw", "Monokutuba", null),
    NDE_NDE("nde-nde", "Ndebele", null),
    NEP_NEP("nep-nep", "Nepali", null),
    NOR_NOR("nor-nor", "Norwegian", null),
    NZI_NZI("nzi-nzi", "Nzima", null),
    ORM_ORM("orm-orm", "Oromo", null),
    BFZ_BFZ("bfz-bfz", "Pahari", null),
    PAM_PAM("pam-pam", "Pampangan", null),
    PAG_PAG("pag-pag", "Pangasinan", null),
    PAT_PAT("pat-pat", "Patois", null),
    POL_POL("pol-pol", "Polish", null),
    POR_POR("por-por", "Portuguese", null),
    PHR_PHR("phr-phr", "Pothohari", null),
    PAN_PAN("pan-pan", "Punjabi", null),
    PAN_INDIAN("pan-indian", "Punjabi", "Indian"),
    PAN_PAKISTANI("pan-pakistani", "Punjabi", "Pakistani"),
    PUS_PUS("pus-pus", "Pushtu (Also Known As Pashto)", null),
    RMM_RMM("rmm-rmm", "Roma", null),
    RUM_RUM("rum-rum", "Romanian", null),
    ROM_ROM("rom-rom", "Romany", null),
    CGG_CGG("cgg-cgg", "Rukiga", null),
    NYO_NYO("nyo-nyo", "Runyoro", null),
    RUS_RUS("rus-rus", "Russian", null),
    TTJ_TTJ("ttj-ttj", "Rutoro", null),
    SKR_SKR("skr-skr", "Saraiki (Seraiki)", null),
    KRN_KRN("krn-krn", "Sarpo", null),
    HBS_HBS("hbs-hbs", "Serbo-Croatian", null),
    TSN_TSN("tsn-tsn", "Setswana", null),
    SCL_SCL("scl-scl", "Shina", null),
    SNA_SNA("sna-sna", "Shona", null),
    SND_SND("snd-snd", "Sindhi", null),
    SIN_SIN("sin-sin", "Sinhalese", null),
    SLO_SLO("slo-slo", "Slovak", null),
    SLV_SLV("slv-slv", "Slovenian", null),
    SOM_SOM("som-som", "Somali", null),
    SPA_SPA("spa-spa", "Spanish", null),
    SUS_SUS("sus-sus", "Susu", null),
    SWA_SWA("swa-swa", "Swahili", null),
    SWA_KIBAJUNI("swa-kibajuni", "Swahili", "Kibajuni"),
    SWA_BRAVANESE("swa-bravanese", "Swahili", "Bravanese"),
    SWE_SWE("swe-swe", "Swedish", null),
    SYL_SYL("syl-syl", "Sylheti", null),
    TGL_TGL("tgl-tgl", "Tagalog", null),
    TAI_TAI("tai-tai", "Taiwanese", null),
    TAM_TAM("tam-tam", "Tamil", null),
    TEL_TEL("tel-tel", "Telugu", null),
    TEM_TEM("tem-tem", "Temne", null),
    THA_THA("tha-tha", "Thai", null),
    TIB_TIB("tib-tib", "Tibetan", null),
    TIG_TIG("tig-tig", "Tigre", null),
    TIR_TIR("tir-tir", "Tigrinya", null),
    DON_DON("don-don", "Toura", null),
    TUR_TUR("tur-tur", "Turkish", null),
    TUK_TUK("tuk-tuk", "Turkmen", null),
    TWI_TWI("twi-twi", "Twi", null),
    UIG_UIG("uig-uig", "Uighur", null),
    UKR_UKR("ukr-ukr", "Ukrainian", null),
    URD_URD("urd-urd", "Urdu", null),
    URH_URH("urh-urh", "Urohobo", null),
    UZB_UZB("uzb-uzb", "Uzbek", null),
    VIE_VIE("vie-vie", "Vietnamese", null),
    VSA_VSA("vsa-vsa", "Visayan", null),
    WEL_WEL("wel-wel", "Welsh", null),
    WOL_WOL("wol-wol", "Wolof", null),
    XHO_XHO("xho-xho", "Xhosa", null),
    YOR_YOR("yor-yor", "Yoruba", null),
    ZAG_ZAG("zag-zag", "Zaghawa", null),
    ZZA_ZZA("zza-zza", "Zaza", null),
    ZUL_ZUL("zul-zul", "Zulu", null);

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

    public static InterpreterLanguage getLanguageAndConvert(String languages){
        return Arrays.stream(InterpreterLanguage.values())
            .filter(c -> c.getLanguage().equals(languages))
            .findFirst()
            .orElse(conditionNotMet(languages));
    }
    
    //TODO: Finish of list of lanuages SSCS-10445
    public static InterpreterLanguage conditionNotMet(String languages){
        switch (languages){
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
        }
        return null;
    }
}

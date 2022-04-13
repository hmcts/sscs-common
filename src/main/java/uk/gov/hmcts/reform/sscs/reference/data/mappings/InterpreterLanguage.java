package uk.gov.hmcts.reform.sscs.reference.data.mappings;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterpreterLanguage {
    ACH_ACH("ach-ach", "Acholi", ""),
    AFR_AFR("afr-afr", "Afrikaans", ""),
    AKA_AKA("aka-aka", "Akan", ""),
    ALB_ALB("alb-alb", "Albanian", ""),
    ARQ_ARQ("arq-arq", "Algerian", ""),
    AMH_AMH("amh-amh", "Amharic", ""),
    ARA_ARA("ara-ara", "Arabic", ""),
    ARA_MIDDLEEASTERN("ara-middleEastern", "Arabic", "Middle Eastern"),
    ARA_NORTHAFRICAN("ara-northAfrican", "Arabic", "North African"),
    ARM_ARM("arm-arm", "Armenian", ""),
    AII_AII("aii-aii", "Assyrian", ""),
    TEO_TEO("teo-teo", "Ateso", ""),
    AZE_AZE("aze-aze", "Azerbajani (aka Nth Azari)", ""),
    BJS_BJS("bjs-bjs", "Bajan (West Indian)", ""),
    BAL_BAL("bal-bal", "Baluchi", ""),
    BAM_BAM("bam-bam", "Bambara", ""),
    BAS_BAS("bas-bas", "Bassa", ""),
    BEL_BEL("bel-bel", "Belorussian", ""),
    BEM_BEM("bem-bem", "Benba (Bemba)", ""),
    BEN_BEN("ben-ben", "Bengali", ""),
    BEN_SYLHETI("ben-sylheti", "Bengali", "Sylheti"),
    BIN_BIN("bin-bin", "Benin/Edo", ""),
    BER_BER("ber-ber", "Berber", ""),
    BTN_BTN("btn-btn", "Bhutanese", ""),
    BIH_BIH("bih-bih", "Bihari", ""),
    BYN_BYN("byn-byn", "Bilin", ""),
    ABR_ABR("abr-abr", "Brong", ""),
    BUL_BUL("bul-bul", "Bulgarian", ""),
    BUR_BUR("bur-bur", "Burmese", ""),
    YUE_YUE("yue-yue", "Cantonese", ""),
    CEB_CEB("ceb-ceb", "Cebuano", ""),
    CHE_CHE("che-che", "Chechen", ""),
    NYA_NYA("nya-nya", "Chichewa", ""),
    CTG_CTG("ctg-ctg", "Chittagonain", ""),
    CPE_CPE("cpe-cpe", "Creole (English)", ""),
    CPF_CPF("cpf-cpf", "Creole (French)", ""),
    CPP_CPP("cpp-cpp", "Creole (Portuguese)", ""),
    CRP_CRP("crp-crp", "Creole (Spanish)", ""),
    CZE_CZE("cze-cze", "Czech", ""),
    DAN_DAN("dan-dan", "Danish", ""),
    PRS_PRS("prs-prs", "Dari", ""),
    DIN_DIN("din-din", "Dinka", ""),
    DYU_DYU("dyu-dyu", "Dioula", ""),
    DUA_DUA("dua-dua", "Douala", ""),
    DUT_DUT("dut-dut", "Dutch", ""),
    EFI_EFI("efi-efi", "Efik", ""),
    EST_EST("est-est", "Estonian", ""),
    EWE_EWE("ewe-ewe", "Ewe", ""),
    EWO_EWO("ewo-ewo", "Ewondo", ""),
    FAT_FAT("fat-fat", "Fanti", ""),
    FAS_FAS("fas-fas", "Farsi", ""),
    FIJ_FIJ("fij-fij", "Fijian", ""),
    NLD_NLD("nld-nld", "Flemish", ""),
    FRE_FRE("fre-fre", "French", ""),
    FRE_ARABIC("fre-arabic", "French", "Arabic"),
    FRE_AFRICAN("fre-african", "French", "African"),
    FUL_FUL("ful-ful", "Fula", ""),
    GAA_GAA("gaa-gaa", "Ga", ""),
    GEO_GEO("geo-geo", "Georgian", ""),
    GER_GER("ger-ger", "German", ""),
    HAC_HAC("hac-hac", "Gorani", ""),
    GRE_GRE("gre-gre", "Greek", ""),
    GUJ_GUJ("guj-guj", "Gujarati", ""),
    SGW_SGW("sgw-sgw", "Gurage", ""),
    HAK_HAK("hak-hak", "Hakka", ""),
    HAU_HAU("hau-hau", "Hausa", ""),
    HEB_HEB("heb-heb", "Hebrew", ""),
    HER_HER("her-her", "Herero", ""),
    HIN_HIN("hin-hin", "Hindi", ""),
    HND_HND("hnd-hnd", "Hindko", ""),
    HUN_HUN("hun-hun", "Hungarian", ""),
    IBB_IBB("ibb-ibb", "Ibibio", ""),
    IBO_IBO("ibo-ibo", "Igbo (Also Known As Ibo)", ""),
    ILO_ILO("ilo-ilo", "Ilocano", ""),
    IND_IND("ind-ind", "Indonesian", ""),
    ISO_ISO("iso-iso", "Isoko", ""),
    ITA_ITA("ita-ita", "Italian", ""),
    JAM_JAM("jam-jam", "Jamaican", ""),
    JPN_JPN("jpn-jpn", "Japanese", ""),
    JAV_JAV("jav-jav", "Javanese", ""),
    GJK_GJK("gjk-gjk", "Kachi", ""),
    KAS_KAS("kas-kas", "Kashmiri", ""),
    KCK_KCK("kck-kck", "Khalanga", ""),
    KHM_KHM("khm-khm", "Khmer", ""),
    KON_KON("kon-kon", "Kikongo", ""),
    KIK_KIK("kik-kik", "Kikuyu", ""),
    KIN_KIN("kin-kin", "Kinyarwandan", ""),
    RUN_RUN("run-run", "Kirundi", ""),
    KNN_KNN("knn-knn", "Konkani", ""),
    KOR_KOR("kor-kor", "Korean", ""),
    KRI_KRI("kri-kri", "Krio (Sierra Leone)", ""),
    KRU_KRU("kru-kru", "Kru", ""),
    KUR_BARDINI("kur-bardini", "Kurdish", "Bardini"),
    KUR_KURMANJI("kur-kurmanji", "Kurdish", "kurmanji"),
    KUR_SORANI("kur-sorani", "Kurdish", "Sorani "),
    KFR_KFR("kfr-kfr", "Kutchi", ""),
    KIR_KIR("kir-kir", "Kyrgyz", ""),
    LAJ_LAJ("laj-laj", "Lango", ""),
    LAV_LAV("lav-lav", "Latvian", ""),
    LIN_LIN("lin-lin", "Lingala", ""),
    LIT_LIT("lit-lit", "Lithuanian", ""),
    LUB_LUB("lub-lub", "Luba (Tshiluba)", ""),
    LUG_LUG("lug-lug", "Lugandan", ""),
    LUO_LUO("luo-luo", "Luo", ""),
    LUO_KENYAN("luo-kenyan", "Luo", "Kenyan"),
    LUO_ACHOLI("luo-acholi", "Luo", "Acholi"),
    LUO_LANGO("luo-lango", "Luo", "Lango"),
    XOG_XOG("xog-xog", "Lusoga", ""),
    MAC_MAC("mac-mac", "Macedonian", ""),
    MSA_MSA("msa-msa", "Malay", ""),
    MAL_MAL("mal-mal", "Malayalam", ""),
    DIV_DIV("div-div", "Maldivian", ""),
    MKU_MKU("mku-mku", "Malinke", ""),
    MLT_MLT("mlt-mlt", "Maltese", ""),
    CMN_CMN("cmn-cmn", "Mandarin", ""),
    MNK_MNK("mnk-mnk", "Mandinka", ""),
    MAR_MAR("mar-mar", "Marathi", ""),
    MEN_MEN("men-men", "Mende", ""),
    MIN_MIN("min-min", "Mina", ""),
    RON_RON("ron-ron", "Moldovan", ""),
    MON_MON("mon-mon", "Mongolian", ""),
    MKW_MKW("mkw-mkw", "Monokutuba", ""),
    NDE_NDE("nde-nde", "Ndebele", ""),
    NEP_NEP("nep-nep", "Nepali", ""),
    NOR_NOR("nor-nor", "Norwegian", ""),
    NZI_NZI("nzi-nzi", "Nzima", ""),
    ORM_ORM("orm-orm", "Oromo", ""),
    BFZ_BFZ("bfz-bfz", "Pahari", ""),
    PAM_PAM("pam-pam", "Pampangan", ""),
    PAG_PAG("pag-pag", "Pangasinan", ""),
    PAT_PAT("pat-pat", "Patois", ""),
    POL_POL("pol-pol", "Polish", ""),
    POR_POR("por-por", "Portuguese", ""),
    PHR_PHR("phr-phr", "Pothohari", ""),
    PAN_PAN("pan-pan", "Punjabi", ""),
    PAN_INDIAN("pan-indian", "Punjabi", "Indian"),
    PAN_PAKISTANI("pan-pakistani", "Punjabi", "Pakistani"),
    PUS_PUS("pus-pus", "Pushtu (Also Known As Pashto)", ""),
    RMM_RMM("rmm-rmm", "Roma", ""),
    RUM_RUM("rum-rum", "Romanian", ""),
    ROM_ROM("rom-rom", "Romany", ""),
    CGG_CGG("cgg-cgg", "Rukiga", ""),
    NYO_NYO("nyo-nyo", "Runyoro", ""),
    RUS_RUS("rus-rus", "Russian", ""),
    TTJ_TTJ("ttj-ttj", "Rutoro", ""),
    SKR_SKR("skr-skr", "Saraiki (Seraiki)", ""),
    KRN_KRN("krn-krn", "Sarpo", ""),
    HBS_HBS("hbs-hbs", "Serbo-Croatian", ""),
    TSN_TSN("tsn-tsn", "Setswana", ""),
    SCL_SCL("scl-scl", "Shina", ""),
    SNA_SNA("sna-sna", "Shona", ""),
    SND_SND("snd-snd", "Sindhi", ""),
    SIN_SIN("sin-sin", "Sinhalese", ""),
    SLO_SLO("slo-slo", "Slovak", ""),
    SLV_SLV("slv-slv", "Slovenian", ""),
    SOM_SOM("som-som", "Somali", ""),
    SPA_SPA("spa-spa", "Spanish", ""),
    SUS_SUS("sus-sus", "Susu", ""),
    SWA_SWA("swa-swa", "Swahili", ""),
    SWA_KIBAJUNI("swa-kibajuni", "Swahili", "Kibajuni"),
    SWA_BRAVANESE("swa-bravanese", "Swahili", "Bravanese"),
    SWE_SWE("swe-swe", "Swedish", ""),
    SYL_SYL("syl-syl", "Sylheti", ""),
    TGL_TGL("tgl-tgl", "Tagalog", ""),
    TAI_TAI("tai-tai", "Taiwanese", ""),
    TAM_TAM("tam-tam", "Tamil", ""),
    TEL_TEL("tel-tel", "Telugu", ""),
    TEM_TEM("tem-tem", "Temne", ""),
    THA_THA("tha-tha", "Thai", ""),
    TIB_TIB("tib-tib", "Tibetan", ""),
    TIG_TIG("tig-tig", "Tigre", ""),
    TIR_TIR("tir-tir", "Tigrinya", ""),
    DON_DON("don-don", "Toura", ""),
    TUR_TUR("tur-tur", "Turkish", ""),
    TUK_TUK("tuk-tuk", "Turkmen", ""),
    TWI_TWI("twi-twi", "Twi", ""),
    UIG_UIG("uig-uig", "Uighur", ""),
    UKR_UKR("ukr-ukr", "Ukrainian", ""),
    URD_URD("urd-urd", "Urdu", ""),
    URH_URH("urh-urh", "Urohobo", ""),
    UZB_UZB("uzb-uzb", "Uzbek", ""),
    VIE_VIE("vie-vie", "Vietnamese", ""),
    VSA_VSA("vsa-vsa", "Visayan", ""),
    WEL_WEL("wel-wel", "Welsh", ""),
    WOL_WOL("wol-wol", "Wolof", ""),
    XHO_XHO("xho-xho", "Xhosa", ""),
    YOR_YOR("yor-yor", "Yoruba", ""),
    ZAG_ZAG("zag-zag", "Zaghawa", ""),
    ZZA_ZZA("zza-zza", "Zaza", ""),
    ZUL_ZUL("zul-zul", "Zulu", "");

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

package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static uk.gov.hmcts.reform.sscs.ccd.domain.Issue.*;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HearingDuration {
    DURATION_001_DD(1,DD,45,75,30,null,false),
    DURATION_001_UM(1,UM,30,60,30,null,false),
    DURATION_001_UM_ELEMENTS(1,UM,45,75,30,List.of("WC","SG"),false),
    DURATION_001_US(1,US,30,60,30,null,false),
    DURATION_001_US_ELEMENTS(1,US,45,75,30,List.of("WC","SG"),false),
    DURATION_002_AR(2,AR,60,90,30,null,false),
    DURATION_002_CC(2,CC,60,90,30,null,false),
    DURATION_002_CD(2,CD,60,90,30,null,false),
    DURATION_002_CE(2,CE,60,90,30,null,false),
    DURATION_002_CF(2,CF,60,90,30,null,false),
    DURATION_002_CP(2,CP,60,90,30,null,false),
    DURATION_002_DD(2,DD,60,90,30,null,false),
    DURATION_002_EC(2,EC,60,90,30,null,false),
    DURATION_002_EI(2,EI,60,90,30,null,false),
    DURATION_002_EX(2,EX,60,90,30,null,false),
    DURATION_002_GC(2,GC,30,60,30,null,false),
    DURATION_002_LC(2,LC,60,90,30,null,false),
    DURATION_002_LE(2,LE,60,90,30,null,false),
    DURATION_002_MD(2,MD,60,90,30,null,false),
    DURATION_002_ML(2,ML,60,90,30,null,false),
    DURATION_002_MO(2,MO,60,90,30,null,false),
    DURATION_002_OC(2,OC,null,null,30,null,false),
    DURATION_002_ON(2,ON,60,90,30,null,false),
    DURATION_002_OS(2,OS,null,null,30,null,false),
    DURATION_002_OX(2,OX,null,null,30,null,false),
    DURATION_002_RA(2,RA,60,90,30,null,false),
    DURATION_002_RC(2,RC,60,90,30,null,false),
    DURATION_002_VW(2,VW,60,90,30,null,false),
    DURATION_003_AR(3,AR,60,90,30,null,false),
    DURATION_003_CC(3,CC,60,90,30,null,false),
    DURATION_003_CD(3,CD,60,90,30,null,false),
    DURATION_003_CE(3,CE,60,90,30,null,false),
    DURATION_003_CF(3,CF,60,90,30,null,false),
    DURATION_003_CP(3,CP,60,90,30,null,false),
    DURATION_003_DD(3,DD,60,90,30,null,false),
    DURATION_003_EC(3,EC,60,90,30,null,false),
    DURATION_003_EI(3,EI,60,90,30,null,false),
    DURATION_003_EX(3,EX,60,90,30,null,false),
    DURATION_003_GC(3,GC,30,60,30,null,false),
    DURATION_003_LC(3,LC,60,90,30,null,false),
    DURATION_003_LE(3,LE,60,90,30,null,false),
    DURATION_003_MD(3,MD,60,90,30,null,false),
    DURATION_003_ML(3,ML,60,90,30,null,false),
    DURATION_003_MO(3,MO,60,90,30,null,false),
    DURATION_003_OC(3,OC,null,null,30,null,false),
    DURATION_003_ON(3,ON,60,90,30,null,false),
    DURATION_003_OS(3,OS,null,null,30,null,false),
    DURATION_003_OX(3,OX,null,null,30,null,false),
    DURATION_003_RA(3,RA,60,90,30,null,false),
    DURATION_003_RC(3,RC,60,90,30,null,false),
    DURATION_003_VW(3,VW,60,90,30,null,false),
    DURATION_013_AR(13,AR,45,75,30,null,false),
    DURATION_013_CC(13,CC,45,75,30,null,false),
    DURATION_013_CD(13,CD,45,75,30,null,false),
    DURATION_013_CE(13,CE,45,75,30,null,false),
    DURATION_013_CF(13,CF,45,75,30,null,false),
    DURATION_013_CP(13,CP,45,75,30,null,false),
    DURATION_013_DD(13,DD,45,75,30,null,false),
    DURATION_013_DQ(13,DQ,45,75,30,null,false),
    DURATION_013_EC(13,EC,45,75,30,null,false),
    DURATION_013_EI(13,EI,45,75,30,null,false),
    DURATION_013_EX(13,EX,45,75,30,null,false),
    DURATION_013_GC(13,GC,30,60,30,null,false),
    DURATION_013_LE(13,LE,45,75,30,null,false),
    DURATION_013_MD(13,MD,45,75,30,null,false),
    DURATION_013_OC(13,OC,null,null,30,null,false),
    DURATION_013_ON(13,ON,45,75,30,null,false),
    DURATION_013_OS(13,OS,null,null,30,null,false),
    DURATION_013_OX(13,OX,null,null,30,null,false),
    DURATION_013_RA(13,RA,45,75,30,null,false),
    DURATION_013_RC(13,RC,45,75,30,null,false),
    DURATION_016_CC(16,CC,45,75,30,null,false),
    DURATION_016_CE(16,CE,45,75,30,null,false),
    DURATION_016_CP(16,CP,45,75,30,null,false),
    DURATION_016_DD(16,DD,45,75,30,null,false),
    DURATION_016_EC(16,EC,45,75,30,null,false),
    DURATION_016_EX(16,EX,45,75,30,null,false),
    DURATION_016_GC(16,GC,45,75,30,null,false),
    DURATION_016_LE(16,LE,45,75,30,null,false),
    DURATION_016_OC(16,OC,60,90,30,null,false),
    DURATION_016_ON(16,ON,45,75,30,null,false),
    DURATION_016_OS(16,OS,60,90,30,null,false),
    DURATION_016_OX(16,OX,60,90,30,null,false),
    DURATION_016_PF(16,PF,45,75,30,null,false),
    DURATION_016_RA(16,RA,45,75,30,null,false),
    DURATION_016_RC(16,RC,45,75,30,null,false),
    DURATION_016_RG(16,RG,45,75,30,null,false),
    DURATION_016_SU(16,SU,45,75,30,null,false),
    DURATION_022_CA(22,CA,null,null,30,null,false),
    DURATION_022_CC(22,CC,null,null,30,null,false),
    DURATION_022_CE(22,CE,null,null,30,null,false),
    DURATION_022_CP(22,CP,null,null,30,null,false),
    DURATION_022_DD(22,DD,null,null,30,null,false),
    DURATION_022_EC(22,EC,null,null,30,null,false),
    DURATION_022_EX(22,EX,null,null,30,null,false),
    DURATION_022_GC(22,GC,null,null,30,null,false),
    DURATION_022_LE(22,LE,null,null,30,null,false),
    DURATION_022_OC(22,OC,null,null,30,null,false),
    DURATION_022_ON(22,ON,null,null,30,null,false),
    DURATION_022_OS(22,OS,null,null,30,null,false),
    DURATION_022_OX(22,OX,null,null,30,null,false),
    DURATION_022_PC(22,PC,null,null,30,null,false),
    DURATION_022_RA(22,RA,null,null,30,null,false),
    DURATION_022_RB(22,RB,null,null,30,null,false),
    DURATION_022_RC(22,RC,null,null,30,null,false),
    DURATION_022_SS(22,SS,null,null,30,null,false),
    DURATION_022_SX(22,SX,null,null,30,null,false),
    DURATION_023_CA(23,CA,null,null,30,null,false),
    DURATION_023_CC(23,CC,null,null,30,null,false),
    DURATION_023_CE(23,CE,null,null,30,null,false),
    DURATION_023_CP(23,CP,null,null,30,null,false),
    DURATION_023_DD(23,DD,null,null,30,null,false),
    DURATION_023_EC(23,EC,null,null,30,null,false),
    DURATION_023_EX(23,EX,null,null,30,null,false),
    DURATION_023_GC(23,GC,null,null,30,null,false),
    DURATION_023_LE(23,LE,null,null,30,null,false),
    DURATION_023_OC(23,OC,null,null,30,null,false),
    DURATION_023_ON(23,ON,null,null,30,null,false),
    DURATION_023_OS(23,OS,null,null,30,null,false),
    DURATION_023_OX(23,OX,null,null,30,null,false),
    DURATION_023_PC(23,PC,null,null,30,null,false),
    DURATION_023_RA(23,RA,null,null,30,null,false),
    DURATION_023_RB(23,RB,null,null,30,null,false),
    DURATION_023_RC(23,RC,null,null,30,null,false),
    DURATION_023_SS(23,SS,null,null,30,null,false),
    DURATION_023_SX(23,SX,null,null,30,null,false),
    DURATION_024_CA(24,CA,null,null,30,null,false),
    DURATION_024_CC(24,CC,null,null,30,null,false),
    DURATION_024_CE(24,CE,null,null,30,null,false),
    DURATION_024_CP(24,CP,null,null,30,null,false),
    DURATION_024_DD(24,DD,null,null,30,null,false),
    DURATION_024_EC(24,EC,null,null,30,null,false),
    DURATION_024_EX(24,EX,null,null,30,null,false),
    DURATION_024_GC(24,GC,null,null,30,null,false),
    DURATION_024_LE(24,LE,null,null,30,null,false),
    DURATION_024_OC(24,OC,null,null,30,null,false),
    DURATION_024_ON(24,ON,null,null,30,null,false),
    DURATION_024_OS(24,OS,null,null,30,null,false),
    DURATION_024_OX(24,OX,null,null,30,null,false),
    DURATION_024_PC(24,PC,null,null,30,null,false),
    DURATION_024_RA(24,RA,null,null,30,null,false),
    DURATION_024_RB(24,RB,null,null,30,null,false),
    DURATION_024_RC(24,RC,null,null,30,null,false),
    DURATION_024_SS(24,SS,null,null,30,null,false),
    DURATION_024_SX(24,SX,null,null,30,null,false),
    DURATION_025_CA(25,CA,null,null,30,null,false),
    DURATION_025_CC(25,CC,null,null,30,null,false),
    DURATION_025_CE(25,CE,null,null,30,null,false),
    DURATION_025_CP(25,CP,null,null,30,null,false),
    DURATION_025_DD(25,DD,null,null,30,null,false),
    DURATION_025_EC(25,EC,null,null,30,null,false),
    DURATION_025_EX(25,EX,null,null,30,null,false),
    DURATION_025_GC(25,GC,null,null,30,null,false),
    DURATION_025_LE(25,LE,null,null,30,null,false),
    DURATION_025_OC(25,OC,null,null,30,null,false),
    DURATION_025_ON(25,ON,null,null,30,null,false),
    DURATION_025_OS(25,OS,null,null,30,null,false),
    DURATION_025_OX(25,OX,null,null,30,null,false),
    DURATION_025_PC(25,PC,null,null,30,null,false),
    DURATION_025_RA(25,RA,null,null,30,null,false),
    DURATION_025_RB(25,RB,null,null,30,null,false),
    DURATION_025_RC(25,RC,null,null,30,null,false),
    DURATION_025_SS(25,SS,null,null,30,null,false),
    DURATION_025_SX(25,SX,null,null,30,null,false),
    DURATION_026_CC(26,CC,null,null,30,null,false),
    DURATION_026_CE(26,CE,null,null,30,null,false),
    DURATION_026_CP(26,CP,null,null,30,null,false),
    DURATION_026_DD(26,DD,null,null,30,null,false),
    DURATION_026_EC(26,EC,null,null,30,null,false),
    DURATION_026_EX(26,EX,null,null,30,null,false),
    DURATION_026_GC(26,GC,null,null,30,null,false),
    DURATION_026_LE(26,LE,null,null,30,null,false),
    DURATION_026_OC(26,OC,null,null,30,null,false),
    DURATION_026_ON(26,ON,null,null,30,null,false),
    DURATION_026_OS(26,OS,null,null,30,null,false),
    DURATION_026_OX(26,OX,null,null,30,null,false),
    DURATION_026_PC(26,PC,null,null,30,null,false),
    DURATION_026_RA(26,RA,null,null,30,null,false),
    DURATION_026_RB(26,RB,null,null,30,null,false),
    DURATION_026_RC(26,RC,null,null,30,null,false),
    DURATION_026_SS(26,SS,null,null,30,null,false),
    DURATION_026_SX(26,SX,null,null,30,null,false),
    DURATION_028_CC(28,CC,null,null,30,null,false),
    DURATION_028_CE(28,CE,null,null,30,null,false),
    DURATION_028_CP(28,CP,null,null,30,null,false),
    DURATION_028_DD(28,DD,null,null,30,null,false),
    DURATION_028_EC(28,EC,null,null,30,null,false),
    DURATION_028_EX(28,EX,null,null,30,null,false),
    DURATION_028_GC(28,GC,null,null,30,null,false),
    DURATION_028_LE(28,LE,null,null,30,null,false),
    DURATION_028_OC(28,OC,null,null,30,null,false),
    DURATION_028_ON(28,ON,null,null,30,null,false),
    DURATION_028_OS(28,OS,null,null,30,null,false),
    DURATION_028_OX(28,OX,null,null,30,null,false),
    DURATION_028_PC(28,PC,null,null,30,null,false),
    DURATION_028_RA(28,RA,null,null,30,null,false),
    DURATION_028_RB(28,RB,null,null,30,null,false),
    DURATION_028_RC(28,RC,null,null,30,null,false),
    DURATION_028_SS(28,SS,null,null,30,null,false),
    DURATION_028_SX(28,SX,null,null,30,null,false),
    DURATION_030_CC(30,CC,45,75,30,null,false),
    DURATION_030_CE(30,CE,30,60,30,null,false),
    DURATION_030_CO(30,CO,45,75,30,null,false),
    DURATION_030_CP(30,CP,45,75,30,null,false),
    DURATION_030_CR(30,CR,45,75,30,null,false),
    DURATION_030_DD(30,DD,45,75,30,null,false),
    DURATION_030_EC(30,EC,45,75,30,null,false),
    DURATION_030_EX(30,EX,45,75,30,null,false),
    DURATION_030_GC(30,GC,45,75,30,null,false),
    DURATION_030_LE(30,LE,45,75,30,null,false),
    DURATION_030_OC(30,OC,60,90,30,null,false),
    DURATION_030_ON(30,ON,45,75,30,null,false),
    DURATION_030_OS(30,OS,60,90,30,null,false),
    DURATION_030_OX(30,OX,60,90,30,null,false),
    DURATION_030_RA(30,RA,45,75,30,null,false),
    DURATION_030_OI(30,OI,45,75,30,null,false),
    DURATION_030_RC(30,RC,45,75,30,null,false),
    DURATION_032_CC(32,CC,45,75,30,null,false),
    DURATION_032_CE(32,CE,30,60,30,null,false),
    DURATION_032_CP(32,CP,45,75,30,null,false),
    DURATION_032_CR(32,CR,45,75,30,null,false),
    DURATION_032_DD(32,DD,45,75,30,null,false),
    DURATION_032_GC(32,GC,30,60,30,null,false),
    DURATION_032_LE(32,LE,30,60,30,null,false),
    DURATION_032_OC(32,OC,60,90,30,null,false),
    DURATION_032_ON(32,ON,45,75,30,null,false),
    DURATION_032_OS(32,OS,60,90,30,null,false),
    DURATION_032_RA(32,RA,45,75,30,null,false),
    DURATION_032_RC(32,RC,45,75,30,null,false),
    DURATION_033_CC(33,CC,45,75,30,null,false),
    DURATION_033_CE(33,CE,30,60,30,null,false),
    DURATION_033_CP(33,CP,45,75,30,null,false),
    DURATION_033_CR(33,CR,45,75,30,null,false),
    DURATION_033_DD(33,DD,45,75,30,null,false),
    DURATION_033_GC(33,GC,30,60,30,null,false),
    DURATION_033_LE(33,LE,30,60,30,null,false),
    DURATION_033_OC(33,OC,60,90,30,null,false),
    DURATION_033_ON(33,ON,45,75,30,null,false),
    DURATION_033_OS(33,OS,60,90,30,null,false),
    DURATION_033_RA(33,RA,45,75,30,null,false),
    DURATION_033_RC(33,RC,45,75,30,null,false),
    DURATION_034_CC(34,CC,45,75,30,null,false),
    DURATION_034_CE(34,CE,30,60,30,null,false),
    DURATION_034_CP(34,CP,45,75,30,null,false),
    DURATION_034_DD(34,DD,45,75,30,null,false),
    DURATION_034_EC(34,EC,45,75,30,null,false),
    DURATION_034_EX(34,EX,45,75,30,null,false),
    DURATION_034_GC(34,GC,30,60,30,null,false),
    DURATION_034_LE(34,LE,30,60,30,null,false),
    DURATION_034_OC(34,OC,60,90,30,null,false),
    DURATION_034_ON(34,ON,45,75,30,null,false),
    DURATION_034_OS(34,OS,60,90,30,null,false),
    DURATION_034_OX(34,OX,60,90,30,null,false),
    DURATION_034_RA(34,RA,45,75,30,null,false),
    DURATION_034_RC(34,RC,45,75,30,null,false),
    DURATION_034_SS(34,SS,45,75,30,null,false),
    DURATION_034_OI(34,OI,45,75,30,null,false),
    DURATION_034_SX(34,SX,45,75,30,null,false),
    DURATION_035_CC(35,CC,45,75,30,null,false),
    DURATION_035_CE(35,CE,30,60,30,null,false),
    DURATION_035_CP(35,CP,45,75,30,null,false),
    DURATION_035_CR(35,CR,45,75,30,null,false),
    DURATION_035_DD(35,DD,45,75,30,null,false),
    DURATION_035_GC(35,GC,30,60,30,null,false),
    DURATION_035_LE(35,LE,30,60,30,null,false),
    DURATION_035_OC(35,OC,60,90,30,null,false),
    DURATION_035_ON(35,ON,45,75,30,null,false),
    DURATION_035_OS(35,OS,60,90,30,null,false),
    DURATION_035_RA(35,RA,45,75,30,null,false),
    DURATION_035_RC(35,RC,45,75,30,null,false),
    DURATION_037_AR(37,AR,60,90,30,null,false),
    DURATION_037_CC(37,CC,60,90,30,null,false),
    DURATION_037_CD(37,CD,60,90,30,null,false),
    DURATION_037_CE(37,CE,60,90,30,null,false),
    DURATION_037_CF(37,CF,60,90,30,null,false),
    DURATION_037_CP(37,CP,60,90,30,null,false),
    DURATION_037_DD(37,DD,60,90,30,null,false),
    DURATION_037_EC(37,EC,60,90,30,null,false),
    DURATION_037_EI(37,EI,60,90,30,null,false),
    DURATION_037_EX(37,EX,60,90,30,null,false),
    DURATION_037_GC(37,GC,30,60,30,null,false),
    DURATION_037_LE(37,LE,60,90,30,null,false),
    DURATION_037_MD(37,MD,60,90,30,null,false),
    DURATION_037_OC(37,OC,null,null,30,null,false),
    DURATION_037_OS(37,OS,null,null,30,null,false),
    DURATION_037_OX(37,OX,null,null,30,null,false),
    DURATION_037_RA(37,RA,60,90,30,null,false),
    DURATION_037_RC(37,RC,60,90,30,null,false),
    DURATION_045_AP(45,AP,45,75,30,null,false),
    DURATION_045_AS(45,AS,45,75,30,null,false),
    DURATION_045_BW(45,BW,45,75,30,null,false),
    DURATION_045_CC(45,CC,45,75,30,null,false),
    DURATION_045_CE(45,CE,30,60,30,null,false),
    DURATION_045_CL(45,CL,45,75,30,null,false),
    DURATION_045_CM(45,CM,45,75,30,null,false),
    DURATION_045_CO(45,CO,45,75,30,null,false),
    DURATION_045_CP(45,CP,45,75,30,null,false),
    DURATION_045_DA(45,DA,45,75,30,null,false),
    DURATION_045_DD(45,DD,45,75,30,null,false),
    DURATION_045_DP(45,DP,45,75,30,null,false),
    DURATION_045_EC(45,EC,45,75,30,null,false),
    DURATION_045_EX(45,EX,45,75,30,null,false),
    DURATION_045_HC(45,HC,45,75,30,null,false),
    DURATION_045_HT(45,HT,60,90,30,null,false),
    DURATION_045_IQ(45,IQ,45,75,30,null,false),
    DURATION_045_LE(45,LE,30,60,30,null,false),
    DURATION_045_LT(45,LT,60,90,30,null,false),
    DURATION_045_OC(45,OC,60,90,30,null,false),
    DURATION_045_OS(45,OS,60,90,30,null,false),
    DURATION_045_OX(45,OX,60,90,30,null,false),
    DURATION_045_PF(45,PF,45,75,30,null,false),
    DURATION_045_PR(45,PR,45,75,30,null,false),
    DURATION_045_RA(45,RA,45,75,30,null,false),
    DURATION_045_RB(45,RB,45,75,30,null,false),
    DURATION_045_RC(45,RC,45,75,30,null,false),
    DURATION_045_SF(45,SF,45,75,30,null,false),
    DURATION_045_SM(45,SM,45,75,30,null,false),
    DURATION_045_SS(45,SS,45,75,30,null,false),
    DURATION_045_SX(45,SX,45,75,30,null,false),
    DURATION_050_CC(50,CC,45,75,30,null,false),
    DURATION_050_CE(50,CE,30,60,30,null,false),
    DURATION_050_CP(50,CP,45,75,30,null,false),
    DURATION_050_DD(50,DD,45,75,30,null,false),
    DURATION_050_EX(50,EX,45,75,30,null,false),
    DURATION_050_GC(50,GC,30,60,30,null,false),
    DURATION_050_LE(50,LE,30,60,30,null,false),
    DURATION_050_OC(50,OC,60,90,30,null,false),
    DURATION_050_ON(50,ON,45,75,30,null,false),
    DURATION_050_OS(50,OS,60,90,30,null,false),
    DURATION_050_OX(50,OX,60,90,30,null,false),
    DURATION_050_RA(50,RA,45,75,30,null,false),
    DURATION_050_OI(50,OI,45,75,30,null,false),
    DURATION_050_RC(50,RC,45,75,30,null,false),
    DURATION_051_CB(51,CB,45,75,30,null,false),
    DURATION_051_CE(51,CE,30,60,30,null,false),
    DURATION_051_CP(51,CP,45,75,30,null,false),
    DURATION_051_DD(51,DD,45,75,30,null,false),
    DURATION_051_EC(51,EC,45,75,30,null,false),
    DURATION_051_FR(51,FR,45,75,30,null,false),
    DURATION_051_WI(51,WI,45,75,30,null,false),
    DURATION_051_GC(51,GC,30,60,30,null,false),
    DURATION_051_HT(51,HT,60,90,30,null,false),
    DURATION_051_IM(51,IM,45,75,30,null,false),
    DURATION_051_LE(51,LE,30,60,30,null,false),
    DURATION_051_LT(51,LT,60,90,30,null,false),
    DURATION_051_NC(51,NC,45,75,30,null,false),
    DURATION_051_OS(51,OS,60,90,30,null,false),
    DURATION_051_PC(51,PC,45,75,30,null,false),
    DURATION_051_RA(51,RA,45,75,30,null,false),
    DURATION_051_SG(51,SG,45,75,30,null,false),
    DURATION_051_TL(51,TL,45,75,30,null,false),
    DURATION_051_TP(51,TP,45,75,30,null,false),
    DURATION_051_WC(51,WC,45,75,30,null,false),
    DURATION_053_CC(53,CC,45,75,30,null,false),
    DURATION_053_CE(53,CE,30,60,30,null,false),
    DURATION_053_CP(53,CP,45,75,30,null,false),
    DURATION_053_DD(53,DD,45,75,30,null,false),
    DURATION_053_DQ(53,DQ,45,75,30,null,false),
    DURATION_053_EC(53,EC,45,75,30,null,false),
    DURATION_053_EX(53,EX,45,75,30,null,false),
    DURATION_053_FX(53,FX,45,75,30,null,false),
    DURATION_053_LE(53,LE,30,60,30,null,false),
    DURATION_053_OI(53,OI,45,75,30,null,false),
    DURATION_053_RA(53,RA,45,75,30,null,false),
    DURATION_053_RC(53,RC,45,75,30,null,false),
    DURATION_054_CC(54,CC,null,null,30,null,false),
    DURATION_054_CE(54,CE,30,60,30,null,false),
    DURATION_054_CP(54,CP,null,null,30,null,false),
    DURATION_054_DD(54,DD,45,75,30,null,false),
    DURATION_054_DQ(54,DQ,45,75,30,null,false),
    DURATION_054_EC(54,EC,null,null,30,null,false),
    DURATION_054_EX(54,EX,45,75,30,null,false),
    DURATION_054_FX(54,FX,null,null,30,null,false),
    DURATION_054_LE(54,LE,30,60,30,null,false),
    DURATION_054_OI(54,OI,45,75,30,null,false),
    DURATION_054_RA(54,RA,45,75,30,null,false),
    DURATION_054_VW(54,VW,45,75,30,null,false),
    DURATION_054_RC(54,RC,null,null,30,null,false),
    DURATION_055_CP(55,CP,45,75,30,null,false),
    DURATION_055_DD(55,DD,45,75,30,null,false),
    DURATION_055_EQ(55,EQ,45,75,30,null,false),
    DURATION_055_FC(55,FC,45,75,30,null,false),
    DURATION_055_OI(55,OI,45,75,30,null,false),
    DURATION_055_IS(55,IS,45,75,30,null,false),
    DURATION_057_CE(57,CE,30,60,30,null,false),
    DURATION_057_CP(57,CP,null,null,30,null,false),
    DURATION_057_DD(57,DD,45,75,30,null,false),
    DURATION_057_DK(57,DK,45,75,30,null,false),
    DURATION_057_DS(57,DS,45,75,30,null,false),
    DURATION_057_RO(57,RO,45,75,30,null,false),
    DURATION_057_EC(57,EC,45,75,30,null,false),
    DURATION_057_EX(57,EX,45,75,30,null,false),
    DURATION_057_LE(57,LE,30,60,30,null,false),
    DURATION_057_OI(57,OI,45,75,30,null,false),
    DURATION_057_RA(57,RA,45,75,30,null,false),
    DURATION_057_RC(57,RC,45,75,30,null,false),
    DURATION_057_DQ(57,DQ,45,75,30,null,false),
    DURATION_057_FX(57,FX,45,75,30,null,false),
    DURATION_057_TU(57,TU,45,75,30,null,false),
    DURATION_058_CE(58,CE,30,60,30,null,false),
    DURATION_058_CP(58,CP,45,75,30,null,false),
    DURATION_058_DD(58,DD,45,75,30,null,false),
    DURATION_061_AS(61,AS,45,75,30,null,false),
    DURATION_061_AT(61,AT,45,75,30,null,false),
    DURATION_061_BW(61,BW,45,75,30,null,false),
    DURATION_061_CC(61,CC,45,75,30,null,false),
    DURATION_061_CE(61,CE,30,60,30,null,false),
    DURATION_061_CL(61,CL,45,75,30,null,false),
    DURATION_061_CM(61,CM,45,75,30,null,false),
    DURATION_061_CO(61,CO,45,75,30,null,false),
    DURATION_061_CP(61,CP,45,75,30,null,false),
    DURATION_061_CR(61,CR,45,75,30,null,false),
    DURATION_061_DD(61,DD,45,75,30,null,false),
    DURATION_061_DP(61,DP,45,75,30,null,false),
    DURATION_061_DQ(61,DQ,45,75,30,null,false),
    DURATION_061_EC(61,EC,45,75,30,null,false),
    DURATION_061_EX(61,EX,45,75,30,null,false),
    DURATION_061_WI(61,WI,45,75,30,null,false),
    DURATION_061_GC(61,GC,30,60,30,null,false),
    DURATION_061_HC(61,HC,45,75,30,null,false),
    DURATION_061_HT(61,HT,60,90,30,null,false),
    DURATION_061_IA(61,IA,45,75,30,null,false),
    DURATION_061_ID(61,ID,45,75,30,null,false),
    DURATION_061_IQ(61,IQ,45,75,30,null,false),
    DURATION_061_LE(61,LE,30,60,30,null,false),
    DURATION_061_LM(61,LM,45,75,30,null,false),
    DURATION_061_LT(61,LT,60,90,30,null,false),
    DURATION_061_OC(61,OC,60,90,30,null,false),
    DURATION_061_ON(61,ON,45,75,30,null,false),
    DURATION_061_OS(61,OS,60,90,30,null,false),
    DURATION_061_OX(61,OX,60,90,30,null,false),
    DURATION_061_PC(61,PC,45,75,30,null,false),
    DURATION_061_PF(61,PF,45,75,30,null,false),
    DURATION_061_PR(61,PR,45,75,30,null,false),
    DURATION_061_RA(61,RA,45,75,30,null,false),
    DURATION_061_RB(61,RB,45,75,30,null,false),
    DURATION_061_RC(61,RC,45,75,30,null,false),
    DURATION_061_RG(61,RG,45,75,30,null,false),
    DURATION_061_SF(61,SF,45,75,30,null,false),
    DURATION_061_SM(61,SM,45,75,30,null,false),
    DURATION_061_SS(61,SS,45,75,30,null,false),
    DURATION_061_SU(61,SU,45,75,30,null,false),
    DURATION_061_SX(61,SX,45,75,30,null,false),
    DURATION_061_TT(61,TT,45,75,30,null,false),
    DURATION_061_WF(61,WF,45,75,30,null,false),
    DURATION_062_CB(62,CB,45,75,30,null,false),
    DURATION_062_CE(62,CE,30,60,30,null,false),
    DURATION_062_CP(62,CP,45,75,30,null,false),
    DURATION_062_DD(62,DD,45,75,30,null,false),
    DURATION_062_EC(62,EC,45,75,30,null,false),
    DURATION_062_GC(62,GC,30,60,30,null,false),
    DURATION_062_HT(62,HT,60,90,30,null,false),
    DURATION_062_IM(62,IM,45,75,30,null,false),
    DURATION_062_LE(62,LE,30,60,30,null,false),
    DURATION_062_LT(62,LT,60,90,30,null,false),
    DURATION_062_NC(62,NC,45,75,30,null,false),
    DURATION_062_OS(62,OS,60,90,30,null,false),
    DURATION_062_PC(62,PC,45,75,30,null,false),
    DURATION_062_RA(62,RA,45,75,30,null,false),
    DURATION_062_SG(62,SG,45,75,30,null,false),
    DURATION_062_TP(62,TP,45,75,30,null,false),
    DURATION_062_WC(62,WC,45,75,30,null,false),
    DURATION_067_CB(67,CB,null,null,30,null,true),
    DURATION_067_CC(67,CC,null,null,30,null,true),
    DURATION_067_CE(67,CE,null,null,30,null,true),
    DURATION_067_CP(67,CP,null,null,30,null,true),
    DURATION_067_CS(67,CS,null,null,30,null,true),
    DURATION_067_DD(67,DD,null,null,30,null,true),
    DURATION_067_DM(67,DM,null,null,30,null,true),
    DURATION_067_DQ(67,DQ,null,null,30,null,true),
    DURATION_067_EC(67,EC,null,null,30,null,true),
    DURATION_067_EX(67,EX,null,null,30,null,true),
    DURATION_067_FW(67,FW,null,null,30,null,true),
    DURATION_067_GC(67,GC,null,null,30,null,true),
    DURATION_067_IA(67,IA,null,null,30,null,true),
    DURATION_067_ID(67,ID,null,null,30,null,true),
    DURATION_067_LE(67,LE,null,null,30,null,true),
    DURATION_067_OK(67,OK,null,null,30,null,true),
    DURATION_067_ON(67,ON,null,null,30,null,true),
    DURATION_067_OS(67,OS,null,null,30,null,true),
    DURATION_067_OX(67,OX,null,null,30,null,true),
    DURATION_067_RA(67,RA,null,null,30,null,true),
    DURATION_067_RC(67,RC,null,null,30,null,true),
    DURATION_070_CC(70,CC,45,75,30,null,false),
    DURATION_070_CE(70,CE,30,60,30,null,false),
    DURATION_070_CP(70,CP,45,75,30,null,false),
    DURATION_070_DD(70,DD,45,75,30,null,false),
    DURATION_070_EC(70,EC,45,75,30,null,false),
    DURATION_070_EI(70,EI,45,75,30,null,false),
    DURATION_070_EX(70,EX,45,75,30,null,false),
    DURATION_070_GC(70,GC,30,60,30,null,false),
    DURATION_070_LE(70,LE,30,60,30,null,false),
    DURATION_070_ON(70,ON,45,75,30,null,false),
    DURATION_070_OS(70,OS,60,90,30,null,false),
    DURATION_070_OX(70,OX,60,90,30,null,false),
    DURATION_070_RA(70,RA,45,75,30,null,false),
    DURATION_070_RC(70,RC,45,75,30,null,false),
    DURATION_073_AS(73,AS,45,75,30,null,false),
    DURATION_073_AT(73,AT,45,75,30,null,false),
    DURATION_073_AV(73,AV,45,75,30,null,false),
    DURATION_073_AW(73,AW,45,75,30,null,false),
    DURATION_073_BW(73,BW,45,75,30,null,false),
    DURATION_073_CB(73,CB,45,75,30,null,false),
    DURATION_073_CC(73,CC,45,75,30,null,false),
    DURATION_073_CE(73,CE,30,60,30,null,false),
    DURATION_073_CL(73,CL,45,75,30,null,false),
    DURATION_073_CM(73,CM,45,75,30,null,false),
    DURATION_073_CO(73,CO,45,75,30,null,false),
    DURATION_073_CP(73,CP,45,75,30,null,false),
    DURATION_073_CR(73,CR,45,75,30,null,false),
    DURATION_073_DD(73,DD,45,75,30,null,false),
    DURATION_073_DR(73,DR,45,75,30,null,false),
    DURATION_073_EC(73,EC,45,75,30,null,false),
    DURATION_073_EX(73,EX,45,75,30,null,false),
    DURATION_073_FT(73,FT,45,75,30,null,false),
    DURATION_073_GC(73,GC,30,60,30,null,false),
    DURATION_073_HC(73,HC,45,75,30,null,false),
    DURATION_073_HT(73,HT,60,90,30,null,false),
    DURATION_073_IQ(73,IQ,45,75,30,null,false),
    DURATION_073_LE(73,LE,30,60,30,null,false),
    DURATION_073_LM(73,LM,45,75,30,null,false),
    DURATION_073_LT(73,LT,60,90,30,null,false),
    DURATION_073_OC(73,OC,60,90,30,null,false),
    DURATION_073_ON(73,ON,45,75,30,null,false),
    DURATION_073_OS(73,OS,60,90,30,null,false),
    DURATION_073_OX(73,OX,60,90,30,null,false),
    DURATION_073_PC(73,PC,45,75,30,null,false),
    DURATION_073_PF(73,PF,45,75,30,null,false),
    DURATION_073_RA(73,RA,45,75,30,null,false),
    DURATION_073_RB(73,RB,45,75,30,null,false),
    DURATION_073_RC(73,RC,45,75,30,null,false),
    DURATION_073_RE(73,RE,45,75,30,null,false),
    DURATION_073_RG(73,RG,45,75,30,null,false),
    DURATION_073_RR(73,RR,45,75,30,null,false),
    DURATION_073_RT(73,RT,45,75,30,null,false),
    DURATION_073_SM(73,SM,45,75,30,null,false),
    DURATION_073_SS(73,SS,45,75,30,null,false),
    DURATION_073_SU(73,SU,45,75,30,null,false),
    DURATION_073_SX(73,SX,45,75,30,null,false),
    DURATION_079_CC(79,CC,45,75,30,null,false),
    DURATION_079_CE(79,CE,30,60,30,null,false),
    DURATION_079_CP(79,CP,45,75,30,null,false),
    DURATION_079_DD(79,DD,45,75,30,null,false),
    DURATION_079_EC(79,EC,45,75,30,null,false),
    DURATION_079_EX(79,EX,45,75,30,null,false),
    DURATION_079_GC(79,GC,30,60,30,null,false),
    DURATION_079_LE(79,LE,30,60,30,null,false),
    DURATION_079_LT(79,LT,60,90,30,null,false),
    DURATION_079_ON(79,ON,45,75,30,null,false),
    DURATION_079_OS(79,OS,60,90,30,null,false),
    DURATION_079_OX(79,OX,60,90,30,null,false),
    DURATION_079_RA(79,RA,45,75,30,null,false),
    DURATION_079_RC(79,RC,45,75,30,null,false),
    DURATION_082_CB(82,CB,45,75,30,null,false),
    DURATION_082_CC(82,CC,45,75,30,null,false),
    DURATION_082_CE(82,CE,30,60,30,null,false),
    DURATION_082_CP(82,CP,45,75,30,null,false),
    DURATION_082_DD(82,DD,45,75,30,null,false),
    DURATION_082_EC(82,EC,45,75,30,null,false),
    DURATION_082_EX(82,EX,45,75,30,null,false),
    DURATION_082_GC(82,GC,30,60,30,null,false),
    DURATION_082_LE(82,LE,30,60,30,null,false),
    DURATION_082_LT(82,LT,60,90,30,null,false),
    DURATION_082_ON(82,ON,45,75,30,null,false),
    DURATION_082_OS(82,OS,60,90,30,null,false),
    DURATION_082_OX(82,OX,60,90,30,null,false),
    DURATION_082_RA(82,RA,45,75,30,null,false),
    DURATION_082_RC(82,RC,45,75,30,null,false),
    DURATION_088_CC(88,CC,45,75,30,null,false),
    DURATION_088_CE(88,CE,30,60,30,null,false),
    DURATION_088_CP(88,CP,45,75,30,null,false),
    DURATION_088_DD(88,DD,45,75,30,null,false),
    DURATION_088_EC(88,EC,45,75,30,null,false),
    DURATION_088_EX(88,EX,45,75,30,null,false),
    DURATION_088_GC(88,GC,30,60,30,null,false),
    DURATION_088_LE(88,LE,30,60,30,null,false),
    DURATION_088_OC(88,OC,60,90,30,null,false),
    DURATION_088_ON(88,ON,45,75,30,null,false),
    DURATION_088_OS(88,OS,60,90,30,null,false),
    DURATION_088_OX(88,OX,60,90,30,null,false),
    DURATION_088_PF(88,PF,45,75,30,null,false),
    DURATION_088_RA(88,RA,45,75,30,null,false),
    DURATION_088_RC(88,RC,45,75,30,null,false),
    DURATION_088_SF(88,SF,45,75,30,null,false),
    DURATION_088_SM(88,SM,45,75,30,null,false),
    DURATION_089_CC(89,CC,45,75,30,null,false),
    DURATION_089_CE(89,CE,30,60,30,null,false),
    DURATION_089_CP(89,CP,45,75,30,null,false),
    DURATION_089_DD(89,DD,45,75,30,null,false),
    DURATION_089_EC(89,EC,45,75,30,null,false),
    DURATION_089_EX(89,EX,45,75,30,null,false),
    DURATION_089_GC(89,GC,30,60,30,null,false),
    DURATION_089_LE(89,LE,30,60,30,null,false),
    DURATION_089_OC(89,OC,60,90,30,null,false),
    DURATION_089_ON(89,ON,45,75,30,null,false),
    DURATION_089_OS(89,OS,60,90,30,null,false),
    DURATION_089_OX(89,OX,60,90,30,null,false),
    DURATION_089_PF(89,PF,45,75,30,null,false),
    DURATION_089_RA(89,RA,45,75,30,null,false),
    DURATION_089_RC(89,RC,45,75,30,null,false),
    DURATION_089_SF(89,SF,45,75,30,null,false),
    DURATION_089_SM(89,SM,45,75,30,null,false),
    DURATION_090_DD(90,DD,45,75,30,null,false),
    DURATION_090_FF(90,FF,45,75,30,null,false),
    DURATION_094_CB(94,CB,45,75,30,null,false),
    DURATION_094_CC(94,CC,45,75,30,null,false),
    DURATION_094_CE(94,CE,30,60,30,null,false),
    DURATION_094_CP(94,CP,45,75,30,null,false),
    DURATION_094_DD(94,DD,45,75,30,null,false),
    DURATION_094_EC(94,EC,45,75,30,null,false),
    DURATION_094_EX(94,EX,45,75,30,null,false),
    DURATION_094_GC(94,GC,30,60,30,null,false),
    DURATION_094_LE(94,LE,30,60,30,null,false),
    DURATION_094_LT(94,LT,60,90,30,null,false),
    DURATION_094_OC(94,OC,60,90,30,null,false),
    DURATION_094_ON(94,ON,45,75,30,null,false),
    DURATION_094_OS(94,OS,60,90,30,null,false),
    DURATION_094_OX(94,OX,60,90,30,null,false),
    DURATION_094_RA(94,RA,45,75,30,null,false),
    DURATION_094_RC(94,RC,45,75,30,null,false),
    DURATION_095_CB(95,CB,45,75,30,null,false),
    DURATION_095_CC(95,CC,45,75,30,null,false),
    DURATION_095_CE(95,CE,30,60,30,null,false),
    DURATION_095_CP(95,CP,45,75,30,null,false),
    DURATION_095_DD(95,DD,45,75,30,null,false),
    DURATION_095_LE(95,LE,30,60,30,null,false),
    DURATION_095_OS(95,OS,60,90,30,null,false),
    DURATION_095_RA(95,RA,45,75,30,null,false),
    DURATION_096_CC(96,CC,null,null,30,null,false),
    DURATION_096_CE(96,CE,30,60,30,null,false),
    DURATION_096_CP(96,CP,null,null,30,null,false),
    DURATION_096_CR(96,CR,45,75,30,null,false),
    DURATION_096_DD(96,DD,45,75,30,null,false),
    DURATION_096_EC(96,EC,null,null,30,null,false),
    DURATION_096_EX(96,EX,45,75,30,null,false),
    DURATION_096_GC(96,GC,30,60,30,null,false),
    DURATION_096_LE(96,LE,30,60,30,null,false),
    DURATION_096_OC(96,OC,null,null,30,null,false),
    DURATION_096_ON(96,ON,45,75,30,null,false),
    DURATION_096_OS(96,OS,null,null,30,null,false),
    DURATION_096_OX(96,OX,null,null,30,null,false),
    DURATION_096_RA(96,RA,45,75,30,null,false),
    DURATION_096_RC(96,RC,null,null,30,null,false),
    DURATION_015_CE(15,CE,30,60,30,null,false),
    DURATION_015_DD(15,DD,45,75,30,null,false),
    DURATION_015_CC(15,CC,45,75,30,null,false),
    DURATION_015_EC(15,EC,45,75,30,null,false),
    DURATION_015_EX(15,EX,45,75,30,null,false),
    DURATION_015_LE(15,LE,30,60,30,null,false),
    DURATION_015_OI(15,OI,45,75,30,null,false),
    DURATION_015_RA(15,RA,45,75,30,null,false),
    DURATION_015_RC(15,RC,45,75,30,null,false),
    DURATION_015_DQ(15,DQ,45,75,30,null,false),
    DURATION_015_FX(15,FX,45,75,30,null,false),
    DURATION_064_AA(64,AA,null,null,30,null,true),
    DURATION_064_CC(64,CC,null,null,30,null,true),
    DURATION_064_CE(64,CE,null,null,30,null,true),
    DURATION_064_CP(64,CP,null,null,30,null,true),
    DURATION_064_CS(64,CS,null,null,30,null,true),
    DURATION_064_DD(64,DD,null,null,30,null,true),
    DURATION_064_DQ(64,DQ,null,null,30,null,true),
    DURATION_064_EC(64,EC,null,null,30,null,true),
    DURATION_064_EX(64,EX,null,null,30,null,true),
    DURATION_064_GC(64,GC,null,null,30,null,true),
    DURATION_064_IA(64,IA,null,null,30,null,true),
    DURATION_064_ID(64,ID,null,null,30,null,true),
    DURATION_064_LE(64,LE,null,null,30,null,true),
    DURATION_064_ON(64,ON,null,null,30,null,true),
    DURATION_064_OS(64,OS,null,null,30,null,true),
    DURATION_064_OX(64,OX,null,null,30,null,true),
    DURATION_064_RA(64,RA,null,null,30,null,true),
    DURATION_064_RC(64,RC,null,null,30,null,true);

    private final int benefitCode;
    private final Issue issue;
    private final Integer durationFaceToFace;
    private final Integer durationInterpreter;
    private final Integer durationPaper;
    private final List<String> elements;
    private final boolean panelSpecialism;

    public static HearingDuration getHearingDuration(String benefitCode, String issueCode, List<String> elements) {
        int benefitCodeInt = Integer.parseInt(benefitCode.replaceFirst("^0+(?!$)", ""));
        return getHearingDuration(benefitCodeInt, Issue.getIssue(issueCode), elements);
    }

    public static HearingDuration getHearingDuration(int benefitCode, Issue issue, List<String> elements) {
        return Arrays.stream(values())
                .filter(hearingDuration -> hearingDuration.benefitCode == benefitCode
                        && hearingDuration.issue == issue
                        && elementsMatch(hearingDuration, elements))
                .findFirst()
                .orElse(null);
    }

    private static boolean elementsMatch(HearingDuration duration, List<String> elements) {
        return (isEmpty(duration.elements)
                && isEmpty(elements))
                || isNotEmpty(elements)
                && isNotEmpty(duration.elements)
                && elements.stream().anyMatch(element -> duration.elements.stream().anyMatch(element::contains));
    }
}
